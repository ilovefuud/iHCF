package us.lemin.hcf.packetnpcs.shop;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.hcf.HCF;
import us.lemin.hcf.listener.Crowbar;
import us.lemin.hcf.packetnpcs.shop.impl.ShopItem;

import java.util.*;

@Getter
public class ShopNPCManager {

    private final HCF plugin;
    private Map<UUID, EntityVillager> villagerMap = new HashMap<>();
    private Map<UUID, Integer> villagerIdMap = new HashMap<>();

    private static final List<Material> ORES = ImmutableList.of(Material.DIAMOND, Material.IRON_INGOT, Material.GOLD_INGOT,
            Material.COAL, Material.INK_SACK, Material.REDSTONE, Material.EMERALD, Material.COAL_BLOCK, Material.IRON_BLOCK,
            Material.GOLD_BLOCK, Material.DIAMOND_BLOCK, Material.LAPIS_BLOCK, Material.REDSTONE_BLOCK);

    private static final List<Material> FARMING = ImmutableList.of(Material.BLAZE_ROD, Material.SUGAR_CANE, Material.POTATO_ITEM,
            Material.CARROT_ITEM, Material.SPECKLED_MELON, Material.MELON_SEEDS, Material.NETHER_WARTS, Material.SLIME_BALL, Material.FEATHER);


    private final Map<String, ShopItem> shopItems = new LinkedHashMap<>();
    private final Map<String, ShopItem> shopSpawners = new LinkedHashMap<>();
    private final Map<String, ShopItem> shopFarming = new LinkedHashMap<>();
    private final Map<String, ShopItem> shopMisc = new LinkedHashMap<>();
    private final Map<String, ShopItem> shopOres = new LinkedHashMap<>();


    public ShopNPCManager(HCF plugin) {
        this.plugin = plugin;

        registerEntries(
                new ShopItem(new ItemBuilder(Material.MOB_SPAWNER).name(CC.YELLOW + "Zombie Spawner").loreLine(CC.WHITE + "Zombie").build(), 27500, false, true),
                new ShopItem(new ItemBuilder(Material.MOB_SPAWNER).name(CC.YELLOW + "Skeleton Spawner").loreLine(CC.WHITE + "Skeleton").build(), 27500, false, true),
                new ShopItem(new ItemBuilder(Material.MOB_SPAWNER).name(CC.YELLOW + "Spider Spawner").loreLine(CC.WHITE + "Spider").build(), 27500, false, true),
                new ShopItem(new ItemBuilder(Material.MOB_SPAWNER).name(CC.YELLOW + "CaveSpider Spawner").loreLine(CC.WHITE + "CaveSpider").build(), 27500, false, true),
                new ShopItem(Material.COAL, 6, true, false),
                new ShopItem(new ItemBuilder(Material.COAL).durability(1).build(), 6, true, false),
                new ShopItem(Material.IRON_INGOT, 10, true, false),
                new ShopItem(Material.GOLD_INGOT, 12, true, false),
                new ShopItem(Material.REDSTONE, 8, true, false),
                new ShopItem(new ItemBuilder(Material.INK_SACK).durability(4).build(), 14, true, false),
                new ShopItem(Material.DIAMOND, 16, true, false),
                new ShopItem(Material.EMERALD, 18, true, false),
                new ShopItem(Material.BLAZE_ROD, 46, false, true),
                new ShopItem(Material.SUGAR_CANE, 32, false, true),
                new ShopItem(Material.POTATO_ITEM, 32, false, true),
                new ShopItem(Material.CARROT_ITEM, 32, false, true),
                new ShopItem(Material.SPECKLED_MELON, 32, false, true),
                new ShopItem(Material.MELON_SEEDS, 32, false, true),
                new ShopItem(Material.NETHER_STALK, 32, false, true),
                new ShopItem(Material.SLIME_BALL, 32, false, true),
                new ShopItem(Material.FEATHER, 32, false, true),
                new ShopItem(new Crowbar().getItemIfPresent(), 500, false, true),
                new ShopItem(Material.ENDER_PORTAL_FRAME, 5000, false, true),
                new ShopItem(Material.BEACON, 27500, false, true),
                new ShopItem(new ItemBuilder(Material.MONSTER_EGG).durability(92).build(), 5, false, true)
        );
    }


    private void registerEntries(ShopEntry... shopEntries) {
        for (ShopEntry shopEntry : shopEntries) {
            if (shopEntry.getClass().isAssignableFrom(ShopItem.class)) {
                ShopItem shopItem = (ShopItem) shopEntry;
                if (ORES.contains(shopItem.getItemStack().getType())) {
                    shopOres.put(shopItem.getName(), shopItem);
                } else if (shopItem.getItemStack().getType() == Material.MOB_SPAWNER) {
                    shopSpawners.put(shopItem.getName(), shopItem);
                } else if (FARMING.contains(shopItem.getItemStack().getType())) {
                    shopFarming.put(shopItem.getName(), shopItem);
                } else {
                    shopMisc.put(shopItem.getName(), shopItem);
                }
            }
        }

    }


    public void sendPacket(Player player, boolean remove) {
        if (remove) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(villagerIdMap.get(player.getUniqueId())));
            villagerIdMap.remove(player.getUniqueId());
            villagerMap.remove(player.getUniqueId());
        } else {
            Location shopLocation = plugin.getConfiguration().getShopLocation();
            EntityVillager villager = new EntityVillager(((CraftWorld) shopLocation.getWorld()).getHandle());
            villager.setCustomName(ChatColor.GREEN + "Shop");
            villager.setCustomNameVisible(true);
            villager.setInvisible(false);
            villager.setLocation(shopLocation.getX(), shopLocation.getY(), shopLocation.getZ(), shopLocation.getPitch(), shopLocation.getYaw());
            villagerIdMap.put(player.getUniqueId(), villager.getId());
            villagerMap.put(player.getUniqueId(), villager);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(villager));
        }
    }


    public ShopItem getShopItemByName(String string) {
        return shopItems.get(string);
    }

    public ShopItem getFarmItemByName(String string) {
        return shopFarming.get(string);
    }

    public ShopItem getSpawnerItemByName(String string) {
        return shopSpawners.get(string);
    }

    public ShopItem getMiscItemByName(String string) {
        return shopMisc.get(string);
    }

    public ShopItem getOresItemByName(String string) {
        return shopOres.get(string);
    }

    public EntityVillager getVillagerByUUID(UUID uuid) {
        return this.villagerMap.get(uuid);
    }

    public int getVillagerIdByUUID(UUID uuid) {
        return this.villagerIdMap.get(uuid);
    }
}
