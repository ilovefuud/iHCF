package com.doctordark.hcf.command.pvptimer;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.command.pvptimer.impl.PvPTimerEnableCommand;
import com.doctordark.hcf.command.pvptimer.impl.PvPTimerRemainingCommand;
import com.doctordark.hcf.timer.type.InvincibilityTimer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.misc.BukkitUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class PvpTimerCommand extends PlayerCommand {

    private final HCF plugin;
    private final Map<String, PlayerSubCommand> subCommandMap;


    public PvpTimerCommand(HCF plugin) {
        super("pvptimer");
        Map<String, PlayerSubCommand> subCommands = new HashMap<>();

        PvPTimerEnableCommand enableCommand = new PvPTimerEnableCommand(plugin);

        subCommands.put("enable", enableCommand);
        subCommands.put("off", enableCommand);
        subCommands.put("remove", enableCommand);
        subCommands.put("remaining", new PvPTimerRemainingCommand(plugin));

        subCommandMap = ImmutableMap.copyOf(subCommands);

        this.plugin = plugin;
    }

    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("enable", "time");

    /**
     * Prints the usage of this command to a sender.
     *
     * @param sender the sender to print for
     */
    private void printUsage(CommandSender sender, InvincibilityTimer pvpTimer) {
        sender.sendMessage(ChatColor.GOLD + "*** " + pvpTimer.getName() + " Timer Help ***");
        sender.sendMessage(ChatColor.GRAY + "/" + getName() + " enable - Removes your " + pvpTimer.getName() + ChatColor.GRAY + " timer.");
        sender.sendMessage(ChatColor.GRAY + "/" + getName() + " time - Check remaining " + pvpTimer.getName() + ChatColor.GRAY + " time.");
        sender.sendMessage(ChatColor.GRAY + "/lives - Life and deathban related commands.");
    }

    @Override
    public void execute(Player sender, String[] args) {
        String arg = args.length < 1 ? "help" : args[0].toLowerCase();
        PlayerSubCommand subCommand = subCommandMap.get(arg);

        InvincibilityTimer pvpTimer = plugin.getTimerManager().getInvincibilityTimer();

        if (subCommand == null) {
            printUsage(sender, pvpTimer);
            return;
        }

        Player target = args.length > 1 ? plugin.getServer().getPlayer(args[1]) : null;
        subCommand.execute(sender, target, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return args.length == 1 ? BukkitUtils.getCompletions(args, COMPLETIONS) : Collections.emptyList();
    }
}
