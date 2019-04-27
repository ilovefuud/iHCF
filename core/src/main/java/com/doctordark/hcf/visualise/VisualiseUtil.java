package com.doctordark.hcf.visualise;

import com.doctordark.hcf.HCF;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.io.IOException;
import java.util.Map;

public class VisualiseUtil {

    public static void handleBlockChanges(Player player, Map<Location, MaterialData> input) throws IOException {
        if (input.isEmpty()) {
            return;
        }

        if (input.size() == 1) {
            Map.Entry<Location, MaterialData> entry = input.entrySet().iterator().next();
            MaterialData materialData = entry.getValue();
            player.sendBlockChange(entry.getKey(), materialData.getItemType(), materialData.getData());
            return;
        }

        Table<Chunk, Location, MaterialData> table = HashBasedTable.create();
        for (Map.Entry<Location, MaterialData> entry : input.entrySet()) {
            Location location = entry.getKey();
            if (location.getWorld().isChunkLoaded(((int) location.getX()) >> 4, ((int) location.getZ()) >> 4)) {
                table.row(entry.getKey().getChunk()).put(location, entry.getValue());
            }
        }

        for (Map.Entry<Chunk, Map<Location, MaterialData>> entry : table.rowMap().entrySet()) {
            VisualiseUtil.sendBulk(player, entry.getValue());
        }
    }

    private static void sendBulk(Player player, Map<Location, MaterialData> input) {
        CraftPlayer craftPlayer = (CraftPlayer) player;

        HCF.getPlugin().getServer().getScheduler().runTaskAsynchronously(HCF.getPlugin(), () ->
                input.forEach((key, value) -> {
                    BlockPosition blockPosition = new BlockPosition(key.getBlockX(), key.getBlockY(), key.getBlockZ());
                    IBlockData iBlockData = Block.getByCombinedId(value.getItemTypeId() + (value.getData() << 12));

                    PacketPlayOutBlockChange packetPlayOutBlockChange = new PacketPlayOutBlockChange(craftPlayer.getHandle().getWorld(), new BlockPosition(blockPosition));

                    packetPlayOutBlockChange.block = iBlockData;

                    craftPlayer.getHandle().playerConnection.sendPacket(packetPlayOutBlockChange);
                }));
    }
}
