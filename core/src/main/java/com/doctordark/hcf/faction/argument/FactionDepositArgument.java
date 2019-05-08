package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.economy.EconomyManager;
import com.doctordark.hcf.faction.struct.Relation;
import com.doctordark.hcf.faction.type.PlayerFaction;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.misc.JavaUtils;

import java.util.UUID;

public class FactionDepositArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionDepositArgument(HCF plugin) {
        super("deposit", "Deposits money to the faction balance.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <all|amount>";
    }

    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("all");

    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        UUID uuid = player.getUniqueId();
        int playerBalance = plugin.getEconomyManager().getBalance(uuid);

        Integer amount;
        if (args[1].equalsIgnoreCase("all")) {
            amount = playerBalance;
        } else {
            if ((amount = (JavaUtils.tryParseInt(args[1]))) == null) {
                player.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a valid number.");
                return;
            }
        }

        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "Amount must be positive.");
            return;
        }

        if (playerBalance < amount) {
            player.sendMessage(ChatColor.RED + "You need at least " + EconomyManager.ECONOMY_SYMBOL + JavaUtils.format(amount) + " to do this, you only have " +
                    EconomyManager.ECONOMY_SYMBOL + JavaUtils.format(playerBalance) + '.');

            return;
        }

        plugin.getEconomyManager().subtractBalance(uuid, amount);

        playerFaction.setBalance(playerFaction.getBalance() + amount);
        playerFaction.broadcast(Relation.MEMBER.toChatColour() + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + ChatColor.YELLOW + " has deposited " +
                ChatColor.BOLD + EconomyManager.ECONOMY_SYMBOL + JavaUtils.format(amount) + ChatColor.YELLOW + " into the faction balance.");

    }
}
