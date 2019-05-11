package com.doctordark.hcf.deathban.lives;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.command.pvptimer.impl.PvPTimerEnableCommand;
import com.doctordark.hcf.command.pvptimer.impl.PvPTimerRemainingCommand;
import com.doctordark.hcf.deathban.lives.argument.*;
import com.doctordark.hcf.timer.type.InvincibilityTimer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.misc.BukkitUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class LivesExecutor extends PlayerCommand {

    private final HCF plugin;
    @Getter
    private final Map<String, SubCommand> subCommandMap;

    //TODO: add help argument

    public LivesExecutor(HCF plugin) {
        super("lives");
        Map<String, SubCommand> subCommands = new HashMap<>();

        subCommands.put("check", new LivesCheckArgument(plugin));
        subCommands.put("checkdeathban", new LivesCheckDeathbanArgument(plugin));
        subCommands.put("cleardeathbans", new LivesClearDeathbansArgument(plugin));
        subCommands.put("give", new LivesGiveArgument(plugin));
        subCommands.put("revive", new LivesReviveArgument(plugin));
        subCommands.put("set", new LivesSetArgument(plugin));
        subCommands.put("setdeathbantime", new LivesSetDeathbanTimeArgument(plugin));
        subCommands.put("help", new LivesHelpArgument(this));

        subCommandMap = ImmutableMap.copyOf(subCommands);

        this.plugin = plugin;
    }

    @Override
    public void execute(Player sender, String[] args) {
        String arg = args.length < 1 ? "help" : args[0].toLowerCase();
        SubCommand subCommand = subCommandMap.get(arg);


        if (subCommand == null) {
            for (SubCommand loop : subCommandMap.values()) {
                if (loop.getAliases() == null) continue;
                if (Arrays.stream(loop.getAliases())
                        .anyMatch(arg::equalsIgnoreCase)) {
                    Player target = args.length > 1 ? plugin.getServer().getPlayer(args[1]) : null;
                    loop.execute(sender, target, args, getLabel());
                    break;
                }
            }
        } else {
            Player target = args.length > 1 ? plugin.getServer().getPlayer(args[1]) : null;
            subCommand.execute(sender, target, args, getLabel());
        }


    }

}
