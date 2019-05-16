package us.lemin.hcf.eventgame.argument;

import com.google.common.primitives.Ints;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.EventType;
import us.lemin.hcf.eventgame.crate.EventKey;

import java.util.List;

/**
 * An {@link PlayerSubCommand} used for setting the loot of an {@link EventKey}.
 */
public class EventSetLootArgument extends PlayerSubCommand {

    private final HCF plugin;

    public EventSetLootArgument(HCF plugin) {
        super("setloottable", "Sets the loot table of an event key at a specific slot");
        this.plugin = plugin;
        this.aliases = new String[]{"setloot"};
        this.permission = "hcf.command.event.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <eventType> <inventoryNumber>";
    }


    @Override
    public void execute(Player sender, Player player1, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        EventType eventType = EventType.getByDisplayName(args[1]);

        if (eventType == null) {
            sender.sendMessage(ChatColor.RED + "There is not an event type named " + args[1] + '.');
            return;
        }

        Integer index = Ints.tryParse(args[2]);

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
            sender.sendMessage(ChatColor.RED + "There are only " + size + " possible loot inventories for " + eventType.getDisplayName() + ChatColor.RED +
                    ". Use " + ChatColor.YELLOW + '/' + label + " addloottable " + eventType.name() + ChatColor.RED + " to add another.");

            return;
        }

        sender.openInventory(inventories.get(--index));
    }
}
