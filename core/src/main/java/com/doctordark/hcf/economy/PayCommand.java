package com.doctordark.hcf.economy;

import com.doctordark.hcf.HCF;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.misc.JavaUtils;

import java.util.Collections;
import java.util.List;

/**
 * Command used to pay other {@link Player}s some money.
 */
public class PayCommand extends PlayerCommand {

    private final HCF plugin;

    public PayCommand(HCF plugin) {
        super("pay");
        this.plugin = plugin;
    }


    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /" + getLabel() + " <playerName> <amount>");
            return;
        }

        Integer amount = JavaUtils.tryParseInt(args[1]);

        if (amount == null) {
            player.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a valid number.");
            return;
        }

        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "You must send money in positive quantities.");
            return;
        }

        // Calculate the senders balance here.

        int senderBalance = plugin.getEconomyManager().getBalance(player.getUniqueId());

        if (senderBalance < amount) {
            player.sendMessage(ChatColor.RED + "You tried to pay " + EconomyManager.ECONOMY_SYMBOL + amount + ", but you only have " +
                    EconomyManager.ECONOMY_SYMBOL + senderBalance + " in your bank account.");

            return;
        }

        Player target = plugin.getServer().getPlayer(args[0]);

        if (player.equals(target)) {
            player.sendMessage(ChatColor.RED + "You cannot send money to yourself.");
            return;
        }

        if (target == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }


        // Make the money transactions.
        plugin.getEconomyManager().subtractBalance(player.getUniqueId(), amount);
        plugin.getEconomyManager().addBalance(target.getUniqueId(), amount);

        target.sendMessage(ChatColor.YELLOW + player.getName() + " has sent you " + ChatColor.GOLD + EconomyManager.ECONOMY_SYMBOL + amount + ChatColor.YELLOW + '.');
        player.sendMessage(ChatColor.YELLOW + "You have sent " + ChatColor.GOLD + EconomyManager.ECONOMY_SYMBOL + amount + ChatColor.YELLOW + " to " + target.getName() + '.');
        return;
    }
}
