package us.lemin.hcf.faction.argument.subclaim;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.faction.claim.ClaimHandler;

/**
 * Faction subclaim argument used to receive the Subclaim Wand.
 */
public class FactionSubclaimStartArgument extends SubCommand {

    public FactionSubclaimStartArgument() {
        super("start", "Receive the subclaim wand");
        this.aliases = new String[]{"begin", "claim", "wand"};
        this.playerOnly = true;
    }

    public String getUsage(String label) {
        return '/' + label + " subclaim " + getName();
    }

    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        PlayerInventory inventory = player.getInventory();

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
