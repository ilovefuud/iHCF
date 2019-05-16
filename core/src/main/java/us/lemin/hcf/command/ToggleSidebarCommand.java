package us.lemin.hcf.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.user.FactionUser;

/**
 * Command used to toggle the sidebar for a {@link Player}.
 */
public class ToggleSidebarCommand extends PlayerCommand {

    private final HCF plugin;

    public ToggleSidebarCommand(HCF plugin) {
        super("togglesidebar");
        this.setAliases("scoreboard", "tsb", "togglescoreboard");
        this.plugin = plugin;
    }


    @Override
    public void execute(Player player, String[] strings) {

        FactionUser user = plugin.getUserManager().getUser(player.getUniqueId());
        boolean newVisible = user.isShowScoreboard();

        user.setShowScoreboard(newVisible);


        player.sendMessage(ChatColor.YELLOW + "Scoreboard sidebar is " + (newVisible ? ChatColor.GREEN + "now" : ChatColor.RED + "no longer") + ChatColor.YELLOW + " visible.");
    }
}