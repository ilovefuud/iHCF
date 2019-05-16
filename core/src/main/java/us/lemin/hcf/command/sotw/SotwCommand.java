package us.lemin.hcf.command.sotw;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.misc.BukkitUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.command.sotw.impl.SotwEndCommand;
import us.lemin.hcf.command.sotw.impl.SotwStartCommand;

import java.util.*;

public class SotwCommand extends BaseCommand {

    private static final List<String> COMPLETIONS = ImmutableList.of("start", "end");

    private final Map<String, SubCommand> subCommandMap;
    private final HCF plugin;

    public SotwCommand(HCF plugin) {
        super("sotw");
        this.plugin = plugin;

        Map<String, SubCommand> subCommands = new HashMap<>();

        subCommands.put("start", new SotwStartCommand(plugin));
        subCommands.put("end", new SotwEndCommand(plugin));
        subCommands.put("cancel", new SotwEndCommand(plugin));

        subCommandMap = ImmutableMap.copyOf(subCommands);
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return args.length == 1 ? BukkitUtils.getCompletions(args, COMPLETIONS) : Collections.emptyList();

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
