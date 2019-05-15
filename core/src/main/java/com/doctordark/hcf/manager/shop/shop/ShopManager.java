package com.doctordark.hcf.manager.shop.shop;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.listener.Crowbar;
import com.doctordark.hcf.manager.shop.shop.impl.ShopItem;
import us.lemin.core.utils.item.ItemBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShopManager {

    private final HCF plugin;

    private final Map<String, ShopItem> shopItems = new LinkedHashMap<>();


    public ShopManager(HCF plugin) {
        this.plugin = plugin;
        registerEntries(
                new ShopItem(ItemBuilder.from(new Crowbar().getItemIfPresent()).lore("Cost: 500").name("Crowbar").durability(1).build(), 500, false)
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

}
