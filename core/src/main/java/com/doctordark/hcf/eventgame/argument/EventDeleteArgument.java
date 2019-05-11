package com.doctordark.hcf.eventgame.argument;

import com.doctordark.hcf.HCF;
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
 * A {@link SubCommand} used for deleting an {@link EventFaction}.
 */
public class EventDeleteArgument extends SubCommand {

    private final HCF plugin;

    public EventDeleteArgument(HCF plugin) {
        super("delete", "Deletes an event");
        this.plugin = plugin;
        this.aliases = new String[]{"remove", "del"};
        this.permission = "hcf.command.event.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <eventName>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[1]);

        if (!(faction instanceof EventFaction)) {
            sender.sendMessage(ChatColor.RED + "There is not an event faction named '" + args[1] + "'.");
            return;
        }

        if (plugin.getFactionManager().removeFaction(faction, sender)) {
            sender.sendMessage(ChatColor.YELLOW + "Deleted event faction " + ChatColor.WHITE + faction.getDisplayName(sender) + ChatColor.YELLOW + '.');
        }


    }
}
