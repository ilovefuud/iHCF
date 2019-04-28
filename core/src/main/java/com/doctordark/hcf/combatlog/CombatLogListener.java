package com.doctordark.hcf.combatlog;

//import com.connorl.istaff.ISDataBaseManager;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.combatlog.event.LoggerRemovedEvent;
import com.doctordark.hcf.combatlog.type.LoggerEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.*;

//import com.gmail.xd.zwander.istaff.data.PlayerHackerMode;

/**
 * Listener that prevents {@link Player}s from combat-logging.
 */
public class CombatLogListener implements Listener {


    private final Set<UUID> safelyDisconnected = new HashSet<>();
    private final Map<UUID, LoggerEntity> loggers = new HashMap<>();
    // private boolean containsStaffPlugin;

    private final HCF plugin;

    public CombatLogListener(HCF plugin) {
        this.plugin = plugin;
        // containsStaffPlugin = plugin.getServer().getPluginManager().getPlugin("iStaff") != null;
    }

    /**
     * Disconnects a {@link Player} without a {@link LoggerEntity} spawning.
     *
     * @param player the {@link Player} to disconnect
     * @param reason the reason for disconnecting
     */
    public void safelyDisconnect(Player player, String reason) {
        if (safelyDisconnected.add(player.getUniqueId())) {
            player.kickPlayer(reason);
        }
    }

    /**
     * Removes all the {@link LoggerEntity} instances from the server.
     */
    public void removeCombatLoggers() {
        Iterator<LoggerEntity> iterator = loggers.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().destroy();
            iterator.remove();
        }

        safelyDisconnected.clear();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLoggerRemoved(LoggerRemovedEvent event) {
        loggers.remove(event.getLoggerEntity().getUniqueID());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
        LoggerEntity currentLogger = loggers.remove(event.getPlayer().getUniqueId());
        if (currentLogger != null) {
            currentLogger.destroy();
        }
    }


}
