package us.lemin.hcf.timer.argument;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.PlayerTimer;
import us.lemin.hcf.timer.Timer;
import us.lemin.hcf.util.UUIDFetcher;

import java.util.UUID;

public class TimerCheckArgument extends SubCommand {

    private final HCF plugin;

    public TimerCheckArgument(HCF plugin) {
        super("check", "Check remaining timer time");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <timerName> <playerName>";
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerTimer temporaryTimer = null;
        for (Timer timer : plugin.getTimerManager().getTimers()) {
            if (timer instanceof PlayerTimer && timer.getName().equalsIgnoreCase(args[1])) {
                temporaryTimer = (PlayerTimer) timer;
                break;
            }
        }

        if (temporaryTimer == null) {
            sender.sendMessage(ChatColor.RED + "Timer '" + args[1] + "' not found.");
            return;
        }

        final PlayerTimer playerTimer = temporaryTimer;
        new BukkitRunnable() {
            @Override
            public void run() {
                UUID uuid;
                try {
                    uuid = UUIDFetcher.getUUIDOf(args[2]);
                } catch (Exception ex) {
                    sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[2] + ChatColor.GOLD + "' not found.");
                    return;
                }

                long remaining = playerTimer.getRemaining(uuid);
                sender.sendMessage(ChatColor.YELLOW + args[2] + " has timer " + playerTimer.getName() + " for another " + DurationFormatUtils.formatDurationWords(remaining, true, true));
            }
        }.runTaskAsynchronously(plugin);
    }
}
