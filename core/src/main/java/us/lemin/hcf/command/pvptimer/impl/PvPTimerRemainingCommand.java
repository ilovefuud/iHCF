package us.lemin.hcf.command.pvptimer.impl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.type.InvincibilityTimer;
import us.lemin.hcf.util.DurationFormatter;

public class PvPTimerRemainingCommand extends PlayerSubCommand {

    private final HCF plugin;

    public PvPTimerRemainingCommand(HCF plugin) {
        super("remaining");
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, Player player1, String[] strings, String s) {
        InvincibilityTimer pvpTimer = plugin.getTimerManager().getInvincibilityTimer();

        long remaining = pvpTimer.getRemaining(player);
        if (remaining <= 0L) {
            player.sendMessage(ChatColor.RED + "Your " + pvpTimer.getName() + ChatColor.RED + " timer is currently not active.");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Your " + pvpTimer.getName() + ChatColor.YELLOW + " timer is active for another " +
                ChatColor.BOLD + DurationFormatter.getRemaining(remaining, true, false) + ChatColor.YELLOW + (pvpTimer.isPaused(player) ? " and is currently paused" : "") + '.');

    }
}
