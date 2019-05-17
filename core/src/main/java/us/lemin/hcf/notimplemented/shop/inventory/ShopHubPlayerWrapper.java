package us.lemin.hcf.notimplemented.shop.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.api.inventoryapi.InventoryWrapper;
import us.lemin.core.api.inventoryapi.PlayerAction;
import us.lemin.core.api.inventoryapi.PlayerInventoryWrapper;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.hcf.HCF;

public class ShopHubPlayerWrapper extends PlayerInventoryWrapper {
    private final HCF plugin;

    public ShopHubPlayerWrapper(HCF plugin) {
        super("Kit", 3);
        this.plugin = plugin;
    }

    @Override
    public void init(Player player, InventoryWrapper inventoryWrapper) {
        format(player, inventoryWrapper);
    }

    @Override
    public void update(Player player, InventoryWrapper inventoryWrapper) {
        format(player, inventoryWrapper);
    }

    private void format(Player player, InventoryWrapper inventoryWrapper) {
        inventoryWrapper.fillBorder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1));

        inventoryWrapper.setItem(2, 2, new ItemBuilder(Material.DIAMOND).name(CC.GOLD + "Ores").build(), new PlayerAction((actionPlayer, clickType) -> {
            actionPlayer.getOpenInventory().close();
            plugin.getInventoryManager().getPlayerWrapper(ShopOrePlayerWrapper.class).open(actionPlayer);
        }));
        inventoryWrapper.setItem(2, 4, new ItemBuilder(Material.MOB_SPAWNER).name(CC.GOLD + "Spawners").build(), new PlayerAction((actionPlayer, clickType) -> {
            actionPlayer.getOpenInventory().close();
            plugin.getInventoryManager().getPlayerWrapper(ShopSpawnerPlayerWrapper.class).open(actionPlayer);
        }));
        inventoryWrapper.setItem(2, 6, new ItemBuilder(Material.SUGAR_CANE).name(CC.GOLD + "Farming").build(), new PlayerAction((actionPlayer, clickType) -> {
            actionPlayer.getOpenInventory().close();
            plugin.getInventoryManager().getPlayerWrapper(ShopFarmPlayerWrapper.class).open(actionPlayer);
        }));
        inventoryWrapper.setItem(2, 8, new ItemBuilder(Material.DIAMOND_HOE).name(CC.GOLD + "Miscellaneous").build(), new PlayerAction((actionPlayer, clickType) -> {
            actionPlayer.getOpenInventory().close();
            plugin.getInventoryManager().getPlayerWrapper(ShopMiscPlayerWrapper.class).open(actionPlayer);
        }));
    }
}
