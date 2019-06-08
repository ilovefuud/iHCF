package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.struct.Relation;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.UUID;

public class FactionLeaveArgument extends SubCommand {

    private final HCF plugin;

    public FactionLeaveArgument(HCF plugin) {
        super("leave", "Leave your current faction.");
        this.plugin = plugin;
        this.playerOnly = true;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        UUID uuid = player.getUniqueId();
        if (playerFaction.getMember(uuid).getRole() == Role.LEADER) {
            player.sendMessage(ChatColor.RED + "You cannot leave factions as a leader. Either use " + ChatColor.GOLD + '/' + label + " disband" + ChatColor.RED + " or " +
                    ChatColor.GOLD + '/' + label + " leader" + ChatColor.RED + '.');

            return;
        }

        if (playerFaction.removeMember(player, player, player.getUniqueId(), false, false)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully left the faction.");
            playerFaction.broadcast(Relation.NEUTRAL.toChatColour() + player.getName() + ChatColor.YELLOW + " has left the faction.");
        }

    }
}
