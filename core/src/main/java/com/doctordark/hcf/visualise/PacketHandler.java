package com.doctordark.hcf.visualise;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.util.NmsUtils;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class PacketHandler implements us.lemin.spigot.handler.PacketHandler {

    private final HCF plugin;

    @Override
    public void handleReceivedPacket(PlayerConnection playerConnection, Packet packet) {
        Player player = playerConnection.getPlayer().getPlayer();
        String simpleName = packet.getClass().getSimpleName();

        switch (simpleName) {
            case "PacketPlayInBlockPlace":
                PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace) packet;
                int face = packetPlayInBlockPlace.getFace();
                if (face == 255) {
                    return;
                }
                Location clickedBlock = new Location(player.getWorld(), packetPlayInBlockPlace.a().getX(),
                        packetPlayInBlockPlace.a().getY(),
                        packetPlayInBlockPlace.a().getZ());
                if (plugin.getVisualiseHandler().getVisualBlockAt(player, clickedBlock) != null) {
                    Location placedLocation = clickedBlock.clone();
                    switch (face) {
                        case 2:
                            placedLocation.add(0, 0, -1);
                            break;
                        case 3:
                            placedLocation.add(0, 0, 1);
                            break;
                        case 4:
                            placedLocation.add(-1, 0, 0);
                            break;
                        case 5:
                            placedLocation.add(1, 0, 0);
                            break;
                        default:
                            return;
                    }

                    if (plugin.getVisualiseHandler().getVisualBlockAt(player, placedLocation) == null) {
                        player.sendBlockChange(placedLocation, Material.AIR, (byte) 0);
                        NmsUtils.resendHeldItemPacket(player);
                    }
                }
                break;
            case "PacketPlayInBlockDig":
                PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig) packet;
                PacketPlayInBlockDig.EnumPlayerDigType digType = packetPlayInBlockDig.c();
                if (digType == PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK || digType == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK || digType == PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) {
                    int x = packetPlayInBlockDig.a().getX(), y = packetPlayInBlockDig.a().getY(), z = packetPlayInBlockDig.a().getZ();
                    Location location = new Location(player.getWorld(), x, y, z);
                    VisualBlock visualBlock = plugin.getVisualiseHandler().getVisualBlockAt(player, location);
                    if (visualBlock != null) {
                        VisualBlockData data = visualBlock.getBlockData();
                        if (digType == PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                            player.sendBlockChange(location, data.getBlockType(), data.getData());
                        } else { // we check this because Blocks that broke pretty much straight away do not send a FINISHED for some weird reason.
                            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
                            if (player.getGameMode() == GameMode.CREATIVE || entityPlayer.world.getType(new BlockPosition(x, y, z)).getBlock().getDamage(entityPlayer, entityPlayer.world, new BlockPosition( x, y, z)) >= 1.0F) {
                                player.sendBlockChange(location, data.getBlockType(), data.getData());
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void handleSentPacket(PlayerConnection playerConnection, Packet packet) {
        // no op
    }
}
