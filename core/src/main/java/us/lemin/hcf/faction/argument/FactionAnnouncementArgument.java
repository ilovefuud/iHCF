package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Arrays;


public class FactionAnnouncementArgument extends SubCommand {

    private final HCF plugin;

    public FactionAnnouncementArgument(HCF plugin) {
        super("announcement", "Set your faction announcement.");
        this.plugin = plugin;
        this.aliases = new String[]{"announce", "motd"};
        this.playerOnly = true;

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <newAnnouncement>";
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

        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage(ChatColor.RED + "You must be a officer to edit the faction announcement.");
            return;
        }

        String oldAnnouncement = playerFaction.getAnnouncement();
        String newAnnouncement;
        if (args[1].equalsIgnoreCase("clear") || args[1].equalsIgnoreCase("none") || args[1].equalsIgnoreCase("remove")) {
            newAnnouncement = null;
        } else {
            newAnnouncement = HCF.SPACE_JOINER.join(Arrays.copyOfRange(args, 1, args.length));
        }

        if (oldAnnouncement == null && newAnnouncement == null) {
            player.sendMessage(ChatColor.RED + "Your factions' announcement is already unset.");
            return;
        }

        if (oldAnnouncement != null && newAnnouncement != null && oldAnnouncement.equals(newAnnouncement)) {
            player.sendMessage(ChatColor.RED + "Your factions' announcement is already " + newAnnouncement + '.');
            return;
        }

        playerFaction.setAnnouncement(newAnnouncement);

        if (newAnnouncement == null) {
            playerFaction.broadcast(ChatColor.AQUA + player.getName() + ChatColor.YELLOW + " has cleared the factions' announcement.");
            return;
        }

        playerFaction.broadcast(ChatColor.AQUA + player.getName() + " has updated the factions' announcement from " +
                ChatColor.YELLOW + (oldAnnouncement != null ? oldAnnouncement : "none") +
                ChatColor.AQUA + " to " + ChatColor.YELLOW + newAnnouncement + ChatColor.AQUA + '.');
    }
}
