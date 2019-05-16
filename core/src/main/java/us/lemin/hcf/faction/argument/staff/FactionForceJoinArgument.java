package us.lemin.hcf.faction.argument.staff;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.ChatChannel;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;

/**
 * Faction argument used to forcefully join {@link Faction}s.
 */
public class FactionForceJoinArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionForceJoinArgument(HCF plugin) {
        super("forcejoin", "Forcefully join a faction.", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName>";
    }

    @Override
    public void execute(Player player, Player player1, String[] args, String label) {


        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction != null) {
            player.sendMessage(ChatColor.RED + "You are already in a faction.");
            return;
        }

        Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (faction == null) {
            player.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        if (!(faction instanceof PlayerFaction)) {
            player.sendMessage(ChatColor.RED + "You can only join player factions.");
            return;
        }

        playerFaction = (PlayerFaction) faction;
        if (playerFaction.addMember(player, player, player.getUniqueId(), new FactionMember(player, ChatChannel.PUBLIC, Role.MEMBER))) {
            playerFaction.broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + player.getName() + " has forcefully joined the faction.");
        }


    }
}
