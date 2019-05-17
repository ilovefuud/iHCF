package us.lemin.hcf.notimplemented.shop;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.message.CC;
import us.lemin.hcf.HCF;

@Getter
@Setter
public abstract class ShopEntry {

    private int value;
    private ItemStack icon;
    boolean sellable;
    boolean purchasable;
    String description;



    public ShopEntry(ItemStack icon, int value, boolean sellable, boolean purchasable) {
        this.value = value;
        this.icon = icon;
        this.sellable = sellable;
        this.purchasable = purchasable;
    }

    public ShopEntry(Material icon, int value, boolean sellable, boolean purchasable){
        this(new ItemStack(icon), value, sellable, purchasable);
    }

    protected abstract void tryPurchase(Player player, int amount);

    protected boolean canPurchase(Player player, int amount) {
        HCF plugin = HCF.getPlugin();

        int balance  = plugin.getEconomyManager().getBalance(player.getUniqueId());
        if (balance < value * amount) {
            player.sendMessage(CC.RED + "You can't afford that.");
            return false;
        }
        plugin.getEconomyManager().subtractBalance(player.getUniqueId(), value * amount);
        return true;
    }

    protected void sell(Player player, int amount) {
        HCF plugin = HCF.getPlugin();

        plugin.getEconomyManager().addBalance(player.getUniqueId(), (value / 2) * amount);
    }

    public int getValue() {
        return value;
    }

    public ShopEntry setDescription(String description) {
        this.description = description;
        return this;
    }
}
