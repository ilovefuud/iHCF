package us.lemin.hcf.notimplemented.shop.shop;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import us.lemin.hcf.HCF;

@RequiredArgsConstructor
public class ShopListener implements Listener {

    private final HCF plugin;

    @EventHandler
    public void onShopInteract(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity.getType() != EntityType.VILLAGER) {
            return;
        }

        ShopManager shopManager = new ShopManager(plugin);
        if (shopManager.getShopEntityId() == entity.getEntityId()) {
            event.setCancelled(true);
            // open wrapper
        }
    }
}
