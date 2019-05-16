package us.lemin.hcf.eventgame;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.tracker.ConquestTracker;
import us.lemin.hcf.eventgame.tracker.EventTracker;
import us.lemin.hcf.eventgame.tracker.KothTracker;

public enum EventType {

    CONQUEST("Conquest", new ConquestTracker(HCF.getPlugin())),
    CITADEL("Citadel", new KothTracker(HCF.getPlugin())),
    KOTH("KOTH", new KothTracker(HCF.getPlugin()));

    private final EventTracker eventTracker;
    private final String displayName;

    EventType(String displayName, EventTracker eventTracker) {
        this.displayName = displayName;
        this.eventTracker = eventTracker;
    }

    public EventTracker getEventTracker() {
        return eventTracker;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final ImmutableMap<String, EventType> byDisplayName;

    static {
        ImmutableMap.Builder<String, EventType> builder = new ImmutableBiMap.Builder<>();
        for (EventType eventType : values()) {
            builder.put(eventType.displayName.toLowerCase(), eventType);
        }

        byDisplayName = builder.build();
    }

    @Deprecated
    public static EventType getByDisplayName(String name) {
        return byDisplayName.get(name.toLowerCase());
    }
}
