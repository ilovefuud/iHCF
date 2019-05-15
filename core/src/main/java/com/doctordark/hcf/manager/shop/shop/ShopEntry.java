package com.doctordark.hcf.manager.shop.shop;

import com.doctordark.hcf.HCF;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.message.CC;

@Getter
public abstract class ShopEntry {

    private int cost;
    private ItemStack icon;
    boolean sellable;



    public ShopEntry(ItemStack icon, int cost, boolean sellable) {
        this.cost = cost;
        this.icon = icon;
        this.sellable = sellable;
    }

    public ShopEntry(Material icon, int cost, boolean sellable){
        this(new ItemStack(icon), cost, sellable);
    }

    protected abstract void purchase(Player player);

    protected boolean canPurchase(Player player) {
        HCF plugin = HCF.getPlugin();

        int balance  = plugin.getEconomyManager().getBalance(player.getUniqueId());
        if (balance < cost) {
            player.sendMessage(CC.RED + "nigga u broke as hell");
            return false;
        }
        return true;
    }

    public int getCost() {
        return cost;
    }
}
