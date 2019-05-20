package us.lemin.hcf.notimplemented.shop;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionManager;
import us.lemin.hcf.faction.type.SpawnFaction;
import us.lemin.hcf.notimplemented.shop.inventory.ShopHubPlayerWrapper;
import us.lemin.spigot.handler.MovementHandler;
import us.lemin.spigot.handler.PacketHandler;

@RequiredArgsConstructor
public class ShopListener implements Listener, PacketHandler, MovementHandler {

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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FactionManager factionManager = plugin.getFactionManager();
        if (factionManager.getFactionAt(event.getPlayer().getLocation()) instanceof SpawnFaction) {
            plugin.getShopManager().sendPacket(event.getPlayer(), false);
        }
    }

    @Override
    public void handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
        Player player = playerConnection.getPlayer().getPlayer();
        String simpleName = packet.getClass().getSimpleName();

        if (simpleName.equalsIgnoreCase("PacketPlayInUseEntity")) {
            PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
            if (plugin.getShopManager().getShopEntityId() == packetPlayInUseEntity.getA()) {
                plugin.getInventoryManager().getPlayerWrapper(ShopHubPlayerWrapper.class).open(player);
            }
        }
    }


    @Override
    public void handleUpdateLocation(Player player, Location to, Location from, PacketPlayInFlying packetPlayInFlying) {
        EntityVillager villager = plugin.getShopManager().getVillager();
        Location location = new Location(villager.getWorld().getWorld(), villager.locX, villager.locY, villager.locZ, villager.yaw, villager.pitch);
        Vector vector = location.toVector().subtract(player.getLocation().toVector());
        Location newVectorLocation = vector.toLocation(location.getWorld());
        villager.setPositionRotation(newVectorLocation.getX(), newVectorLocation.getY(), newVectorLocation.getZ(), newVectorLocation.getYaw(), newVectorLocation.getPitch());
    }

    @Override
    public void handleSentPacket(PlayerConnection playerConnection, Packet packet) {
        // No Op
    }


    @Override
    public void handleUpdateRotation(Player player, Location location, Location location1, PacketPlayInFlying packetPlayInFlying) {
        // No Op
    }
}
