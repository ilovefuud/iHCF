package com.doctordark.hcf.faction.argument.subclaim;

import com.doctordark.hcf.faction.claim.ClaimHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.Collections;
import java.util.List;

/**
 * Faction subclaim argument used to receive the Subclaim Wand.
 */
public class FactionSubclaimStartArgument extends PlayerSubCommand {

    public FactionSubclaimStartArgument() {
        super("start", "Receive the subclaim wand");
        this.aliases = new String[]{"begin", "claim", "wand"};
    }

    public String getUsage(String label) {
        return '/' + label + " subclaim " + getName();
    }

    @Override
    public void execute(Player sender, Player player1, String[] args, String label) {
        PlayerInventory inventory = sender.getInventory();

        if (inventory.contains(ClaimHandler.SUBCLAIM_WAND)) {
            sender.sendMessage(ChatColor.RED + "You already have a subclaim wand in your inventory.");
            return;
        }

        if (inventory.contains(ClaimHandler.CLAIM_WAND)) {
            sender.sendMessage(ChatColor.RED + "You cannot have a subclaim wand whilst you have a claiming wand in your inventory.");
            return;
        }

        if (!inventory.addItem(ClaimHandler.SUBCLAIM_WAND).isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Your inventory is full.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Subclaim wand added to inventory. Read the item to understand how to create a subclaim.");

    }
}
