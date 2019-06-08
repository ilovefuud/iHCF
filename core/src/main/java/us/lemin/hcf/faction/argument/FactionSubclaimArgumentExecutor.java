package us.lemin.hcf.faction.argument;

import com.google.common.collect.ImmutableMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.argument.subclaim.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FactionSubclaimArgumentExecutor extends SubCommand {

    private final HCF plugin;
    private final ImmutableMap<String, SubCommand> subCommandMap;


    public FactionSubclaimArgumentExecutor(HCF plugin) {
        super("subclaim", "Shows the subclaim help page.");
        this.aliases =  new String[]{"sub", "subland", "subclaimland"};
        this.permission = "hcf.command.faction.argument." + getName();
        this.plugin = plugin;
        Map<String, SubCommand> subCommands = new HashMap<>();

        subCommands.put("addmember", new FactionSubclaimAddMemberArgument(plugin));
        subCommands.put("create", new FactionSubclaimCreateArgument(plugin));
        subCommands.put("delete", new FactionSubclaimDeleteArgument(plugin));
        subCommands.put("delmember", new FactionSubclaimDelMemberArgument(plugin));
        subCommands.put("list", new FactionSubclaimListArgument(plugin));
        subCommands.put("members", new FactionSubclaimMembersArgument(plugin));
        subCommands.put("rename", new FactionSubclaimRenameArgument(plugin));
        subCommands.put("start", new FactionSubclaimStartArgument());


        subCommandMap = ImmutableMap.copyOf(subCommands);

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        String arg = args.length < 1 ? "help" : args[0].toLowerCase();
        SubCommand subCommand = subCommandMap.get(arg);

        if (subCommand == null) {
            for (SubCommand loop : subCommandMap.values()) {
                if (loop.getAliases() == null) continue;
                if (Arrays.stream(loop.getAliases())
                        .anyMatch(arg::equalsIgnoreCase)) {
                    loop.verify(sender, target, args, label);
                    break;
                }
            }
        } else {
            subCommand.verify(sender, target, args, label);
        }
    }
}
