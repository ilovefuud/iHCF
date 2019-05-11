package com.doctordark.hcf.combatlog.event;

import com.doctordark.hcf.combatlog.LoggerListener;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoggerDeathEvent extends Event implements Cancellable {

    private boolean cancelled;

    private static final HandlerList handlers = new HandlerList();

    private final LoggerListener.CombatLogger loggerEntity;

    public LoggerDeathEvent(LoggerListener.CombatLogger loggerEntity) {
        this.loggerEntity = loggerEntity;
    }

    public LoggerListener.CombatLogger getLoggerEntity() {
        return loggerEntity;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
