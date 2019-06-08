package us.lemin.hcf.listener;

import com.google.common.cache.CacheBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import us.lemin.hcf.HCF;
import us.lemin.hcf.packetnpcs.kit.inventory.KitSelectorWrapper;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class KitMapListener implements Listener {
    private static final ChatColor[] COLOURS = {ChatColor.AQUA, ChatColor.YELLOW, ChatColor.GREEN};

    private final HCF plugin;
    private final ConcurrentMap lastClicks = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).concurrencyLevel(1).build().asMap();

    public KitMapListener(HCF plugin) {
        this.plugin = plugin;
    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onSignClick(PlayerInteractEvent e) {
        if (e.useInteractedBlock() == Event.Result.ALLOW) {
            Block block = e.getClickedBlock();
            if (block.getState() instanceof Sign) {
                Sign sign = (Sign) block.getState();
                if (sign.getLine(1).equalsIgnoreCase("kit selector") && plugin.getFactionManager().getFactionAt(sign.getBlock()).isSafezone()) {
                    plugin.getInventoryManager().getPlayerWrapper(KitSelectorWrapper.class).open(e.getPlayer());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock().getType() == Material.ENDER_CHEST && plugin.getFactionManager().getFactionAt(event.getClickedBlock()).isSafezone()) {
            event.setCancelled(false);
        }
    }

}
