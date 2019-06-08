package us.lemin.hcf.timer.type;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.storage.flatfile.Config;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.PlayerTimer;
import us.lemin.hcf.timer.TimerCooldown;
import us.lemin.hcf.util.DurationFormatter;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EnderPearlTimer extends PlayerTimer implements Listener {


    private final HCF plugin;

    public EnderPearlTimer(HCF plugin) {
        super("Enderpearl", TimeUnit.SECONDS.toMillis(15L));
        this.plugin = plugin;
    }

    @Override
    public String getScoreboardPrefix() {
        return plugin.getConfiguration().getScoreboardSidebarTimerPearlPrefix();
    }

    @Override
    public void load(Config config) {
        super.load(config);
    }

    @Override
    public void onDisable(Config config) {
        super.onDisable(config);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        clearCooldown(event.getPlayer(), event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerKick(PlayerKickEvent event) {
        clearCooldown(event.getPlayer(), event.getPlayer().getUniqueId());
    }

    public void refund(Player player) {
        player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
        clearCooldown(player, player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPearl(PlayerInteractEvent event) {
        if (!event.hasItem() || event.getItem().getType() != Material.ENDER_PEARL
                || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }
        final Player player = event.getPlayer();

        long remaining = getRemaining(player);
        if (remaining > 0L) {
            player.sendMessage(ChatColor.RED + "You still have a " + getDisplayName()
                    + ChatColor.RED + " cooldown for another " + ChatColor.BOLD + DurationFormatter.getRemaining(remaining, true, false) + ChatColor.RED + '.');

            event.setCancelled(true);
            return;
        }
        setCooldown(player, player.getUniqueId(), defaultCooldown, true);
    }


    @Override
    public void handleExpiry(@Nullable Player player, UUID playerUUID) {
        clearCooldown(player, playerUUID);
    }

    @Override
    public TimerCooldown clearCooldown(@Nullable Player player, UUID playerUUID) {
        return super.clearCooldown(player, playerUUID);
    }
}
