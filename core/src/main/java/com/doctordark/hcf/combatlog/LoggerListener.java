package com.doctordark.hcf.combatlog;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.combatlog.event.LoggerSpawnEvent;
import com.doctordark.hcf.combatlog.type.LoggerEntity;
import com.doctordark.hcf.combatlog.type.LoggerEntityHuman;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import com.doctordark.hcf.util.XPUtil;
import com.pvpraids.core.CorePlugin;
import com.pvpraids.core.player.CoreProfile;
import com.pvpraids.core.utils.message.CC;
import com.pvpraids.raid.RaidPlugin;
import com.pvpraids.raid.players.RaidPlayer;
import com.pvpraids.raid.team.Team;
import com.pvpraids.raid.util.XPUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.EntityPlayer;
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
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class LoggerListener implements Listener {

	private static final int NEARBY_SPAWN_RADIUS = 64;

	private final HCF plugin;
	private final Set<UUID> safelyDisconnected = new HashSet<>();
	private final Map<Integer, CombatLogger> combatLoggers = new HashMap<>();
	private final Set<UUID> killedLoggers = new HashSet<>();


	public void safelyDisconnect(Player player, String reason) {
		if (safelyDisconnected.add(player.getUniqueId())) {
			player.kickPlayer(reason);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		boolean result = safelyDisconnected.remove(uuid);
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

			// Make sure the player hasn't already spawned a logger.
			if (loggers.containsKey(player.getUniqueId())) {
				return;
			}

			LoggerEntity loggerEntity = new LoggerEntityHuman(player, location.getWorld());
			LoggerSpawnEvent calledEvent = new LoggerSpawnEvent(loggerEntity);
			Bukkit.getPluginManager().callEvent(calledEvent);
			if (!calledEvent.isCancelled()) {
				loggers.put(player.getUniqueId(), loggerEntity);

				new BukkitRunnable() {
					@Override
					public void run() {
						if (!player.isOnline()) { // just in-case
							loggerEntity.postSpawn(plugin);
						}
					}
				}.runTaskLater(plugin, 1L);
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();


		if (playerData == null || playerData.isSpawnProtected() || profile.isVanished()) {
			return;
		}

		List<Player> nearbyPlayers = player.getNearbyEntities(32.0, 32.0, 32.0).stream()
				.filter(Player.class::isInstance)
				.map(Player.class::cast)
				.filter(player::canSee)
				.filter(other -> !plugin.getPlayerManager().getPlayer(other).isSpawnProtected())
				.filter(other -> plugin.getTeamManager().getTeam(player) == null || !plugin.getTeamManager().getTeam(player).isOnTeam(other))
				.collect(Collectors.toList());

		if (playerData.hasCombatTag() || nearbyPlayers.size() > 0) {
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

			if (nearbyPlayers.size() > 0) {
				for (Player nearbyPlayer : nearbyPlayers) {
					nearbyPlayer.sendMessage(player.getDisplayName() + ChatColor.RED + " has combat logged! An entity has spawned at their location.");
				}
			}

			// remove the combat logger 30 seconds later
			plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
				if (!entity.isDead()) {
					removeVillagerLogger(entity);
				}
			}, 30 * 20L);
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

		PlayerFaction damagerTeam = plugin.getFactionManager().getPlayerFaction(damager);

		if (damagerTeam != null && damagerTeam.isOnTeam(logger.getId())) {
			event.setCancelled(true);
			damager.sendMessage(ChatColor.RED + "You can't kill your own team's combat logger!");
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onLoggerKill(EntityDeathEvent event) {
		event.getDrops().clear();

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

			player.sendMessage(ChatColor.RED + "You were killed for combat logging.");
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
