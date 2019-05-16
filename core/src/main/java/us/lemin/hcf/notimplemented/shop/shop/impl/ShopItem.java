package us.lemin.hcf.notimplemented.shop.shop.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.message.CC;
import us.lemin.hcf.notimplemented.shop.shop.ShopEntry;

public class ShopItem extends ShopEntry {
    private String name;
    private ItemStack itemStack;

    public ShopItem(ItemStack icon, int cost, boolean sellable, boolean purchasable) {
        super(icon, cost, sellable, purchasable);
        this.name = icon.getItemMeta().getDisplayName();
    }

    public ShopItem(Material material, int cost, boolean sellable, boolean purchasable) {
        this(new ItemStack(material), cost, sellable, purchasable);
    }


    @Override
    public void purchase(Player player) {
        if (canPurchase(player)) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().setItem(player.getInventory().firstEmpty(), itemStack);
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                player.sendMessage(CC.RED + "Item was dropped at your feet because your inventory was full.");
            }
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }
}
