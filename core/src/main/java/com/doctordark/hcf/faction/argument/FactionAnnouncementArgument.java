package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.Arrays;


public class FactionAnnouncementArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionAnnouncementArgument(HCF plugin) {
        super("announcement", "Set your faction announcement.");
        this.plugin = plugin;
        this.aliases = new String[]{"announce", "motd"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <newAnnouncement>";
    }

    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
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
