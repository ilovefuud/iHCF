package us.lemin.hcf.combatlog.event;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import us.lemin.hcf.combatlog.LoggerListener;

public class LoggerSpawnEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled;
    private final LoggerListener.CombatLogger combatLogger;

    public LoggerSpawnEvent(LoggerListener.CombatLogger combatLogger) {
        this.combatLogger = combatLogger;
    }

    public Entity getLoggerEntity() {
        return combatLogger.getEntity();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
