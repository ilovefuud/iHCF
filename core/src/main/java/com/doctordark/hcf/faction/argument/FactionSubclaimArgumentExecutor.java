package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.command.sotw.impl.SotwEndCommand;
import com.doctordark.hcf.command.sotw.impl.SotwStartCommand;
import com.doctordark.hcf.faction.argument.subclaim.*;
import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.commands.SubCommand;

import java.util.*;

public class FactionSubclaimArgumentExecutor extends PlayerSubCommand {

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
    public void execute(Player player, Player ignore, String[] args, String label) {
        String arg = args.length < 2 ? "help" : args[1].toLowerCase();
        SubCommand subCommand = subCommandMap.get(arg);

        if (subCommand == null) {
            for (SubCommand loop : subCommandMap.values()) {
                if (Arrays.stream(loop.getAliases())
                        .anyMatch(alias -> args[1].equalsIgnoreCase(alias))) {
                    Player target = args.length > 2 ? plugin.getServer().getPlayer(args[2]) : null;
                    loop.execute(player, target, args, label);
                    break;
                }
            }
        } else {
            Player target = args.length > 2 ? plugin.getServer().getPlayer(args[2]) : null;
            subCommand.execute(player, target, args, label);
        }
    }
}
