package com.doctordark.hcf.combatlog.event;

import com.doctordark.hcf.combatlog.LoggerListener;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LoggerRemovedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final LoggerListener.CombatLogger combatLogger;

    public LoggerRemovedEvent(LoggerListener.CombatLogger combatLogger) {
        this.combatLogger = combatLogger;
    }

    public Entity getLoggerEntity() {
        return combatLogger.getEntity();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
