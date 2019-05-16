package us.lemin.hcf.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;
import us.lemin.hcf.user.FactionUser;

public class DeathListener implements Listener {

    private final HCF plugin;

    public DeathListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerDeathKillIncrement(PlayerDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        FactionUser victimUser = plugin.getUserManager().getUser(event.getEntity().getUniqueId());
        victimUser.setDeaths(victimUser.getDeaths() + 1);
        victimUser.setKillstreak(0);
        if (killer != null) {
            FactionUser killerUser = plugin.getUserManager().getUser(killer.getUniqueId());
            killerUser.setKills(killerUser.getKills() + 1);
            killerUser.setKillstreak(killerUser.getKillstreak() + 1);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) return;

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
