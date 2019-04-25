package com.doctordark.hcf.command;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.scoreboard.PlayerBoard;
import com.doctordark.hcf.user.FactionUser;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

/**
 * Command used to toggle the sidebar for a {@link Player}.
 */
public class ToggleSidebarCommand implements CommandExecutor, TabExecutor {

    private final HCF plugin;

    public ToggleSidebarCommand(HCF plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This oldcommands is only executable by players.");
            return true;
        }

        FactionUser user = plugin.getUserManager().getUser(((Player) sender).getUniqueId());
        boolean newVisible = user.isShowScoreboard();

        user.setShowScoreboard(newVisible);


        sender.sendMessage(ChatColor.YELLOW + "Scoreboard sidebar is " + (newVisible ? ChatColor.GREEN + "now" : ChatColor.RED + "no longer") + ChatColor.YELLOW + " visible.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}