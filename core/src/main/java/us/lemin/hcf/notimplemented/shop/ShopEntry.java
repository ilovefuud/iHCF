package us.lemin.hcf.notimplemented.shop;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.message.CC;
import us.lemin.hcf.HCF;

@Getter
public abstract class ShopEntry {

    private int value;
    private ItemStack icon;
    boolean sellable;
    boolean purchasable;



    public ShopEntry(ItemStack icon, int value, boolean sellable, boolean purchasable) {
        this.value = value;
        this.icon = icon;
        this.sellable = sellable;
        this.purchasable = purchasable;
    }

    public ShopEntry(Material icon, int value, boolean sellable, boolean purchasable){
        this(new ItemStack(icon), value, sellable, purchasable);
    }

    protected abstract void purchase(Player player);

    protected boolean canPurchase(Player player) {
        HCF plugin = HCF.getPlugin();

        int balance  = plugin.getEconomyManager().getBalance(player.getUniqueId());
        if (balance < value) {
            player.sendMessage(CC.RED + "nigga u broke as hell");
            return false;
        }
        plugin.getEconomyManager().subtractBalance(player.getUniqueId(), value);
        return true;
    }

    protected void sell(Player player, int amount) {
        HCF plugin = HCF.getPlugin();

        plugin.getEconomyManager().addBalance(player.getUniqueId(), (value / 2) * amount);
    }

    public int getValue() {
        return value;
    }
}
