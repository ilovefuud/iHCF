package us.lemin.hcf.notimplemented.shop;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import us.lemin.hcf.HCF;
import us.lemin.hcf.notimplemented.shop.inventory.ShopHubPlayerWrapper;

@RequiredArgsConstructor
public class ShopListener implements Listener {

    private final HCF plugin;

    @EventHandler
    public void onShopInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity.getType() != EntityType.VILLAGER) {
            return;
        }

        ShopManager shopManager = plugin.getShopManager();
        if (shopManager.getShopEntityId() == entity.getEntityId()) {
            event.setCancelled(true);
            plugin.getInventoryManager().getPlayerWrapper(ShopHubPlayerWrapper.class).open(event.getPlayer());
        }
    }

}
