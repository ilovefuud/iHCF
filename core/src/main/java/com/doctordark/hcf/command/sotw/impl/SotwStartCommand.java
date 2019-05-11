package com.doctordark.hcf.command.sotw.impl;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.sotw.SotwTimer;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.time.TimeUtil;

public class SotwStartCommand extends SubCommand {

    private final HCF plugin;

    public SotwStartCommand(HCF plugin) {
        super("start");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {

        if (args.length != 1) {
            return;
        }

        long duration = TimeUtil.parseTime(args[0]);

        if (duration == -1L) {
            sender.sendMessage(ChatColor.RED + "'" + args[0] + "' is an invalid duration.");
            return;
        }

        if (duration < 1000L) {
            sender.sendMessage(ChatColor.RED + "SOTW protection time must last for at least 20 ticks.");
            return;
        }

        SotwTimer.SotwRunnable sotwRunnable = plugin.getSotwTimer().getSotwRunnable();

        if (sotwRunnable != null) {
            sender.sendMessage(ChatColor.RED + "SOTW protection is already enabled, use /sotw cancel to end it.");
            return;
        }

        plugin.getSotwTimer().start(duration);
        sender.sendMessage(ChatColor.RED + "Started SOTW protection for " + DurationFormatUtils.formatDurationWords(duration, true, true) + ".");
    }
}
