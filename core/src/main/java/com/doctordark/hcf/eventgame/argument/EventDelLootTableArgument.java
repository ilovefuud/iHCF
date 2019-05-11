package com.doctordark.hcf.eventgame.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.eventgame.EventType;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.misc.JavaUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An {@link PlayerSubCommand} used to delete a loot table for an {@link EventType}.
 */
public class EventDelLootTableArgument extends PlayerSubCommand {

    private final HCF plugin;

    public EventDelLootTableArgument(HCF plugin) {
        super("delloottable", "Deletes a loot table at a specific slot for a type");
        this.plugin = plugin;
        this.permission = "hcf.command.event.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <eventType> [size (multiple of 9)]";
    }


    @Override
    public void execute(Player sender, Player player1, String[] args, String label) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        EventType eventType = EventType.getByDisplayName(args[1]);

        if (eventType == null) {
            sender.sendMessage(ChatColor.RED + "There is not an event type named " + args[1] + '.');
            return;
        }

        Integer index = JavaUtils.tryParseInt(args[2]);

        if (index == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
            return;
        }

        List<Inventory> inventories = plugin.getKeyManager().getEventKey().getInventories(eventType);
        int size = inventories.size();

        if (index < 1) {
            sender.sendMessage(ChatColor.RED + "You cannot edit an inventory less than 1.");
            return;
        }

        if (index > size) {
            sender.sendMessage(ChatColor.RED + "There are only " + size + " possible loot inventories for " + eventType.getDisplayName() + ChatColor.RED + '.');
            return;
        }

        int removedIndex = --index;
        inventories.remove(removedIndex);
        sender.sendMessage(ChatColor.YELLOW + "Removed inventory for " + eventType.getDisplayName() + " at index " + removedIndex + '.');
    }
}
