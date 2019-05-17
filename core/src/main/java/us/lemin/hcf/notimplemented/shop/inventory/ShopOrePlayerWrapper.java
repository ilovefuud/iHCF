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
import us.lemin.hcf.notimplemented.shop.ShopManager;


public class ShopOrePlayerWrapper extends PlayerInventoryWrapper {
    private final HCF plugin;

    public ShopOrePlayerWrapper(HCF plugin) {
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

        ShopManager shopManager = plugin.getShopManager();
        shopManager.getShopOres().forEach((string, shopItem) -> {
            boolean sellable = shopItem.isSellable();
            boolean purchasable = shopItem.isPurchasable();

            ItemBuilder icon = ItemBuilder.from(shopItem.getItemStack().clone());
            icon.name(shopItem.getName());

            if (purchasable) icon.loreLine(CC.YELLOW + "Left click to purchase 1 for " + shopItem.getValue() + ".");
            if (purchasable) icon.loreLine(CC.YELLOW + "Right click to purchase 32 for " + shopItem.getValue() * 32 + ".");
            if (sellable) icon.loreLine(CC.YELLOW + "Left click to sell 1 for $" + (shopItem.getValue() / 2) + " each.");
            if (sellable) icon.loreLine(CC.YELLOW + "Right click to sell 32 for $" + (shopItem.getValue() / 2) + " each.");

            inventoryWrapper.addItem(icon.build(), new PlayerAction((actionPlayer, clickType) -> {
                if (purchasable && clickType.isLeftClick()) {
                    shopItem.tryPurchase(actionPlayer, 1);
                } else if (purchasable && clickType.isRightClick()){
                    shopItem.tryPurchase(actionPlayer, 32);
                } else if (sellable && clickType.isLeftClick()) {
                    shopItem.trySell(player, 1);
                } else if (sellable && clickType.isRightClick()) {
                    shopItem.trySell(player, 32);
                }
            }, false));
        });


    }
}
