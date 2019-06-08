package us.lemin.hcf.packetnpcs.kit;

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
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionManager;
import us.lemin.hcf.faction.type.SpawnFaction;
import us.lemin.hcf.packetnpcs.kit.inventory.KitSelectorWrapper;
import us.lemin.spigot.handler.PacketHandler;

@RequiredArgsConstructor
public class KitNPCListener implements Listener, PacketHandler {

    private final HCF plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getKitNPCManager().sendPacket(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        plugin.getKitNPCManager().sendPacket(event.getPlayer(), false);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        FactionManager factionManager = plugin.getFactionManager();
        if (factionManager.getFactionAt(event.getPlayer().getLocation()) instanceof SpawnFaction && event.getPlayer().getLocation().getWorld().getEnvironment() == World.Environment.NORMAL) {
            TaskUtil.runSyncLater(plugin, () -> {
                plugin.getKitNPCManager().sendPacket(event.getPlayer(), false);
            }, 5);
        }
    }


    @Override
    public void handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
        Player player = playerConnection.getPlayer().getPlayer();
        String simpleName = packet.getClass().getSimpleName();

        if (player.getName().equalsIgnoreCase("scramble")) {
            player.sendMessage(simpleName);
        }
        if (simpleName.equalsIgnoreCase("PacketPlayInUseEntity")) {
            PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet;
            if (plugin.getKitNPCManager().getVillagerByUUID(player.getUniqueId()) == null) {
                return;
            }
            if (ProtocolSupportAPI.getProtocolVersion(player).isAfterOrEq(ProtocolVersion.MINECRAFT_1_8)) {
                player.sendMessage(CC.RED + "You may experience bugs when using NPCs on 1.8, please use the Kit Selector sign or switch your version to 1.7.");
            }
            if (plugin.getKitNPCManager().getVillagerIdByUUID(player.getUniqueId()) == packetPlayInUseEntity.getA()) {
                TaskUtil.runSyncLater(plugin, () -> plugin.getInventoryManager().getPlayerWrapper(KitSelectorWrapper.class).open(player), 1);
            }
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        FactionManager factionManager = plugin.getFactionManager();
        if (factionManager.getFactionAt(event.getTo()) instanceof SpawnFaction && event.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
            TaskUtil.runAsync(plugin, () -> {
                if (plugin.getKitNPCManager().getVillagerByUUID(event.getPlayer().getUniqueId()) == null) {
                    plugin.getKitNPCManager().sendPacket(event.getPlayer(), false);
                }
                EntityVillager villager = plugin.getKitNPCManager().getVillagerByUUID(event.getPlayer().getUniqueId());
                Location villagerLocation = new Location(villager.getWorld().getWorld(), villager.locX, villager.locY, villager.locZ);
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
