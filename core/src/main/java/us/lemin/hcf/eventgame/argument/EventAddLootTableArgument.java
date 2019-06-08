package us.lemin.hcf.eventgame.argument;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.misc.InventoryUtils;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.EventType;
import us.lemin.hcf.eventgame.crate.EventKey;

import java.util.Collection;

/**
 * A {@link SubCommand} used to add a loot table for an {@link EventType}.
 */
public class EventAddLootTableArgument extends SubCommand {

    private final HCF plugin;

    public EventAddLootTableArgument(HCF plugin) {
        super("addloottable", "Adds another loot table for an event type");
        this.plugin = plugin;
        this.permission = "hcf.command.event.argument." + getName();
        this.playerOnly = true;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <eventType> [size (multiple of 9)]";
    }

    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        EventType eventType = EventType.getByDisplayName(args[1]);

        if (eventType == null) {
            sender.sendMessage(ChatColor.RED + "There is not an event type named " + args[1] + '.');
            return;
        }

        Integer size = JavaUtils.tryParseInt(args[2]);
        if (size == null) {
            size = InventoryUtils.MAXIMUM_SINGLE_CHEST_SIZE;
        } else if (size % InventoryUtils.DEFAULT_INVENTORY_WIDTH != 0) {
            sender.sendMessage(ChatColor.RED + "Inventory size must be a multiple of " + InventoryUtils.DEFAULT_INVENTORY_WIDTH + '.');
            return;
        } else if (size < InventoryUtils.MINIMUM_INVENTORY_SIZE) {
            sender.sendMessage(ChatColor.RED + "Inventory size must be at least " + InventoryUtils.MINIMUM_INVENTORY_SIZE + '.');
            return;
        } else if (size > InventoryUtils.MAXIMUM_DOUBLE_CHEST_SIZE) {
            sender.sendMessage(ChatColor.RED + "Inventory size must be at most " + InventoryUtils.MAXIMUM_DOUBLE_CHEST_SIZE + '.');
            return;
        }

        EventKey eventKey = plugin.getKeyManager().getEventKey();
        Collection<Inventory> inventories = eventKey.getInventories(eventType);
        int newAmount = inventories.size() + 1;
        inventories.add(Bukkit.createInventory(null, size, eventType.getDisplayName() + " Loot " + newAmount));
        sender.sendMessage(ChatColor.YELLOW + "Created a new key inventory of size " + size + " for event " + eventType.getDisplayName() + ". There are now " + newAmount + " inventories.");

    }
}
