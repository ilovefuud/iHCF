package us.lemin.hcf.notimplemented.shop.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.misc.InventoryUtils;
import us.lemin.hcf.notimplemented.shop.ShopEntry;

public class ShopItem extends ShopEntry {
    private String name;
    private ItemStack itemStack;

    public ShopItem(ItemStack icon, int cost, boolean sellable, boolean purchasable) {
        super(icon, cost, sellable, purchasable);
        this.name = icon.getItemMeta().getDisplayName();
        this.itemStack = icon;
    }

    public ShopItem(Material material, int cost, boolean sellable, boolean purchasable) {
        this(new ItemStack(material), cost, sellable, purchasable);
    }


    @Override
    public void tryPurchase(Player player, int amount) {
        if (canPurchase(player, amount)) {
            boolean dropped = false;
            int x = 0;
            while (x <= amount) {
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().setItem(player.getInventory().firstEmpty(), itemStack);
                } else {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    dropped = true;
                }
                x++;
            }
            if (dropped) player.sendMessage(CC.RED + "Item was dropped at your feet because your inventory was full.");
        }
    }

    public void trySell(Player player, int amount) {
        ItemStack clone = this.itemStack.clone();
        int actualAmount = InventoryUtils.countAmount(player.getInventory(), clone.getType(), clone.getData().getData());
        if (actualAmount > 32) {
            actualAmount = 32;
        }
        InventoryUtils.removeItem(player.getInventory(), clone.getType(), clone.getData().getData(), amount);
        sell(player, actualAmount);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }
}
