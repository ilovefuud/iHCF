package us.lemin.hcf.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.user.FactionUser;

/**
 * Command used to toggle messages shown when entering or leaving
 */
public class ToggleCapzoneEntryCommand extends PlayerCommand {

    private final HCF plugin;

    public ToggleCapzoneEntryCommand(HCF plugin) {
        super("togglecapzoneentry");
        this.plugin = plugin;
    }



    @Override
    public void execute(Player player, String[] strings) {
        FactionUser factionUser = plugin.getUserManager().getUser(player.getUniqueId());
        boolean newStatus = !factionUser.isCapzoneEntryAlerts();
        factionUser.setCapzoneEntryAlerts(newStatus);

        player.sendMessage(ChatColor.AQUA + "You will now " + (newStatus ? ChatColor.GREEN.toString() : ChatColor.RED + "un") + "able" +
                ChatColor.AQUA + " to see capture zone entry messages.");

    }
}
