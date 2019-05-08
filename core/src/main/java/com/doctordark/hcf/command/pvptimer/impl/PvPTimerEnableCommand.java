package com.doctordark.hcf.command.pvptimer.impl;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.timer.type.InvincibilityTimer;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

@RequiredArgsConstructor
public class PvPTimerEnableCommand implements PlayerSubCommand {

    private final HCF plugin;

    @Override
    public void execute(Player player, Player player1, String[] strings) {
        InvincibilityTimer pvpTimer = plugin.getTimerManager().getInvincibilityTimer();

        if (pvpTimer.getRemaining(player) <= 0L) {
            player.sendMessage(ChatColor.RED + "Your " + pvpTimer.getName() + ChatColor.RED + " timer is currently not active.");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Your " + pvpTimer.getName() + ChatColor.YELLOW + " timer is now off.");
        pvpTimer.clearCooldown(player);
    }
}
