package com.doctordark.hcf.combatlog;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.combatlog.event.LoggerSpawnEvent;
import com.doctordark.hcf.combatlog.type.LoggerEntity;
import com.doctordark.hcf.combatlog.type.LoggerEntityHuman;
import com.doctordark.hcf.faction.type.PlayerFaction;
import com.doctordark.hcf.util.XPUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

@RequiredArgsConstructor
public class CombatLogListener implements Listener {
    private final Map<Integer, CombatLogger> combatLoggers = new HashMap<>();
    private final Set<UUID> killedLoggers = new HashSet<>();
    private final Set<UUID> safelyDisconnected = new HashSet<>();


    private final HCF plugin;

    @EventHandler(priority = EventPriority.LOW)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!plugin.getConfiguration().isHandleCombatLogging()) {
            return;
        }

        if (player.getGameMode() != GameMode.CREATIVE && !player.isDead() && !result) {
            // If the player has PVP protection, don't spawn a logger
            if (plugin.getTimerManager().getInvincibilityTimer().getRemaining(uuid) > 0L) {
                return;
            }

            // There is no enemies near the player, so don't spawn a logger.
            if (plugin.getTimerManager().getTeleportTimer().getNearbyEnemies(player, NEARBY_SPAWN_RADIUS) <= 0 || plugin.getSotwTimer().getSotwRunnable() != null) {
                return;
            }

            // Make sure the player is not in a safezone.
            Location location = player.getLocation();
            if (plugin.getFactionManager().getFactionAt(location).isSafezone()) {
                return;
            }

            Entity entity = player.getWorld().spawnEntity(player.getLocation(), EntityType.PLAYER);

            entity.setCustomName(player.getDisplayName());
            entity.setCustomNameVisible(true);

            PlayerInventory playerInventory = player.getInventory();

            List<ItemStack> itemsToDrop = new ArrayList<>();

            for (ItemStack item : playerInventory.getContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    itemsToDrop.add(item);
                }
            }

            EntityEquipment entityEquipment = ((LivingEntity)entity).getEquipment();
            entityEquipment.setArmorContents(playerInventory.getArmorContents());

            for (ItemStack item : playerInventory.getArmorContents()) {
                if (item != null && item.getType() != Material.AIR) {
                    itemsToDrop.add(item);
                }
            }

            CombatLogger logger = new CombatLogger(player.getUniqueId(), itemsToDrop, XPUtil.getXP(player), entity);

            combatLoggers.put(entity.getEntityId(), logger);

            LoggerSpawnEvent calledEvent = new LoggerSpawnEvent(entity);
            Bukkit.getPluginManager().callEvent(calledEvent);
            if (calledEvent.isCancelled()) {
                entity.remove();
            }

            // remove the combat logger 30 seconds later
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (!entity.isDead()) {
                    removeVillagerLogger(entity);
                }
            }, 30 * 20L);

            LoggerEntity loggerEntity = new LoggerEntityHuman(player, location.getWorld());

        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLoggerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        CombatLogger logger = combatLoggers.get(event.getEntity().getEntityId());

        if (logger == null) {
            return;
        }

        Player damager = null;

        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();

            if (projectile.getShooter() instanceof Player) {
                damager = (Player) projectile.getShooter();
            }
        }

        if (damager == null) {
            return;
        }

        PlayerFaction damagerFaction = plugin.getFactionManager().getPlayerFaction(damager.getUniqueId());
        boolean allied = damagerFaction.getAlliedFactions().stream().anyMatch(alliedFaction -> alliedFaction.isOnTeam(logger.getId()));
        if (damagerFaction != null && damagerFaction.isOnTeam(logger.getId()) || allied) {
            event.setCancelled(true);
            damager.sendMessage(ChatColor.RED + "You can't kill a friendly combat logger!");
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onLoggerKill(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        CombatLogger logger = combatLoggers.remove(entity.getEntityId());

        if (logger == null) {
            return;
        }

        killedLoggers.add(logger.getId());

        for (ItemStack armor : logger.getItems()) {
            entity.getWorld().dropItemNaturally(entity.getLocation(), armor);
        }

        int xp = logger.getXp();

        if (xp > 0) {
            ExperienceOrb orb = (ExperienceOrb) entity.getWorld().spawnEntity(entity.getLocation(), EntityType.EXPERIENCE_ORB);
            orb.setExperience(xp);
        }

        Player player = event.getEntity().getKiller();

        if (player != null) {
            player.sendMessage(ChatColor.GREEN + "You killed a combat logger! Their items and experience have dropped on the ground.");
        }
    }

    @EventHandler
    public void onLoggerReJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (killedLoggers.remove(player.getUniqueId())) {
            PlayerInventory playerInventory = player.getInventory();

            playerInventory.clear();
            playerInventory.setArmorContents(null);

            XPUtil.setXP(player, 0);

            if (!player.isDead()) {
                player.setHealth(0.0);
            }

            player.sendMessage(CC.RED + "You were killed for combat logging.");
        } else {
            CombatLogger logger = combatLoggers.values().stream().filter(filtered -> filtered.getId().equals(player.getUniqueId())).findAny().orElse(null);

            if (logger != null) {
                Entity entity = logger.getEntity();

                if (!entity.isDead()) {
                    removeVillagerLogger(entity);
                }
            }
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        combatLoggers.values().stream().map(CombatLogger::getEntity).forEach(Entity::remove);
    }

    private void removeVillagerLogger(Entity entity) {
        combatLoggers.remove(entity.getEntityId());
        entity.remove();
    }

    @RequiredArgsConstructor
    @Getter
    private static final class CombatLogger {
        private final UUID id;
        private final List<ItemStack> items;
        private final int xp;
        private final Entity entity;
    }
}