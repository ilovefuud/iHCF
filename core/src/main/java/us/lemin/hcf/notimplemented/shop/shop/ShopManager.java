package us.lemin.hcf.notimplemented.shop.shop;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import us.lemin.hcf.HCF;
import us.lemin.hcf.listener.Crowbar;
import us.lemin.hcf.notimplemented.shop.shop.impl.ShopItem;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShopManager {

    private final HCF plugin;
    @Getter
    private int shopEntityId;

    private final Map<String, ShopItem> shopItems = new LinkedHashMap<>();


    public ShopManager(HCF plugin) {
        this.plugin = plugin;

        registerEntity();
        registerEntries(
                new ShopItem(new Crowbar().getItemIfPresent(), 500, false, true)
        );
    }


    private void registerEntries(ShopEntry... shopEntries) {
        for (ShopEntry shopEntry : shopEntries) {
            if (shopEntry.getClass().isAssignableFrom(ShopItem.class)) {
                ShopItem shopItem = (ShopItem) shopEntry;
                shopItems.put(shopItem.getName(), shopItem);
            }
        }
    }

    public Map<String, ShopItem> getShopItems() {
        return shopItems;
    }


    public ShopItem getShopItemByName(String string) {
        return shopItems.get(string);
    }

    private void registerEntity() {
        Location shopLocation = plugin.getConfiguration().getShopLocation().getLocation();

        Entity villager = shopLocation.getWorld().spawnEntity(shopLocation, EntityType.VILLAGER);
        villager.setCustomName(ChatColor.GREEN + "Shop");
        villager.setCustomNameVisible(true);

        this.shopEntityId = villager.getEntityId();
    }

}
