package com.doctordark.hcf.scoreboard;

import com.doctordark.hcf.Configuration;
import com.doctordark.hcf.DateTimeFormats;
import com.doctordark.hcf.HCF;
import com.doctordark.hcf.eventgame.EventTimer;
import com.doctordark.hcf.eventgame.eotw.EotwHandler;
import com.doctordark.hcf.eventgame.faction.ConquestFaction;
import com.doctordark.hcf.eventgame.faction.EventFaction;
import com.doctordark.hcf.eventgame.faction.KothFaction;
import com.doctordark.hcf.eventgame.tracker.ConquestTracker;
import com.doctordark.hcf.faction.type.PlayerFaction;
import com.doctordark.hcf.pvpclass.PvpClass;
import com.doctordark.hcf.pvpclass.archer.ArcherClass;
import com.doctordark.hcf.pvpclass.archer.ArcherMark;
import com.doctordark.hcf.pvpclass.bard.BardClass;
import com.doctordark.hcf.pvpclass.type.MinerClass;
import com.doctordark.hcf.sotw.SotwTimer;
import com.doctordark.hcf.timer.PlayerTimer;
import com.doctordark.hcf.timer.Timer;
import com.doctordark.hcf.user.FactionUser;
import com.doctordark.hcf.util.DurationFormatter;
import com.google.common.collect.Ordering;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import us.lemin.core.api.scoreboardapi.ScoreboardUpdateEvent;
import us.lemin.core.api.scoreboardapi.api.ScoreboardAdapter;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.player.PlayerUtil;

import java.text.DecimalFormat;
import java.util.*;

@RequiredArgsConstructor
public class HCFAdapter implements ScoreboardAdapter {

    public static final ThreadLocal<DecimalFormat> CONQUEST_FORMATTER = ThreadLocal.withInitial(() -> new DecimalFormat("00.0"));

    private static final Comparator<Map.Entry<UUID, ArcherMark>> ARCHER_MARK_COMPARATOR = Comparator.comparing(Map.Entry::getValue);


    private final HCF plugin;


