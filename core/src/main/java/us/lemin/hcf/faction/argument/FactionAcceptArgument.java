package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.ChatChannel;
import us.lemin.hcf.faction.struct.Relation;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;

/**
 * Faction argument used to accept invitations from {@link Faction}s.
 */
public class FactionAcceptArgument extends SubCommand {

    private final HCF plugin;

    public FactionAcceptArgument(HCF plugin) {
        super("accept", "Accept a join request from an existing faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"join", "a"};
        this.playerOnly = true;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName>";
    }


    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }


        if (plugin.getFactionManager().getPlayerFaction(player) != null) {
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

        PlayerFaction targetFaction = (PlayerFaction) faction;

        if (targetFaction.getMembers().size() >= plugin.getConfiguration().getFactionMaxMembers()) {
            player.sendMessage(faction.getDisplayName(player) + ChatColor.RED + " is full. Faction limits are at " + plugin.getConfiguration().getFactionMaxMembers() + '.');
            return;
        }

        if (!targetFaction.isOpen() && !targetFaction.getInvitedPlayerNames().contains(player.getName())) {
            player.sendMessage(ChatColor.RED + faction.getDisplayName(player) + ChatColor.RED + " has not invited you.");
            return;
        }

        if (targetFaction.addMember(player, player, player.getUniqueId(), new FactionMember(player, ChatChannel.PUBLIC, Role.MEMBER))) {
            targetFaction.broadcast(Relation.MEMBER.toChatColour() + player.getName() + ChatColor.YELLOW + " has joined the faction.");
        }
    }
}
