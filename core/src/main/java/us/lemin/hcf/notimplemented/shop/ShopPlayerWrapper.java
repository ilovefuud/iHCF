package us.lemin.hcf.notimplemented.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.api.inventoryapi.InventoryWrapper;
import us.lemin.core.api.inventoryapi.PlayerAction;
import us.lemin.core.api.inventoryapi.PlayerInventoryWrapper;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.hcf.HCF;


public class ShopPlayerWrapper extends PlayerInventoryWrapper {
    private final HCF plugin;

    public ShopPlayerWrapper(HCF plugin) {
        super("Shop", 6);
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

        ShopManager shopManager = new ShopManager(plugin);
        shopManager.getShopItems().forEach((string, shopItem) -> {
            boolean sellable = shopItem.isSellable();
            boolean purchasable = shopItem.isPurchasable();

            ItemBuilder icon = ItemBuilder.from(shopItem.getItemStack().clone());
            icon.name(shopItem.getName());

            if (purchasable) icon.loreLine(CC.YELLOW + "Left click to purchase for " + shopItem.getValue() + ".");
            if (sellable) icon.loreLine(CC.YELLOW + "Right click to sell 1 for $" + (shopItem.getValue() / 2) + " each.");
            if (sellable) icon.loreLine(CC.YELLOW + "Shift click to sell 32 for $" + (shopItem.getValue() / 2) + " each.");

            inventoryWrapper.addItem(icon.build(), new PlayerAction((actionPlayer, clickType) -> {
                if (purchasable && clickType.isLeftClick()) {
                    shopItem.purchase(actionPlayer);
                } else if (sellable && clickType.isRightClick()) {
                    shopItem.trySell(player, 1);
                } else if (sellable && clickType.isShiftClick()) {
                    shopItem.trySell(player, 32);
                }
            }, false));
        });


    }
}
