package us.lemin.hcf.listener;

import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.lemin.hcf.HCF;
import us.lemin.hcf.tablist.HCFTemplate;

@RequiredArgsConstructor
public class TabListListener implements Listener {

    private final HCF plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getTabHandler().setTabTemplate(event.getPlayer(), new HCFTemplate(plugin));
    }
}
