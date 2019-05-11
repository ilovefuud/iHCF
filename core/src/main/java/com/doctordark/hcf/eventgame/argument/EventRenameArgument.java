package com.doctordark.hcf.eventgame.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.eventgame.faction.CapturableFaction;
import com.doctordark.hcf.eventgame.faction.EventFaction;
import com.doctordark.hcf.faction.type.Faction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An {@link SubCommand} used for renaming an {@link CapturableFaction}.
 */
public class EventRenameArgument extends SubCommand {

    private final HCF plugin;

    public EventRenameArgument(HCF plugin) {
        super("rename", "Renames an event");
        this.plugin = plugin;
        this.permission = "hcf.command.event.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <oldName> <newName>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[2]);

        if (faction != null) {
            sender.sendMessage(ChatColor.RED + "There is already a faction named " + args[2] + '.');
            return;
        }

        faction = plugin.getFactionManager().getFaction(args[1]);

        if (!(faction instanceof EventFaction)) {
            sender.sendMessage(ChatColor.RED + "There is not an event faction named '" + args[1] + "'.");
            return;
        }

        String oldName = faction.getName();
        faction.setName(args[2], sender);

        sender.sendMessage(ChatColor.YELLOW + "Renamed event " + ChatColor.WHITE + oldName + ChatColor.YELLOW + " to " + ChatColor.WHITE + faction.getName() + ChatColor.YELLOW + '.');
        return;
    }
}
