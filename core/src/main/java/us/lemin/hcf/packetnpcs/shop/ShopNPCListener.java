package us.lemin.hcf.packetnpcs.shop;

import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.util.Vector;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionManager;
import us.lemin.hcf.faction.type.SpawnFaction;
import us.lemin.hcf.packetnpcs.shop.inventory.ShopHubPlayerWrapper;
import us.lemin.spigot.handler.PacketHandler;

@RequiredArgsConstructor
public class ShopNPCListener implements Listener, PacketHandler {

    private final HCF plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        FactionManager factionManager = plugin.getFactionManager();
        if (factionManager.getFactionAt(event.getPlayer().getLocation()) instanceof SpawnFaction && event.getPlayer().getLocation().getWorld().getEnvironment() == World.Environment.NORMAL) {
            plugin.getShopNPCManager().sendPacket(event.getPlayer(), false);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        FactionManager factionManager = plugin.getFactionManager();
        if (factionManager.getFactionAt(event.getPlayer().getLocation()) instanceof SpawnFaction && event.getPlayer().getLocation().getWorld().getEnvironment() == World.Environment.NORMAL) {
            if (plugin.getShopNPCManager().getVillagerByUUID(event.getPlayer().getUniqueId()) != null) {
                return;
            }
            plugin.getShopNPCManager().sendPacket(event.getPlayer(), false);
        }
    }

    @Override
    public void handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
        Player player = playerConnection.getPlayer().getPlayer();
        String simpleName = packet.getClass().getSimpleName();

        if (simpleName.equalsIgnoreCase("PacketPlayInUseEntity")) {
            PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
            if (plugin.getShopNPCManager().getVillagerByUUID(player.getUniqueId()) == null) {
                return;
            }
            if (plugin.getShopNPCManager().getVillagerIdByUUID(player.getUniqueId()) == packetPlayInUseEntity.getA()) {
                plugin.getInventoryManager().getPlayerWrapper(ShopHubPlayerWrapper.class).open(player);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        FactionManager factionManager = plugin.getFactionManager();
        if (factionManager.getFactionAt(event.getTo()) instanceof SpawnFaction) {
            TaskUtil.runAsync(plugin, ()-> {
                if (plugin.getShopNPCManager().getVillagerByUUID(event.getPlayer().getUniqueId()) == null) {
                    plugin.getShopNPCManager().sendPacket(event.getPlayer(), false);
                }
                EntityVillager villager = plugin.getShopNPCManager().getVillagerByUUID(event.getPlayer().getUniqueId());
                Location villagerLocation = new Location(villager.getWorld().getWorld(), villager.locX, villager.locY, villager.locZ);
                if (villagerLocation.getWorld().getEnvironment() != World.Environment.NORMAL) {
                    return;
                }
                if (villagerLocation.distance(event.getPlayer().getLocation()) >= 20) {
                    return;
                }
                Vector dirBetweenLocations = villagerLocation.toVector().subtract(event.getPlayer().getLocation().toVector()).multiply(-1);
                double yaw = getLookAtYaw(dirBetweenLocations);

                PacketPlayOutEntityHeadRotation rotation = new PacketPlayOutEntityHeadRotation(villager, (byte) ((yaw * 256.0F) / 360.0F));
                ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(rotation);
            });
        }
    }

    public static float getLookAtYaw(Vector motion) {
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0;
        // Set yaw
        if (dx != 0) {
            // Set yaw start value based on dx
            if (dx < 0) {
                yaw = 1.5 * Math.PI;
            } else {
                yaw = 0.5 * Math.PI;
            }
            yaw -= Math.atan(dz / dx);
        } else if (dz < 0) {
            yaw = Math.PI;
        }
        return (float) (-yaw * 180 / Math.PI);
    }

    @Override
    public void handleSentPacket(PlayerConnection playerConnection, Packet packet) {
        // No Op
    }

}
