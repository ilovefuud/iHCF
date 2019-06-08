package us.lemin.hcf.packetnpcs.kit;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.type.SpawnFaction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class KitNPCManager {

    private final HCF plugin;
    private Map<UUID, EntityVillager> villagerMap = new HashMap<>();
    private Map<UUID, Integer> villagerIdMap = new HashMap<>();


    public KitNPCManager(HCF plugin) {
        this.plugin = plugin;
    }

    public void sendPacket(Player player, boolean remove) {
        if (remove) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(villagerIdMap.get(player.getUniqueId())));
            villagerIdMap.remove(player.getUniqueId());
            villagerMap.remove(player.getUniqueId());
        } else {
            if (villagerIdMap.get(player.getUniqueId()) != null) {
                return;
            }
            if (!(plugin.getFactionManager().getFactionAt(player.getLocation()) instanceof SpawnFaction && player.getLocation().getWorld().getEnvironment() == World.Environment.NORMAL)) {
                return;
            }
            Location kitSelectorLocation = plugin.getConfiguration().getKitSelectorLocation();
            EntityVillager villager = new EntityVillager(((CraftWorld) kitSelectorLocation.getWorld()).getHandle());
            villager.setCustomName(ChatColor.GREEN + "Kit Selector");
            villager.setCustomNameVisible(true);
            villager.setInvisible(false);
            villager.setLocation(kitSelectorLocation.getX(), kitSelectorLocation.getY(), kitSelectorLocation.getZ(), kitSelectorLocation.getPitch(), kitSelectorLocation.getYaw());
            villagerIdMap.put(player.getUniqueId(), villager.getId());
            villagerMap.put(player.getUniqueId(), villager);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(villager));
        }
    }

    public EntityVillager getVillagerByUUID(UUID uuid) {
        return this.villagerMap.get(uuid);
    }

    public int getVillagerIdByUUID(UUID uuid) {
        return this.villagerIdMap.get(uuid);
    }
}
