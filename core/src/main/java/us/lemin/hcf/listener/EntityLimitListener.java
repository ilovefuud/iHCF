package us.lemin.hcf.listener;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.scheduler.BukkitRunnable;
import us.lemin.hcf.HCF;

/**
 * Listener that limits the amount of entities in one chunk.
 */
public class EntityLimitListener implements Listener {

    private static final int MAX_CHUNK_GENERATED_ENTITIES = 25;
    private static final int MAX_NATURAL_CHUNK_ENTITIES = 25;

    private final HCF plugin;

    public EntityLimitListener(HCF plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (plugin.getConfiguration().isKitmap()) {
            event.setCancelled(true);
        }
        if (!plugin.getConfiguration().isHandleEntityLimiting()) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity instanceof Squid
                || entity instanceof Rabbit
                || entity instanceof Guardian
                || entity instanceof Endermite
                || entity instanceof ArmorStand) {
            event.setCancelled(true);
            return;
        }

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) { // allow slimes to always split
            switch (event.getSpawnReason()) {
                case NATURAL:
                    if (event.getLocation().getChunk().getEntities().length > MAX_NATURAL_CHUNK_ENTITIES) {
                        event.setCancelled(true);
                    }
                    break;
                case CHUNK_GEN:
                    if (event.getLocation().getChunk().getEntities().length > MAX_CHUNK_GENERATED_ENTITIES) {
                        event.setCancelled(true);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        if (item != null) {
            new BukkitRunnable() {
                public void run() {
                    item.remove();
                }
            }.runTaskLater(plugin, (20 * 5));
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onItemSpawn(ItemSpawnEvent event) {
        Item item = event.getEntity();
        if (item != null) {
            new BukkitRunnable() {
                public void run() {
                    item.remove();
                }
            }.runTaskLater(plugin, (20 * 15));
        }
    }
}
