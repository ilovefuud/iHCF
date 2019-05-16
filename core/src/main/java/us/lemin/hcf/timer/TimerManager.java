package us.lemin.hcf.timer;

import lombok.Data;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import us.lemin.core.storage.flatfile.Config;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.EventTimer;
import us.lemin.hcf.timer.type.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
public class TimerManager implements Listener {

    @Getter
    private final CombatTimer combatTimer;

    @Getter
    private final LogoutTimer logoutTimer;

    @Getter
    private final EnderPearlTimer enderPearlTimer;

    @Getter
    private final EventTimer eventTimer;

    @Getter
    private final GappleTimer gappleTimer;

    @Getter
    private InvincibilityTimer invincibilityTimer;

    @Getter
    private final PvpClassWarmupTimer pvpClassWarmupTimer;

    @Getter
    private final StuckTimer stuckTimer;

    @Getter
    private final TeleportTimer teleportTimer;

    @Getter
    private final Set<Timer> timers = new LinkedHashSet<>();

    private final JavaPlugin plugin;
    private Config config;

    public TimerManager(HCF plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        registerTimer(enderPearlTimer = new EnderPearlTimer(plugin));
        registerTimer(logoutTimer = new LogoutTimer(plugin));
        registerTimer(gappleTimer = new GappleTimer(plugin));
        registerTimer(stuckTimer = new StuckTimer(plugin));
        if (!plugin.getConfiguration().isKitmap()) {
            registerTimer(invincibilityTimer = new InvincibilityTimer(plugin));
        }
        registerTimer(combatTimer = new CombatTimer(plugin));
        registerTimer(teleportTimer = new TeleportTimer(plugin));
        registerTimer(eventTimer = new EventTimer(plugin));
        registerTimer(pvpClassWarmupTimer = new PvpClassWarmupTimer(plugin));
        reloadTimerData();
    }

    public void registerTimer(Timer timer) {
        timers.add(timer);
        if (timer instanceof Listener) {
            plugin.getServer().getPluginManager().registerEvents((Listener) timer, plugin);
        }
    }

    public void unregisterTimer(Timer timer) {
        timers.remove(timer);
    }

    /**
     * Reloads the {@link Timer} data from storage.
     */
    public void reloadTimerData() {
        config = new Config(plugin, "timers");
        for (Timer timer : timers) {
            timer.load(config);
        }
    }

    /**
     * Saves the {@link Timer} data to storage.
     */
    public void saveTimerData() {
        for (Timer timer : timers) {
            timer.onDisable(config);
        }

        config.save();
    }
}
