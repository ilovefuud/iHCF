package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Set;

public class FactionUninviteArgument extends SubCommand {

    private final HCF plugin;

    public FactionUninviteArgument(HCF plugin) {
        super("uninvite", "Revoke an invitation to a player.");
        this.plugin = plugin;
        this.playerOnly = true;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <all|playerName>";
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

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        FactionMember factionMember = playerFaction.getMember(player);

        if (factionMember.getRole() == Role.MEMBER) {
            player.sendMessage(ChatColor.RED + "You must be a faction officer to un-invite players.");
            return;
        }

        Set<String> invitedPlayerNames = playerFaction.getInvitedPlayerNames();

        if (args[1].equalsIgnoreCase("all")) {
            invitedPlayerNames.clear();
            player.sendMessage(ChatColor.YELLOW + "You have cleared all pending invitations.");
            return;
        }

        if (!invitedPlayerNames.remove(args[1])) {
            player.sendMessage(ChatColor.RED + "There is not a pending invitation for " + args[1] + '.');
            return;
        }

        playerFaction.broadcast(ChatColor.YELLOW + factionMember.getRole().getAstrix() + player.getName() + " has uninvited " +
                plugin.getConfiguration().getRelationColourEnemy() + args[1] + ChatColor.YELLOW + " from the faction.");

    }
}
