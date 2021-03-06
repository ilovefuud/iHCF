package us.lemin.hcf.scoreboard;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.lemin.hcf.Configuration;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerBoard {


    private final AtomicBoolean removed = new AtomicBoolean(false);
    private final Team members;
    private final Team neutrals;
    private final Team enemies;
    private final Team allies;
    private final Team focus;

    @Getter
    private final Scoreboard scoreboard;

    @Getter
    private final Player player;

    private final HCF plugin;

    public PlayerBoard(HCF plugin, Player player) { ;
        this.plugin = plugin;
        this.player = player;

        Configuration configuration = plugin.getConfiguration();

        this.scoreboard = player.getScoreboard();

        this.members = scoreboard.registerNewTeam("members");
        this.members.setPrefix(configuration.getRelationColourTeammate().toString());
        this.members.setCanSeeFriendlyInvisibles(true);

        this.neutrals = scoreboard.registerNewTeam("neutrals");
        this.neutrals.setPrefix(configuration.getRelationColourNeutral().toString());

        this.enemies = scoreboard.registerNewTeam("enemies");
        this.enemies.setPrefix(configuration.getRelationColourEnemy().toString());

        this.allies = scoreboard.registerNewTeam("allies");
        this.allies.setPrefix(configuration.getRelationColourAlly().toString());

        this.focus = scoreboard.registerNewTeam("focus");
        this.focus.setPrefix(configuration.getRelationColourFocus().toString());

        player.setScoreboard(this.scoreboard);
    }

    /**
     * Removes this {@link PlayerBoard}.
     */
    public void remove() {
        if (!this.removed.getAndSet(true) && this.scoreboard != null) {
            for (Team team : this.scoreboard.getTeams()) {
                team.unregister();
            }

            for (Objective objective : this.scoreboard.getObjectives()) {
                objective.unregister();
            }
        }
    }


    public void addUpdate(Player target) {
        this.addUpdates(Collections.singleton(target));
    }

    public void addUpdates(Iterable<? extends Player> updates) {
        if (this.removed.get()) {
            throw new IllegalStateException("Cannot update whilst board is removed");
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                if (removed.get()) {
                    cancel();
                    return;
                }

                // Lazy load - don't lookup this in every iteration
                PlayerFaction playerFaction = null;
                boolean firstExecute = false;
                //

                for (Player update : updates) {
                    if (player.equals(update)) {
                        members.addEntry(update.getName());
                        ;
                        continue;
                    }

                    if (!firstExecute) {
                        playerFaction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
                        firstExecute = true;
                    }

                    // Lazy loading for performance increase.
                    PlayerFaction targetFaction;
                    if (playerFaction == null || (targetFaction = plugin.getFactionManager().getPlayerFaction(update.getUniqueId())) == null) {
                        neutrals.addEntry(update.getName());
                    } else if (playerFaction == targetFaction) {
                        members.addEntry(update.getName());
                    } else if (playerFaction.getAllied().contains(targetFaction.getUniqueID())) {
                        allies.addEntry(update.getName());
                    } else if (playerFaction.getFocusedName() != null && playerFaction.getFocusedName().equalsIgnoreCase(update.getName())) {
                        focus.addEntry(update.getName());
                    } else if (playerFaction.getEnemied().contains(targetFaction.getUniqueID())) {
                        enemies.addEntry(update.getName());
                    } else {
                        neutrals.addEntry(update.getName());
                    }
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
