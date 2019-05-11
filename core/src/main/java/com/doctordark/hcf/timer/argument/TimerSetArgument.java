package com.doctordark.hcf.timer.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.timer.PlayerTimer;
import com.doctordark.hcf.timer.Timer;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;

import java.util.regex.Pattern;

public class TimerSetArgument extends SubCommand {

    private static final Pattern WHITESPACE_TRIMMER = Pattern.compile("\\s");

    private final HCF plugin;

    public TimerSetArgument(HCF plugin) {
        super("set", "Set remaining timer time");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <timerName> <all|playerName> <remaining>";
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 4) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        long duration = JavaUtils.parse(args[3]);

        if (duration == -1L) {
            sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
            return;
        }

        TaskUtil.runAsync(plugin, () -> {
            PlayerTimer playerTimer = null;
            for (Timer timer : plugin.getTimerManager().getTimers()) {
                if (timer instanceof PlayerTimer && WHITESPACE_TRIMMER.matcher(timer.getName()).replaceAll("").equalsIgnoreCase(args[1])) {
                    playerTimer = (PlayerTimer) timer;
                    break;
                }
            }

            if (playerTimer == null) {
                sender.sendMessage(ChatColor.RED + "Timer '" + args[1] + "' not found.");
                return;
            }

            if (args[2].equalsIgnoreCase("all")) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    playerTimer.setCooldown(players, players.getUniqueId(), duration, true, null);
                }

                sender.sendMessage(ChatColor.BLUE + "Set timer " + playerTimer.getName() + " for all to " + DurationFormatUtils.formatDurationWords(duration, true, true) + '.');
            } else {
                ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[2]);
                if (profile == null) {
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                } else {
                    playerTimer.setCooldown(null, profile.getId(), duration, true, null);
                    sender.sendMessage(ChatColor.BLUE + "Set timer " + playerTimer.getName() + " duration to " + DurationFormatUtils.formatDurationWords(duration, true, true) + " for " + profile.getName() + '.');
                }

            }

        });


    }
}
