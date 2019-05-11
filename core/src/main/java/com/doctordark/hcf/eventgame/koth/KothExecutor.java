package com.doctordark.hcf.eventgame.koth;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.eventgame.argument.*;
import com.doctordark.hcf.eventgame.koth.argument.KothHelpArgument;
import com.doctordark.hcf.eventgame.koth.argument.KothNextArgument;
import com.doctordark.hcf.eventgame.koth.argument.KothScheduleArgument;
import com.doctordark.hcf.eventgame.koth.argument.KothSetCapDelayArgument;
import com.doctordark.hcf.timer.type.InvincibilityTimer;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class KothExecutor extends BaseCommand {

    private final HCF plugin;
    @Getter
    private final Map<String, SubCommand> subCommandMap;

    //TODO: add help argument

    public KothExecutor(HCF plugin) {
        super("koth", Rank.ADMIN);
        Map<String, SubCommand> subCommands = new HashMap<>();

        subCommands.put("help", new KothHelpArgument(this));
        subCommands.put("next", new KothNextArgument(plugin));
        subCommands.put("schedule", new KothScheduleArgument(plugin));
        subCommands.put("setcapdelay", new KothSetCapDelayArgument(plugin));



        subCommandMap = ImmutableMap.copyOf(subCommands);

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
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