    @Override
    public void onUpdate(ScoreboardUpdateEvent event) {
        Player player = event.getPlayer();

        FactionUser user = plugin.getUserManager().getUser(player.getUniqueId());

        if (!user.isShowScoreboard()) {
            return;
        }

        event.setTitle(plugin.getConfiguration().getScoreboardTablistTitle());
        event.setSeparator(CC.BOARD_SEPARATOR);

        Configuration config = plugin.getConfiguration();

        if (config.isKitmap()) {
            event.addLine(config.getScoreboardSidebarKitmapKills().replace("%kills%", String.valueOf(user.getKills())));
            event.addLine(config.getScoreboardSidebarKitmapDeaths().replace("%deaths%", String.valueOf(user.getDeaths())));
            event.addLine(config.getScoreboardSidebarKitmapKillstreak().replace("%killstreak%", String.valueOf(user.getKillstreak())));
        } else {
            EotwHandler.EotwRunnable eotwRunnable = plugin.getEotwHandler().getRunnable();
            if (eotwRunnable != null) {
                long remaining = eotwRunnable.getMillisUntilStarting();
                if (remaining > 0L) {
                    event.addLine(config.getScoreboardSidebarEotwCountdown()
                            .replace("%remaining%", DurationFormatter.getRemaining(remaining, true) ));
                } else if ((remaining = eotwRunnable.getMillisUntilCappable()) > 0L) {
                    event.addLine(config.getScoreboardSidebarEotwCappable()
                            .replace("%remaining%", DurationFormatter.getRemaining(remaining, true) ));
                }
            }

            SotwTimer.SotwRunnable sotwRunnable = plugin.getSotwTimer().getSotwRunnable();
            if (sotwRunnable != null) {
                event.addLine(config.getScoreboardSidebarSotw()
                        .replace("%remaining%", DurationFormatter.getRemaining(sotwRunnable.getRemaining(), true)));
            }

            EventTimer eventTimer = plugin.getTimerManager().getEventTimer();
            List<String> conquestLines = null;
            EventFaction eventFaction = eventTimer.getEventFaction();
            if (eventFaction instanceof KothFaction) {
                event.addLine(config.getScoreboardSidebarActiveKoth()
                        .replace("%remaining%", DurationFormatter.getRemaining(eventTimer.getRemaining(), true)));
            } else if (eventFaction instanceof ConquestFaction) {
                ConquestFaction conquestFaction = (ConquestFaction) eventFaction;

                conquestLines = new ArrayList<>();

                conquestLines.add(config.getScoreboardSidebarConquestActiveConquest()
                        .replace("%conquestName%", conquestFaction.getName()));
                conquestLines.add(config.getScoreboardSidebarConquestLineOne()
                        .replace("%redRemaining%", conquestFaction.getRed().getScoreboardRemaining())
                        .replace("%yellowRemaining%", conquestFaction.getYellow().getScoreboardRemaining()));
                conquestLines.add(config.getScoreboardSidebarConquestLineTwo()
                        .replace("%greenRemaining%", conquestFaction.getGreen().getScoreboardRemaining())
                        .replace("%blueRemaining%", conquestFaction.getBlue().getScoreboardRemaining()));

                // Show the top 3 factions next.
                ConquestTracker conquestTracker = (ConquestTracker) conquestFaction.getEventType().getEventTracker();
                int count = 0;
                for (Map.Entry<PlayerFaction, Integer> entry : conquestTracker.getFactionPointsMap().entrySet()) {
                    String factionName = entry.getKey().getName();
                    event.addLine(config.getScoreboardSidebarConquestTopThree().replace("%factionName%", factionName).replace("%score%", String.valueOf(entry.getValue())));
                    if (++count == 3) break;
                }
            }

            // Show the current PVP Class statistics of the player.
            PvpClass pvpClass = plugin.getPvpClassManager().getEquippedClass(player);
            if (pvpClass != null) {
                event.addLine(config.getScoreboardSidebarPvpClassActiveClass().replace("%className%", pvpClass.getName()));
                if (pvpClass instanceof BardClass) {
                    BardClass bardClass = (BardClass) pvpClass;
                    event.addLine(config.getScoreboardSidebarPvpClassBardEnergy().replace("%energy", handleBardFormat(bardClass.getEnergyMillis(player), true) ));
                    long remaining = bardClass.getRemainingBuffDelay(player);
                    if (remaining > 0) {
                        event.addLine(config.getScoreboardSidebarPvpClassBardBuffDelay().replace("%buffDelay%", DurationFormatter.getRemaining(remaining, true)));
                    }
                } else if (pvpClass instanceof ArcherClass) {
                    ArcherClass archerClass = (ArcherClass) pvpClass;

                    List<Map.Entry<UUID, ArcherMark>> entryList = Ordering.from(ARCHER_MARK_COMPARATOR).sortedCopy(archerClass.getSentMarks(player).entrySet());
                    entryList = entryList.subList(0, Math.min(entryList.size(), 3));
                    for (Map.Entry<UUID, ArcherMark> entry : entryList) {
                        ArcherMark archerMark = entry.getValue();
                        Player target = Bukkit.getPlayer(entry.getKey());
                        if (target != null) {
                            ChatColor levelColour;
                            switch (archerMark.currentLevel) {
                                case 1:
                                    levelColour = ChatColor.getByChar(config.getScoreboardSidebarPvpClassMarkColorLevelOne());
                                    break;
                                case 2 | 3:
                                    levelColour = ChatColor.getByChar(config.getScoreboardSidebarPvpClassMarkColorLevelTwo());
                                    break;
                                default:
                                    levelColour = ChatColor.getByChar(config.getScoreboardSidebarPvpClassMarkColorLevelThree());
                                    break;
                            }

                            // Add the current mark level to scoreboard.
                            String targetName = target.getName();
                            event.addLine(config.getScoreboardSidebarPvpClassArcherMark()
                                    .replace("%targetName%", targetName)
                                    .replace("%levelColor%", levelColour.toString())
                                    .replace("%markLevel%", String.valueOf(archerMark.currentLevel))
                            );
                        }
                    }
                } else if (pvpClass instanceof MinerClass) {
                    String invisibility = PlayerUtil.hasEffect(player, PotionEffectType.INVISIBILITY) ? config.getScoreboardSidebarPvpClassMinerInvisibilityEnabled() : config.getScoreboardSidebarPvpClassMinerInvisibilityDisabled();
                    event.addLine(config.getScoreboardSidebarPvpClassMinerInvisibilityStatus().replace("%invisibility%", invisibility));
                }
            }

            Collection<com.doctordark.hcf.timer.Timer> timers = plugin.getTimerManager().getTimers();
            for (Timer timer : timers) {
                if (timer instanceof PlayerTimer) {
                    PlayerTimer playerTimer = (PlayerTimer) timer;
                    long remaining = playerTimer.getRemaining(player);
                    if (remaining <= 0) continue;

                    String timerName = playerTimer.getName();
                    event.addLine(config.getScoreboardSidebarTimer().replace("%timer%", timerName).replace("%remaining%", DurationFormatter.getRemaining(remaining, true)));
                }
            }

            if (conquestLines != null && !conquestLines.isEmpty()) {
                if (!event.getLines().isEmpty()) {
                    conquestLines.add("");
                }

                conquestLines.forEach(event::addLine);
            }
        }

    }

    @Override
    public int updateRate() {
        return plugin.getConfiguration().getScoreboardSidebarUpdateRate();
    }

    private static String handleBardFormat(long millis, boolean trailingZero) {
        return (trailingZero ? DateTimeFormats.REMAINING_SECONDS_TRAILING : DateTimeFormats.REMAINING_SECONDS).get().format(millis * 0.001);
    }
}
