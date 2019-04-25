package com.doctordark.hcf.oldcommands;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.timer.type.LogoutTimer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;

import java.util.Collections;
import java.util.List;

public class LogoutCommand extends PlayerCommand {

    private final HCF plugin;

    public LogoutCommand(HCF plugin) {
        super("logout");
        setAliases("log", "disconnect");
        this.plugin = plugin;
    }
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public void execute(Player player, String[] strings) {
            LogoutTimer logoutTimer = plugin.getTimerManager().getLogoutTimer();

            if (!logoutTimer.setCooldown(player, player.getUniqueId())) {
                player.sendMessage(ChatColor.RED + "Your " + logoutTimer.getName() + ChatColor.RED + " timer is already active.");
                return;
            }

            player.sendMessage(ChatColor.RED + "Your " + logoutTimer.getName() + ChatColor.RED + " timer has started.");
        }
}
