package us.lemin.hcf.eventgame.koth;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.koth.argument.KothHelpArgument;
import us.lemin.hcf.eventgame.koth.argument.KothNextArgument;
import us.lemin.hcf.eventgame.koth.argument.KothScheduleArgument;
import us.lemin.hcf.eventgame.koth.argument.KothSetCapDelayArgument;
import us.lemin.hcf.timer.type.InvincibilityTimer;

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
