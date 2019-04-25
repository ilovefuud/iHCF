package land.pvp.testplugin;

import land.pvp.sokudotab.tab.TabHandler;
import land.pvp.sokudotab.tab.lines.DefaultTemplate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {
    private static final LobbyTemplate templaye = new LobbyTemplate();
    private static final EloTemplate templaeye = new EloTemplate();
    private TabHandler tabHandler;

    @Override
    public void onEnable() {
        tabHandler = new TabHandler(this, 20);
        tabHandler.setDefaultTabTemplate(new DefaultTemplate());

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        tabHandler.setTabTemplate(event.getPlayer(), new LobbyTemplate());
    }

    @EventHandler
    public void a(PlayerToggleSprintEvent event) {
        tabHandler.setTabTemplate(event.getPlayer(), event.getPlayer().isSprinting() ? templaye : templaeye);
    }
}

