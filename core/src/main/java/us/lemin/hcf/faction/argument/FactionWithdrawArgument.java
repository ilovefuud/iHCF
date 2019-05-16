package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.economy.EconomyManager;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.UUID;

public class FactionWithdrawArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionWithdrawArgument(HCF plugin) {
        super("withdraw", "Withdraws money from the faction balance.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <all|amount>";
    }

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
        FactionMember factionMember = playerFaction.getMember(uuid);

        if (factionMember.getRole() == Role.MEMBER) {
            player.sendMessage(ChatColor.RED + "You must be a faction officer to withdraw money.");
            return;
        }

        int factionBalance = playerFaction.getBalance();
        Integer amount;

        if (args[1].equalsIgnoreCase("all")) {
            amount = factionBalance;
        } else {
            if ((amount = (JavaUtils.tryParseInt(args[1]))) == null) {
                player.sendMessage(ChatColor.RED + "Error: '" + args[1] + "' is not a valid number.");
                return;
            }
        }

        if (amount <= 0) {
            player.sendMessage(ChatColor.RED + "Amount must be positive.");
            return;
        }

        if (amount > factionBalance) {
            player.sendMessage(ChatColor.RED + "Your faction need at least " + EconomyManager.ECONOMY_SYMBOL +
                    JavaUtils.format(amount) + " to do this, whilst it only has " + EconomyManager.ECONOMY_SYMBOL + JavaUtils.format(factionBalance) + '.');

            return;
        }

        plugin.getEconomyManager().addBalance(uuid, amount);
        playerFaction.setBalance(factionBalance - amount);
        playerFaction.broadcast(plugin.getConfiguration().getRelationColourTeammate() + factionMember.getRole().getAstrix() + player.getName() + ChatColor.YELLOW + " has withdrew " +
                ChatColor.BOLD + EconomyManager.ECONOMY_SYMBOL + JavaUtils.format(amount) + ChatColor.YELLOW + " from the faction balance.");

    }
}
