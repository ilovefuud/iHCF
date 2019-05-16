package us.lemin.hcf.notimplemented.shop.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.CorePlugin;
import us.lemin.core.api.inventoryapi.InventoryWrapper;
import us.lemin.core.api.inventoryapi.PlayerAction;
import us.lemin.core.api.inventoryapi.PlayerInventoryWrapper;
import us.lemin.core.player.CoreProfile;
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
        CoreProfile coreProfile = CorePlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());

        ShopManager shopManager = new ShopManager(plugin);
        shopManager.getShopItems().forEach((string, shopItem) -> {
            inventoryWrapper.addItem(kitItem.build(), new PlayerAction(kit::purchaseKit, true));

        });


    }
}
