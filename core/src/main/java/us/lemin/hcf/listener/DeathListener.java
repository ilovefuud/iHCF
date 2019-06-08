package us.lemin.hcf.listener;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;
import us.lemin.hcf.user.FactionUser;

import java.util.ArrayList;
import java.util.List;

public class DeathListener implements Listener {

    private final HCF plugin;

    public DeathListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDeathKillIncrement(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        Entity entityKiller = killer;
        Player player = event.getEntity();
        if(killer == null){
            entityKiller = getKiller(event);
            if(entityKiller instanceof Player){
                killer = (Player) entityKiller;
            }
        }
        if(killer == null){
            EntityDamageEvent entityDamageEvent = player.getLastDamageCause();
            if(entityDamageEvent instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent entityDamageByEntityEvent = (EntityDamageByEntityEvent) entityDamageEvent;
                if(entityDamageByEntityEvent.getDamager() instanceof Player){
                    killer = (Player) entityDamageByEntityEvent.getDamager();
                }
                if(entityKiller == null){
                    entityKiller = entityDamageByEntityEvent.getDamager();
                }
            }
        }

        FactionUser slain = this.plugin.getUserManager().getUser(player.getUniqueId());
        slain.setKillstreak(0);
        slain.setDeaths(slain.getDeaths() + 1);
        String originalMessage = event.getDeathMessage();
        if (killer != null) {
            stattrack(player, killer);
            FactionUser user = this.plugin.getUserManager().getUser(killer.getUniqueId());
            user.setKills(user.getKills() + 1);
            user.setKillstreak(user.getKillstreak() + 1);
        }
        event.setDeathMessage(getDeathMessage(originalMessage, player, entityKiller));
    }

    private Entity getKiller(PlayerDeathEvent event) {
        EntityLiving lastAttacker = ((CraftPlayer) event.getEntity()).getHandle().lastDamager;
        return lastAttacker == null ? null : lastAttacker.getBukkitEntity();
    }

    private String getDeathMessage(String input, org.bukkit.entity.Entity entity, org.bukkit.entity.Entity killer) {
        input = input.replaceFirst("\\[", CC.GOLD + "[" + CC.GOLD);
        input = replaceLast(input, "]", CC.GOLD + "]" + CC.GOLD);
        if (entity != null) {
            input = input.replaceFirst("(?i)" + getEntityName(entity), CC.RED + getDisplayName(entity) + ChatColor.YELLOW);
        }
        if ((killer != null) && ((entity == null) || (!killer.equals(entity)))) {
            input = input.replaceFirst("(?i)" + getEntityName(killer), CC.RED + getDisplayName(killer) + ChatColor.YELLOW);
        }
        return input;
    }

    private String getStattrackMessage(org.bukkit.entity.Entity entity, org.bukkit.entity.Entity killer) {
        return CC.RED + getEntityName(entity) + ChatColor.YELLOW + " was slain by " + CC.RED + getEntityName(killer);
    }

    private String getEntityName(org.bukkit.entity.Entity entity) {
        Preconditions.checkNotNull(entity, "Entity cannot be null");
        return (entity instanceof Player) ? ((Player) entity).getName() : ((CraftEntity) entity).getHandle().getName();
    }

    private String getDisplayName(org.bukkit.entity.Entity entity) {
        Preconditions.checkNotNull(entity, "Entity cannot be null");
        if ((entity instanceof Player)) {
            Player player = (Player) entity;
            return player.getName() + CC.GOLD + '[' + CC.WHITE + this.plugin.getUserManager().getUser(player.getUniqueId()).getKills() + CC.GOLD + ']';
        }
        return WordUtils.capitalizeFully(entity.getType().name().replace('_', ' '));
    }

    public static String replaceLast(String text, String regex, String replacement) {
        return text.replaceFirst("(?s)" + regex + "(?!.*?" + regex + ')', replacement);
    }

    public static final String KILL_COUNTER_PREFIX =  ChatColor.YELLOW  + "Kill Counter: " + ChatColor.AQUA;

    public void stattrack(Player death, Player killer){
        ItemStack itemStack = killer.getItemInHand();
        if(itemStack != null){
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                List<String> lore = itemMeta.getLore();
                if(lore == null){
                    lore = new ArrayList<>();
                    lore.add(0, KILL_COUNTER_PREFIX + 1);
                }
                else{
                    boolean found = false;
                    for(int i = 0; i < lore.size(); i ++){
                        String loreLine = lore.get(i);
                        if(loreLine.startsWith(KILL_COUNTER_PREFIX)){
                            String killString = loreLine.substring(KILL_COUNTER_PREFIX.length());
                            int kills = Integer.parseInt(killString) + 1;
                            lore.remove(i);
                            lore.add(0, KILL_COUNTER_PREFIX + kills);
                            found = true;
                            break;
                        }
                    }
                    if(!found){
                        lore.add(0, KILL_COUNTER_PREFIX + 1);
                    }
                }
                lore.add(getStattrackMessage(death, killer));
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
                killer.setItemInHand(itemStack);
                killer.updateInventory();
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null || plugin.getConfiguration().isKitmap()) return;

        Faction factionAt = plugin.getFactionManager().getFactionAt(player.getLocation());
        double dtrLoss = (1.0D * factionAt.getDtrLossMultiplier());
        double newDtr = playerFaction.setDeathsUntilRaidable(playerFaction.getDeathsUntilRaidable() - dtrLoss);

        Role role = playerFaction.getMember(player.getUniqueId()).getRole();
        long baseDelay = plugin.getConfiguration().getFactionDtrRegenFreezeBaseMilliseconds();
        playerFaction.setRemainingRegenerationTime(baseDelay + (playerFaction.getOnlinePlayers().size() * plugin.getConfiguration().getFactionDtrRegenFreezeMillisecondsPerMember()));
        playerFaction.broadcast(ChatColor.GOLD + "Member Death: " + plugin.getConfiguration().getRelationColourTeammate() +
                role.getAstrix() + player.getName() + ChatColor.GOLD + ". " +
                "DTR: (" + ChatColor.WHITE + JavaUtils.format(newDtr, 2) + '/' + JavaUtils.format(playerFaction.getMaximumDeathsUntilRaidable(), 2) + ChatColor.GOLD + ").");
    }



}
