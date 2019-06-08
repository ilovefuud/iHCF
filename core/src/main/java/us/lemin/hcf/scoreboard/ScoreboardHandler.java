package us.lemin.hcf.scoreboard;

import com.google.common.collect.Iterables;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.lemin.core.api.scoreboardapi.ScoreboardApi;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.event.FactionRelationCreateEvent;
import us.lemin.hcf.faction.event.FactionRelationRemoveEvent;
import us.lemin.hcf.faction.event.PlayerJoinedFactionEvent;
import us.lemin.hcf.faction.event.PlayerLeftFactionEvent;

import java.util.*;

public class ScoreboardHandler implements Listener {

    private final Map<UUID, PlayerBoard> playerBoards = new HashMap<>();
    private ScoreboardApi scoreboardApi;
    private final HCF plugin;

    public ScoreboardHandler(HCF plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        if (plugin.getConfiguration().isScoreboardSidebarEnabled()) {
            scoreboardApi = new ScoreboardApi(plugin, new HCFAdapter(plugin), false);
        }

        // Give all online players a scoreboard.
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
        players.forEach(player -> applyBoard(player).addUpdates(players));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Update this player for every other online player.
        for (PlayerBoard board : playerBoards.values()) {
            board.addUpdate(player);
        }

        applyBoard(player).addUpdates(plugin.getServer().getOnlinePlayers());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerBoards.remove(event.getPlayer().getUniqueId()).remove();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerJoinedFaction(PlayerJoinedFactionEvent event) {
        Optional<Player> optional = event.getPlayer();
        if (optional.isPresent()) {
            Player player = optional.get();

            Collection<Player> players = event.getFaction().getOnlinePlayers();
            getPlayerBoard(event.getPlayerUUID()).addUpdates(players);
            for (Player target : players) {
                getPlayerBoard(target.getUniqueId()).addUpdate(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerLeftFaction(PlayerLeftFactionEvent event) {
        Optional<Player> optional = event.getPlayer();
        if (optional.isPresent()) {
            Player player = optional.get();

            Collection<Player> players = event.getFaction().getOnlinePlayers();
            getPlayerBoard(event.getUniqueID()).addUpdates(players);
            for (Player target : players) {
                getPlayerBoard(target.getUniqueId()).addUpdate(player);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionAllyCreate(FactionRelationCreateEvent event) {
        Iterable<Player> updates = Iterables.concat(
                event.getSenderFaction().getOnlinePlayers(),
                event.getTargetFaction().getOnlinePlayers()
        );

        for (PlayerBoard board : playerBoards.values()) {
            board.addUpdates(updates);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionAllyRemove(FactionRelationRemoveEvent event) {
        Iterable<Player> updates = Iterables.concat(
                event.getSenderFaction().getOnlinePlayers(),
                event.getTargetFaction().getOnlinePlayers()
        );

        for (PlayerBoard board : playerBoards.values()) {
            board.addUpdates(updates);
        }
    }

    public PlayerBoard getPlayerBoard(UUID uuid) {
        return playerBoards.get(uuid);
    }

    public PlayerBoard applyBoard(Player player) {
        PlayerBoard board = new PlayerBoard(plugin, player);
        PlayerBoard previous = playerBoards.put(player.getUniqueId(), board);
        if (previous != null) {
            previous.remove();
        }

        return board;
    }

    public void clearBoards() {
        Iterator<PlayerBoard> iterator = playerBoards.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().remove();
            iterator.remove();
        }
    }
}
