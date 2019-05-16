package us.lemin.hcf.faction.argument.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

public class FactionForceLeaderArgument extends SubCommand {

    private final HCF plugin;

    public FactionForceLeaderArgument(HCF plugin) {
        super("forceleader", "Forces the leader of a faction.", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }


    @Override
    public void execute(CommandSender commandSender, Player player, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getContainingPlayerFaction(args[1]);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        FactionMember factionMember = playerFaction.getMember(args[1]);

        if (factionMember == null) {
            player.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        if (factionMember.getRole() == Role.LEADER) {
            player.sendMessage(ChatColor.RED + factionMember.getName() + " is already the leader of " + playerFaction.getDisplayName(player) + ChatColor.RED + '.');
            return;
        }

        FactionMember leader = playerFaction.getLeader();
        String oldLeaderName = leader == null ? "none" : leader.getName();
        String newLeaderName = factionMember.getName();

        // Demote the previous leader, promoting the new.
        if (leader != null) {
            leader.setRole(Role.CAPTAIN);
        }

        factionMember.setRole(Role.LEADER);
        playerFaction.broadcast(ChatColor.YELLOW + player.getName() + " has forcefully set the leader to " + newLeaderName + '.');

        player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Leader of " + playerFaction.getName() + " was forcefully set from " + oldLeaderName + " to " + newLeaderName + '.');

    }
}
