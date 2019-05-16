package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.ClaimHandler;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.UUID;

public class FactionClaimArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionClaimArgument(HCF plugin) {
        super("claim", "Claim land in the Wilderness.");
        this.plugin = plugin;
        this.aliases = new String[]{"claimland"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(Player player, Player player1, String[] strings, String label) {
        UUID uuid = player.getUniqueId();

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(uuid);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.isRaidable()) {
            player.sendMessage(ChatColor.RED + "You cannot claim land for your faction while raidable.");
            return;
        }

        PlayerInventory inventory = player.getInventory();

        if (inventory.contains(ClaimHandler.CLAIM_WAND)) {
            player.sendMessage(ChatColor.RED + "You already have a claiming wand in your inventory.");
            return;
        }

        if (inventory.contains(ClaimHandler.SUBCLAIM_WAND)) {
            player.sendMessage(ChatColor.RED + "You cannot have a claiming wand whilst you have a subclaim wand in your inventory.");
            return;
        }

        if (!inventory.addItem(ClaimHandler.CLAIM_WAND).isEmpty()) {
            player.sendMessage(ChatColor.RED + "Your inventory is full.");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Claiming wand added to inventory, read the item to understand how to claim. " +
                ChatColor.BOLD + "Alternatively" + ChatColor.YELLOW + " you can use " + ChatColor.AQUA + ChatColor.BOLD + '/' + "faction" + " claimchunk" + ChatColor.YELLOW + '.');

    }
}
