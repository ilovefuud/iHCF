package com.doctordark.hcf.listener;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.tablist.HCFTemplate;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class TabListListener implements Listener {

    private final HCF plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getTabHandler().setTabTemplate(event.getPlayer(), new HCFTemplate(plugin));
    }
}
