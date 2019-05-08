package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionArgument;
import com.doctordark.hcf.faction.argument.subclaim.*;
import com.doctordark.util.command.CommandArgument;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.*;

public class FactionSubclaimArgumentExecutor extends FactionArgument {

    private final List<CommandArgument> arguments = new ArrayList<>(8);

    public FactionSubclaimArgumentExecutor(HCF plugin) {
        super("subclaim", "Shows the subclaim help page.", new String[]{"sub", "subland", "subclaimland"});
        this.arguments.add(new FactionSubclaimAddMemberArgument(plugin));
        this.arguments.add(new FactionSubclaimCreateArgument(plugin));
        this.arguments.add(new FactionSubclaimDeleteArgument(plugin));
        this.arguments.add(new FactionSubclaimDelMemberArgument(plugin));
        this.arguments.add(new FactionSubclaimListArgument(plugin));
        this.arguments.add(new FactionSubclaimMembersArgument(plugin));
        this.arguments.add(new FactionSubclaimRenameArgument(plugin));
        this.arguments.add(new FactionSubclaimStartArgument());
        this.permission = "hcf.command.faction.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.AQUA + "*** Faction Subclaim Help ***");
            for (CommandArgument argument : arguments) {
                String permission = argument.getPermission();
                if (permission == null || player.hasPermission(permission)) {
                    player.sendMessage(ChatColor.GRAY + argument.getUsage(label) + " - " + argument.getDescription() + '.');
                }
            }

            player.sendMessage(ChatColor.GRAY + "/" + label + " map subclaim - Shows the faction subclaim map.");
            return true;
        }

        CommandArgument argument = getArgument(arguments, args[1]);
        String permission = (argument == null) ? null : argument.getPermission();

        if (argument == null || (permission != null && !player.hasPermission(permission))) {
            player.sendMessage(ChatColor.RED + "Faction subclaim sub-command " + args[1] + " not found.");
            return true;
        }

        argument.onCommand(player, command, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            List<String> results = new ArrayList<>();
            for (CommandArgument argument : arguments) {
                String permission = argument.getPermission();
                if (permission == null || sender.hasPermission(permission)) {
                    results.add(argument.getName());
                }
            }

            return results;
        }

        CommandArgument argument = getArgument(arguments, args[1]);
        if (argument != null) {
            String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                return argument.onTabComplete(sender, command, label, args);
            }
        }

        return Collections.emptyList();
    }

    //TODO: Needs moving into better library
    private static CommandArgument getArgument(Collection<CommandArgument> arguments, String id) {
        for (CommandArgument commandArgument : arguments) {
            if (commandArgument.getName().equalsIgnoreCase(id) || Arrays.asList(commandArgument.getAliases()).contains(id)) {
                return commandArgument;
            }
        }

        return null;
    }
}
