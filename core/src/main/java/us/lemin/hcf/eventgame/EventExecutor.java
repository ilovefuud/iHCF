package us.lemin.hcf.eventgame;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.argument.*;
import us.lemin.hcf.timer.type.InvincibilityTimer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class EventExecutor extends BaseCommand {

    private final HCF plugin;
    @Getter
    private final Map<String, SubCommand> subCommandMap;

    //TODO: add help argument

    public EventExecutor(HCF plugin) {
        super("event", Rank.ADMIN);
        Map<String, SubCommand> subCommands = new HashMap<>();

        subCommands.put("addloottable", new EventAddLootTableArgument(plugin));
        subCommands.put("cancel", new EventCancelArgument(plugin));
        subCommands.put("create", new EventCreateArgument(plugin));
        subCommands.put("delete", new EventDeleteArgument(plugin));
        subCommands.put("delloottable", new EventDelLootTableArgument(plugin));
        subCommands.put("rename", new EventRenameArgument(plugin));
        subCommands.put("setarea", new EventSetAreaArgument(plugin));
        subCommands.put("setcapzone", new EventSetCapzoneArgument(plugin));
        subCommands.put("setloot", new EventSetLootArgument(plugin));
        subCommands.put("start", new EventStartArgument(plugin));
        subCommands.put("uptime", new EventUptimeArgument(plugin));
        subCommands.put("help", new EventHelpArgument(this));


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
