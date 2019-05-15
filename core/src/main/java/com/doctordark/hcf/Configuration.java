package com.doctordark.hcf;

import com.doctordark.hcf.util.PersistableLocation;
import gnu.trove.impl.Constants;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionType;
import us.lemin.core.storage.flatfile.Config;

import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Getter
public class Configuration {

    private final Config config;

    private boolean kitmap;
    private boolean spawnCannon;
    private boolean handleEntityLimiting;
    private boolean removeInfinityArrowsOnLand;
    private int beaconStrengthLevelLimit;
    private boolean disableBoatPlacementOnLand;
    private boolean handleChat;
    private boolean vault;
    private boolean essentials;
    private boolean enderpearlGlitchingEnabled;
    private boolean enderpearlGlitchingRefund;
    private boolean disableEnderchests;
    private boolean preventPlacingBedsNether;
    @Getter(AccessLevel.NONE)
    private String serverTimeZoneName = "EST";
    private TimeZone serverTimeZone;
    private ZoneId serverTimeZoneID;
    private float furnaceCookSpeedMultiplier;
    private boolean bottledExp;
    private boolean bookDisenchanting;
    private boolean deathSigns;
    private boolean deathLightning;
    private int mapNumber;
    private boolean preventAllyAttackDamage;
    private int economyStartingBalance;
    private boolean spawnersPreventBreakingNether;
    private boolean spawnersPreventPlacingNether;
    private float expMultiplierGlobal;
    private float expMultiplierFishing;
    private float expMultiplierSmelting;
    private float expMultiplierLootingPerLevel;
    private float expMultiplierLuckPerLevel;
    private float expMultiplierFortunePerLevel;
    private String scoreboardSidebarTitle;
    private boolean scoreboardSidebarEnabled;
    private int scoreboardSidebarUpdateRate;
    private String scoreboardSidebarKitmapKills;
    private String scoreboardSidebarKitmapDeaths;
    private String scoreboardSidebarKitmapKillstreak;
    private String scoreboardSidebarEotwCountdown;
    private String scoreboardSidebarEotwCappable;
    private String scoreboardSidebarSotw;
    private String scoreboardSidebarActiveKoth;
    private String scoreboardSidebarConquestActiveConquest;
    private String scoreboardSidebarConquestLineOne;
    private String scoreboardSidebarConquestLineTwo;
    private String scoreboardSidebarConquestTopThree;
    private String scoreboardSidebarPvpClassActiveClass;
    private String scoreboardSidebarPvpClassBardEnergy;
    private String scoreboardSidebarPvpClassBardBuffDelay;
    private String scoreboardSidebarPvpClassMarkColorLevelOne;
    private String scoreboardSidebarPvpClassMarkColorLevelTwo;
    private String scoreboardSidebarPvpClassMarkColorLevelThree;
    private String scoreboardSidebarPvpClassArcherMark;
    private String scoreboardSidebarPvpClassMinerInvisibilityEnabled;
    private String scoreboardSidebarPvpClassMinerInvisibilityDisabled;
    private String scoreboardSidebarPvpClassMinerInvisibilityStatus;
    private String scoreboardSidebarTimerFormat;
    private String scoreboardSidebarTimerGapplePrefix;
    private String scoreboardSidebarTimerCombatPrefix;
    private String scoreboardSidebarTimerPearlPrefix;
    private String scoreboardSidebarTimerInvincibilityPrefix;
    private String scoreboardSidebarTimerLogoutPrefix;
    private String scoreboardSidebarTimerClassWarmupPrefix;
    private String scoreboardSidebarTimerStuckPrefix;
    private String scoreboardSidebarTimerTeleportPrefix;
    private String scoreboardTablistTitle;
    private int scoreboardTablistUpdateRate;
    private boolean scoreboardTablistEnabled;
    private boolean scoreboardNametagsEnabled;
    private boolean handleCombatLogging;
    private int combatlogDespawnDelayTicks;
    private int warzoneRadiusOverworld;
    private int warzoneRadiusNether;
    private boolean allowCreationDuringEOTW;
    private int conquestPointLossPerDeath;
    private int conquestRequiredVictoryPoints;
    private boolean conquestAllowNegativePoints;
    private boolean allowClaimsBesideRoads;
    //TODO: CaseInsensitiveList
    private List<String> factionDisallowedNames;
    private int factionHomeTeleportDelayOverworldSeconds;
    private long factionHomeTeleportDelayOverworldMillis;
    private int factionHomeTeleportDelayNetherSeconds;
    private long factionHomeTeleportDelayNetherMillis;
    private int factionHomeTeleportDelayEndSeconds;
    private long factionHomeTeleportDelayEndMillis;
    private int maxHeightFactionHome;
    private boolean allowTeleportingInEnemyTerritory;
    private int factionNameMinCharacters;
    private int factionNameMaxCharacters;
    private int factionMaxMembers;
    private int factionMaxClaims;
    private int factionMaxAllies;
    private int factionSubclaimNameMinCharacters;
    private int factionSubclaimNameMaxCharacters;
    private int factionDtrRegenFreezeBaseMinutes;
    private long factionDtrRegenFreezeBaseMilliseconds;
    @Getter(AccessLevel.NONE)
    private int factionDtrRegenFreezeMinutesPerMember;
    private long factionDtrRegenFreezeMillisecondsPerMember;
    private int factionMinimumDtr;
    private float factionMaximumDtr;
    private int factionDtrUpdateMillis; // 45 seconds
    private String factionDtrUpdateTimeWords;
    private float factionDtrUpdateIncrement;
    @Getter(AccessLevel.NONE)
    private String relationColourWarzoneName;
    private ChatColor relationColourWarzone = ChatColor.LIGHT_PURPLE;
    @Getter(AccessLevel.NONE)
    private String relationColourWildernessName;
    private ChatColor relationColourWilderness = ChatColor.DARK_GREEN;
    @Getter(AccessLevel.NONE)
    private String relationColourTeammateName;
    private ChatColor relationColourTeammate = ChatColor.GREEN;
    @Getter(AccessLevel.NONE)
    private String relationColourAllyName;
    private ChatColor relationColourAlly = ChatColor.GOLD;
    @Getter(AccessLevel.NONE)
    private String relationColourEnemyName;
    private ChatColor relationColourEnemy = ChatColor.RED;
    @Getter(AccessLevel.NONE)
    private String relationColourNeutralName;
    private ChatColor relationColourNeutral = ChatColor.YELLOW;
    @Getter(AccessLevel.NONE)
    private String relationColourFocusName;
    private ChatColor relationColourFocus = ChatColor.LIGHT_PURPLE;
    @Getter(AccessLevel.NONE)
    private String relationColourRoadName;
    private ChatColor relationColourRoad = ChatColor.YELLOW;
    @Getter(AccessLevel.NONE)
    private String relationColourSafezoneName;
    private ChatColor relationColourSafezone = ChatColor.AQUA;
    @Setter
    private int deathbanBaseDurationMinutes;
    @Setter
    private int deathbanRespawnScreenSecondsBeforeKick;
    private long deathbanRespawnScreenTicksBeforeKick;
    private boolean endOpen;
    private String endExitLocationRaw;
    private PersistableLocation endExitLocation = new PersistableLocation(Bukkit.getWorld("world"), 0.5, 75, 0.5);
    private boolean endExtinguishFireOnExit;
    private boolean endRemoveStrengthOnEntrance;
    private String eotwChatSymbolPrefix;
    private String eotwChatSymbolSuffix;
    //TODO: UUID list not UUID string list
    private List<String> eotwLastMapCapperUuids;
    @SuppressWarnings("ALL")
    private List<String> enchantmentLimitsUnstored = new ArrayList<>();
    @SuppressWarnings("ALL")
    private List<String> potionLimitsUnstored = new ArrayList<>();
    private boolean subclaimSignPrivate;
    private boolean subclaimSignCaptain;
    private boolean subclaimSignLeader;
    private boolean subclaimHopperCheck;
    private String shopLocationRaw;
    private PersistableLocation shopLocation = new PersistableLocation(Bukkit.getWorld("world"), 0.5, 75, 0.5);


    public Configuration(HCF plugin) {
        this.config = new Config(plugin, "config");

        updateConfig();
        loadFromConfig();
        updateFields();
    }

    private final TObjectIntMap<Enchantment> enchantmentLimits = new TObjectIntHashMap<>(Constants.DEFAULT_CAPACITY, Constants.DEFAULT_LOAD_FACTOR, -1);

    private final HashMap<PotionType, PotionLimitData> potionLimits = new HashMap<>();

    public int getEnchantmentLimit(Enchantment enchantment) {
        int maxLevel = enchantmentLimits.get(enchantment);
        return maxLevel == enchantmentLimits.getNoEntryValue() ? enchantment.getMaxLevel() : maxLevel;
    }

    public int getPotionLimit(PotionType potionEffectType) {
        if (potionLimits.get(potionEffectType) == null) {
            return potionEffectType.getMaxLevel();
        }
        return potionLimits.get(potionEffectType).getMaxLevel();
    }

    public boolean isPotionExtendable(PotionType potionEffectType) {
        if (potionLimits.get(potionEffectType) == null) {
            return potionEffectType.isInstant();
        }
        return potionLimits.get(potionEffectType).isExtendable();
    }

    private void updateConfig() {
        Map<String, Object> map = new HashMap<>();
        /*try {
            for (Field field : this.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getAnnotation(Setting.class) != null) {
                    map.put(field.getAnnotation(Setting.class).value(), field.get(this));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        map.put("kitmap", false);
        map.put("spawnCannon", true);
        map.put("handleEntityLimiting", true);
        map.put("removeInfinityArrowsOnLand", true);
        map.put("beaconStrengthLevelLimit", 1);
        map.put("disableBoatPlacementOnLand", true);
        map.put("chat.handleChat", true);
        map.put("chat.vault", false);
        map.put("chat.essentials", true);
        map.put("enderpearlGlitching.enabled", true);
        map.put("enderpearlGlitching.refund", true);
        map.put("disableEnderchests", true);
        map.put("preventPlacingBedsNether", false);
        map.put("serverTimeZone", "EST");
        map.put("furnaceCookSpeedMultiplier", 6.0f);
        map.put("bottledExp", true);
        map.put("bookDisenchanting", true);
        map.put("deathSigns", true);
        map.put("deathLightning", true);
        map.put("mapNumber", 1);
        map.put("preventAllyDamage", true);
        map.put("economy.startingBalance", 250);
        map.put("spawners.preventBreakingNether", true);
        map.put("spawners.preventPlacingNether", true);
        map.put("expMultiplier.global", 1.0F);
        map.put("expMultiplier.fishing", 1.0F);
        map.put("expMultiplier.smelting", 1.0F);
        map.put("expMultiplier.lootingPerLevel", 1.0F);
        map.put("expMultiplier.luckPerLevel", 1.0F);
        map.put("expMultiplier.fortunePerLevel", 1.0F);
        map.put("scoreboard.sidebar.title", "&a&lHCF &c[Map {MAP_NUMBER}]");
        map.put("scoreboard.sidebar.enabled", true);
        map.put("scoreboard.sidebar.updateRate", 2);
        map.put("scoreboard.sidebar.kitmap.kills", "&4&lKills: %kills%");
        map.put("scoreboard.sidebar.kitmap.deaths", "&4&lDeaths: %deaths%");
        map.put("scoreboard.sidebar.kitmap.killstreak", "&4&lKillstreak: %killstreak%");
        map.put("scoreboard.sidebar.eotw.countdown", "&4&lEOTW &cstarts in &l%remaining%");
        map.put("scoreboard.sidebar.eotw.cappable", "&4&lEOTW &ccappable in &l%remaining%");
        map.put("scoreboard.sidebar.sotw", "&2&lSOTW&7: &6%remaining%");
        map.put("scoreboard.sidebar.activeKoth", "&a%kothName%&7: &6%remaining%");
        map.put("scoreboard.sidebar.conquest.activeConquest", "&9&l%conquestName%&7:");
        map.put("scoreboard.sidebar.conquest.lineOne", "  &c%redRemaining%&r &e%yellowRemaining%");
        map.put("scoreboard.sidebar.conquest.lineTwo", "  &a%greenRemaining%&r &b%blueRemaining%");
        map.put("scoreboard.sidebar.conquest.topThree", "&d&l%factionName%&7: &e%score%");
        map.put("scoreboard.sidebar.pvpClass.activeClass", "&eActive Class&7: &a%className%");
        map.put("scoreboard.sidebar.pvpClass.bard.energy", " &5\u00bb &dEnergy&7: &6%energy%");
        map.put("scoreboard.sidebar.pvpClass.bard.buffDelay", " &5\u00bb &dBuff Delay&7: &6%buffDelay%");
        map.put("scoreboard.sidebar.pvpClass.archer.markColorLevel.one", "&a");
        map.put("scoreboard.sidebar.pvpClass.archer.markColorLevel.two", "&c");
        map.put("scoreboard.sidebar.pvpClass.archer.markColorLevel.three", "&e");
        map.put("scoreboard.sidebar.pvpClass.archer.archerMark", " &d\u00bb&c %targetName% %levelColor%[Mark %markLevel%]");
        map.put("scoreboard.sidebar.pvpClass.miner.invisibility.enabled", "&aEnabled");
        map.put("scoreboard.sidebar.pvpClass.miner.invisibility.disabled", "&cDisabled");
        map.put("scoreboard.sidebar.pvpClass.miner.invisibility.status", " &5\u00bb &dInvisibility&7: %invisibility%");
        map.put("scoreboard.sidebar.timer.format", "&b%timer%&7: &6%remaining%");
        map.put("scoreboard.sidebar.timer.gapplePrefix", "&e&l");
        map.put("scoreboard.sidebar.timer.combatPrefix", "&4&l");
        map.put("scoreboard.sidebar.timer.pearlPrefix", "&d&l");
        map.put("scoreboard.sidebar.timer.invincibilityPrefix", "&2&l");
        map.put("scoreboard.sidebar.timer.logoutPrefix", "&c&l");
        map.put("scoreboard.sidebar.timer.classWarmupPrefix", "&b&l");
        map.put("scoreboard.sidebar.timer.stuckPrefix", "&3&l");
        map.put("scoreboard.sidebar.timer.teleportPrefix", "&3&l");
        map.put("scoreboard.tablist.title", "&a&lHCF");
        map.put("scoreboard.tablist.updateRate", 20);
        map.put("scoreboard.tablist.enabled", true);
        map.put("scoreboard.nametags.enabled", true);
        map.put("combatlog.enabled", true);
        map.put("combatlog.despawnDelayTicks", 600);
        map.put("warzone.radiusOverworld", 800);
        map.put("warzone.radiusNether", 800);
        map.put("factions.allowCreationDuringEOTW", false);
        map.put("factions.conquest.pointLossPerDeath", 20);
        map.put("factions.conquest.requiredVictoryPoints", 300);
        map.put("factions.conquest.allowNegativePoints", true);
        map.put("factions.roads.allowClaimsBesideRoads", true);
        map.put("factions.disallowedFactionNames", new ArrayList<>(Collections.singletonList("nigger")));
        map.put("factions.home.teleportDelay.NORMAL", 30);
        map.put("factions.home.teleportDelay.NETHER", -1);
        map.put("factions.home.teleportDelay.THE_END", 10);
        map.put("factions.home.maxHeight", -1);
        map.put("factions.home.allowTeleportingInEnemyTerritory", true);
        map.put("factions.nameMinCharacters", 3);
        map.put("factions.nameMaxCharacters", 16);
        map.put("factions.maxMembers", 25);
        map.put("factions.maxClaims", 8);
        map.put("factions.maxAllies", 1);
        map.put("factions.subclaim.nameMinCharacters", 3);
        map.put("factions.subclaim.nameMaxCharacters", 16);
        map.put("factions.dtr.regenFreeze.baseMinutes", 40);
        map.put("factions.dtr.regenFreeze.minutesPerMember", 2);
        map.put("factions.dtr.minimum", -50);
        map.put("factions.dtr.maximum", 6.0F);
        map.put("factions.dtr.millisecondsBetweenUpdates", 45000);
        map.put("factions.dtr.incrementBetweenUpdates", 0.1F);
        map.put("factions.relationColours.warzone", "LIGHT_PURPLE");
        map.put("factions.relationColours.wilderness", "DARK_GREEN");
        map.put("factions.relationColours.teammate", "GREEN");
        map.put("factions.relationColours.enemy", "RED");
        map.put("factions.relationColours.ally", "GOLD");
        map.put("factions.relationColours.neutral", "YELLOW");
        map.put("factions.relationColours.focus", "LIGHT_PURPLE");
        map.put("factions.relationColours.road", "YELLOW");
        map.put("factions.relationColours.safezone", "AQUA");
        map.put("deathban.baseDurationMinutes", 60);
        map.put("deathban.respawnScreenSecondsBeforeKick", 15);
        map.put("end.open", true);
        map.put("end.exitLocation", "world,0.5,75,0.5,0,0");
        map.put("end.extinguishFireOnExit", true);
        map.put("end.removeStrengthOnEntrance", true);
        map.put("eotw.chatSymbolPrefix", " \u2605");
        map.put("eotw.chatSymbolSuffix", "");
        map.put("eotw.lastMapCapperUuids", new ArrayList<>());
        map.put("enchantmentLimits", new ArrayList<>());
        map.put("potionLimits", new ArrayList<>());
        map.put("subclaimSigns.private", false);
        map.put("subclaimSigns.captain", false);
        map.put("subclaimSigns.leader", false);
        map.put("subclaimSigns.hopperCheck", false);
        map.put("shopLocation", "world,0.5,75,0.5,0,0");
        config.addDefaults(map);
        config.copyDefaults();
    }

    private void loadFromConfig() {
        this.kitmap = (boolean) config.get("kitmap");
        this.spawnCannon = (boolean) config.get("spawnCannon");
        this.handleEntityLimiting = (boolean) config.get("handleEntityLimiting");
        this.removeInfinityArrowsOnLand = (boolean) config.get("removeInfinityArrowsOnLand");
        this.beaconStrengthLevelLimit = (int) config.get("beaconStrengthLevelLimit");
        this.disableBoatPlacementOnLand = (boolean) config.get("disableBoatPlacementOnLand");
        this.handleChat = (boolean) config.get("chat.handleChat");
        this.vault = (boolean) config.get("chat.vault");
        this.essentials = (boolean) config.get("chat.essentials");
        this.enderpearlGlitchingEnabled = (boolean) config.get("enderpearlGlitching.enabled");
        this.enderpearlGlitchingRefund = (boolean) config.get("enderpearlGlitching.refund");
        this.disableEnderchests = (boolean) config.get("disableEnderchests");
        this.preventPlacingBedsNether = (boolean) config.get("preventPlacingBedsNether");
        this.serverTimeZoneName = (String) config.get("serverTimeZone");
        this.furnaceCookSpeedMultiplier = Float.valueOf(String.valueOf(config.get("furnaceCookSpeedMultiplier")));
        this.bottledExp = (boolean) config.get("bottledExp");
        this.bookDisenchanting = (boolean) config.get("bookDisenchanting");
        this.deathSigns = (boolean) config.get("deathSigns");
        this.deathLightning = (boolean) config.get("deathLightning");
        this.mapNumber = (int) config.get("mapNumber");
        this.preventAllyAttackDamage = (boolean) config.get("preventAllyDamage");
        this.economyStartingBalance = (int) config.get("economy.startingBalance");
        this.spawnersPreventBreakingNether = (boolean) config.get("spawners.preventBreakingNether");
        this.spawnersPreventPlacingNether = (boolean) config.get("spawners.preventPlacingNether");
        this.expMultiplierGlobal = Float.valueOf(String.valueOf(config.get("expMultiplier.global")));
        this.expMultiplierFishing = Float.valueOf(String.valueOf(config.get("expMultiplier.fishing")));
        this.expMultiplierSmelting = Float.valueOf(String.valueOf(config.get("expMultiplier.smelting")));
        this.expMultiplierLootingPerLevel = Float.valueOf(String.valueOf(config.get("expMultiplier.lootingPerLevel")));
        this.expMultiplierLuckPerLevel = Float.valueOf(String.valueOf(config.get("expMultiplier.luckPerLevel")));
        this.expMultiplierFortunePerLevel = Float.valueOf(String.valueOf(config.get("expMultiplier.fortunePerLevel")));
        this.scoreboardSidebarTitle = (String) config.get("scoreboard.sidebar.title");
        this.scoreboardSidebarEnabled = (boolean) config.get("scoreboard.sidebar.enabled");
        this.scoreboardSidebarUpdateRate = (int) config.get("scoreboard.sidebar.updateRate");
        this.scoreboardSidebarKitmapKills = (String) config.get("scoreboard.sidebar.kitmap.kills");
        this.scoreboardSidebarKitmapDeaths = (String) config.get("scoreboard.sidebar.kitmap.deaths");
        this.scoreboardSidebarKitmapKillstreak = (String) config.get("scoreboard.sidebar.kitmap.killstreak");
        this.scoreboardSidebarEotwCountdown = (String) config.get("scoreboard.sidebar.eotw.countdown");
        this.scoreboardSidebarEotwCappable = (String) config.get("scoreboard.sidebar.eotw.cappable");
        this.scoreboardSidebarSotw = (String) config.get("scoreboard.sidebar.sotw");
        this.scoreboardSidebarActiveKoth = (String) config.get("scoreboard.sidebar.activeKoth");
        this.scoreboardSidebarConquestActiveConquest = (String) config.get("scoreboard.sidebar.conquest.activeConquest");
        this.scoreboardSidebarConquestLineOne = (String) config.get("scoreboard.sidebar.conquest.lineOne");
        this.scoreboardSidebarConquestLineTwo = (String) config.get("scoreboard.sidebar.conquest.lineTwo");
        this.scoreboardSidebarConquestTopThree = (String) config.get("scoreboard.sidebar.conquest.topThree");
        this.scoreboardSidebarPvpClassActiveClass = (String) config.get("scoreboard.sidebar.pvpClass.activeClass");
        this.scoreboardSidebarPvpClassBardEnergy = (String) config.get("scoreboard.sidebar.pvpClass.bard.energy");
        this.scoreboardSidebarPvpClassBardBuffDelay = (String) config.get("scoreboard.sidebar.pvpClass.bard.buffDelay");
        this.scoreboardSidebarPvpClassMarkColorLevelOne = (String) config.get("scoreboard.sidebar.pvpClass.archer.markColorLevel.one");
        this.scoreboardSidebarPvpClassMarkColorLevelTwo = (String) config.get("scoreboard.sidebar.pvpClass.archer.markColorLevel.two");
        this.scoreboardSidebarPvpClassMarkColorLevelThree = (String) config.get("scoreboard.sidebar.pvpClass.archer.markColorLevel.three");
        this.scoreboardSidebarPvpClassArcherMark = (String) config.get("scoreboard.sidebar.pvpClass.archer.archerMark");
        this.scoreboardSidebarPvpClassMinerInvisibilityEnabled = (String) config.get("scoreboard.sidebar.pvpClass.miner.invisibility.enabled");
        this.scoreboardSidebarPvpClassMinerInvisibilityDisabled = (String) config.get("scoreboard.sidebar.pvpClass.miner.invisibility.disabled");
        this.scoreboardSidebarPvpClassMinerInvisibilityStatus = (String) config.get("scoreboard.sidebar.pvpClass.miner.invisibility.status");
        this.scoreboardSidebarTimerFormat = (String) config.get("scoreboard.sidebar.timer.format");
        this.scoreboardSidebarTimerGapplePrefix = (String) config.get("scoreboard.sidebar.timer.gapplePrefix");
        this.scoreboardSidebarTimerCombatPrefix = (String) config.get("scoreboard.sidebar.timer.combatPrefix");
        this.scoreboardSidebarTimerPearlPrefix = (String) config.get("scoreboard.sidebar.timer.pearlPrefix");
        this.scoreboardSidebarTimerInvincibilityPrefix = (String) config.get("scoreboard.sidebar.timer.invincibilityPrefix");
        this.scoreboardSidebarTimerLogoutPrefix = (String) config.get("scoreboard.sidebar.timer.logoutPrefix");
        this.scoreboardSidebarTimerClassWarmupPrefix = (String) config.get("scoreboard.sidebar.timer.classWarmupPrefix");
        this.scoreboardSidebarTimerStuckPrefix = (String) config.get("scoreboard.sidebar.timer.stuckPrefix");
        this.scoreboardSidebarTimerTeleportPrefix = (String) config.get("scoreboard.sidebar.timer.teleportPrefix");
        this.scoreboardTablistTitle = (String) config.get("scoreboard.tablist.title");
        this.scoreboardTablistUpdateRate = (int) config.get("scoreboard.tablist.updateRate");
        this.scoreboardTablistEnabled = (boolean) config.get("scoreboard.tablist.enabled");
        this.scoreboardNametagsEnabled = (boolean) config.get("scoreboard.nametags.enabled");
        this.handleCombatLogging = (boolean) config.get("combatlog.enabled");
        this.combatlogDespawnDelayTicks = (int) config.get("combatlog.despawnDelayTicks");
        this.warzoneRadiusOverworld = (int) config.get("warzone.radiusOverworld");
        this.warzoneRadiusNether = (int) config.get("warzone.radiusNether");
        this.allowCreationDuringEOTW = (boolean) config.get("factions.allowCreationDuringEOTW");
        this.conquestPointLossPerDeath = (int) config.get("factions.conquest.pointLossPerDeath");
        this.conquestRequiredVictoryPoints = (int) config.get("factions.conquest.requiredVictoryPoints");
        this.conquestAllowNegativePoints = (boolean) config.get("factions.conquest.allowNegativePoints");
        this.allowClaimsBesideRoads = (boolean) config.get("factions.roads.allowClaimsBesideRoads");
        this.factionDisallowedNames = (List<String>) config.get("factions.disallowedFactionNames");
        this.factionHomeTeleportDelayOverworldSeconds = (int) config.get("factions.home.teleportDelay.NORMAL");
        this.factionHomeTeleportDelayNetherSeconds = (int) config.get("factions.home.teleportDelay.NETHER");
        this.factionHomeTeleportDelayEndSeconds = (int) config.get("factions.home.teleportDelay.THE_END");
        this.maxHeightFactionHome = (int) config.get("factions.home.maxHeight");
        this.allowTeleportingInEnemyTerritory = (boolean) config.get("factions.home.allowTeleportingInEnemyTerritory");
        this.factionNameMinCharacters = (int) config.get("factions.nameMinCharacters");
        this.factionNameMaxCharacters = (int) config.get("factions.nameMaxCharacters");
        this.factionMaxMembers = (int) config.get("factions.maxMembers");
        this.factionMaxClaims = (int) config.get("factions.maxClaims");
        this.factionMaxAllies = (int) config.get("factions.maxAllies");
        this.factionSubclaimNameMinCharacters = (int) config.get("factions.subclaim.nameMinCharacters");
        this.factionSubclaimNameMaxCharacters = (int) config.get("factions.subclaim.nameMaxCharacters");
        this.factionDtrRegenFreezeBaseMinutes = (int) config.get("factions.dtr.regenFreeze.baseMinutes");
        this.factionDtrRegenFreezeMinutesPerMember = (int) config.get("factions.dtr.regenFreeze.minutesPerMember");
        this.factionMinimumDtr = (int) config.get("factions.dtr.minimum");
        this.factionMaximumDtr = Float.valueOf(String.valueOf(config.get("factions.dtr.maximum")));
        this.factionDtrUpdateMillis = (int) config.get("factions.dtr.millisecondsBetweenUpdates");
        this.factionDtrUpdateIncrement = Float.valueOf(String.valueOf(config.get("factions.dtr.incrementBetweenUpdates")));
        this.relationColourWarzoneName = (String) config.get("factions.relationColours.warzone");
        this.relationColourWildernessName = (String) config.get("factions.relationColours.wilderness");
        this.relationColourTeammateName = (String) config.get("factions.relationColours.teammate");
        this.relationColourAllyName = (String) config.get("factions.relationColours.ally");
        this.relationColourEnemyName = (String) config.get("factions.relationColours.enemy");
        this.relationColourNeutralName = (String) config.get("factions.relationColours.neutral");
        this.relationColourFocusName = (String) config.get("factions.relationColours.focus");
        this.relationColourRoadName = (String) config.get("factions.relationColours.road");
        this.relationColourSafezoneName = (String) config.get("factions.relationColours.safezone");
        this.deathbanBaseDurationMinutes = (int) config.get("deathban.baseDurationMinutes");
        this.deathbanRespawnScreenSecondsBeforeKick = (int) config.get("deathban.respawnScreenSecondsBeforeKick");
        this.endOpen = (boolean) config.get("end.open");
        this.endExitLocationRaw = (String) config.get("end.exitLocation");
        this.endExtinguishFireOnExit = (boolean) config.get("end.extinguishFireOnExit");
        this.endRemoveStrengthOnEntrance = (boolean) config.get("end.removeStrengthOnEntrance");
        this.eotwChatSymbolPrefix = (String) config.get("eotw.chatSymbolPrefix");
        this.eotwChatSymbolSuffix = (String) config.get("eotw.chatSymbolSuffix");
        this.eotwLastMapCapperUuids = (List<String>) config.get("eotw.lastMapCapperUuids");
        this.enchantmentLimitsUnstored = (List<String>) config.get("enchantmentLimits");
        this.potionLimitsUnstored = (List<String>) config.get("potionLimits");
        this.subclaimSignPrivate = (boolean) config.get("subclaimSigns.private");
        this.subclaimSignCaptain = (boolean) config.get("subclaimSigns.captain");
        this.subclaimSignLeader = (boolean) config.get("subclaimSigns.leader");
        this.subclaimHopperCheck = (boolean) config.get("subclaimSigns.hopperCheck");
        this.shopLocationRaw = (String) config.get("shopLocation");


    }

    protected void updateFields() {
        serverTimeZone = TimeZone.getTimeZone(serverTimeZoneName);
        serverTimeZoneID = serverTimeZone.toZoneId();
        scoreboardSidebarTitle = ChatColor.translateAlternateColorCodes('&',
                scoreboardSidebarTitle.replace("{MAP_NUMBER}", Integer.toString(mapNumber)));
        factionDtrUpdateTimeWords = DurationFormatUtils.formatDurationWords(factionDtrUpdateMillis, true, true);
        relationColourWarzone = ChatColor.valueOf(relationColourWarzoneName.replace(" ", "_").toUpperCase());
        relationColourWilderness = ChatColor.valueOf(relationColourWildernessName.replace(" ", "_").toUpperCase());
        relationColourTeammate = ChatColor.valueOf(relationColourTeammateName.replace(" ", "_").toUpperCase());
        relationColourAlly = ChatColor.valueOf(relationColourAllyName.replace(" ", "_").toUpperCase());
        relationColourEnemy = ChatColor.valueOf(relationColourEnemyName.replace(" ", "_").toUpperCase());
        relationColourNeutral = ChatColor.valueOf(relationColourNeutralName.replace(" ", "_").toUpperCase());
        relationColourFocus = ChatColor.valueOf(relationColourFocusName.replace(" ", "_").toUpperCase());
        relationColourRoad = ChatColor.valueOf(relationColourRoadName.replace(" ", "_").toUpperCase());
        relationColourSafezone = ChatColor.valueOf(relationColourSafezoneName.replace(" ", "_").toUpperCase());
        factionDtrRegenFreezeBaseMilliseconds = TimeUnit.MINUTES.toMillis(factionDtrRegenFreezeBaseMinutes);
        factionDtrRegenFreezeMillisecondsPerMember = TimeUnit.MINUTES.toMillis(factionDtrRegenFreezeMinutesPerMember);
        factionHomeTeleportDelayOverworldMillis = TimeUnit.SECONDS.toMillis(factionHomeTeleportDelayOverworldSeconds);
        factionHomeTeleportDelayNetherMillis = TimeUnit.SECONDS.toMillis(factionHomeTeleportDelayNetherSeconds);
        factionHomeTeleportDelayEndMillis = TimeUnit.SECONDS.toMillis(factionHomeTeleportDelayEndSeconds);
        deathbanRespawnScreenTicksBeforeKick = TimeUnit.SECONDS.toMillis(deathbanRespawnScreenSecondsBeforeKick) / 50L;

        String[] split = endExitLocationRaw.split(",");
        if (split.length == 6) {
            try {
                String worldName = split[0];
                if (Bukkit.getWorld(worldName) != null) {
                    Integer x = Integer.parseInt(split[0]);
                    Integer y = Integer.parseInt(split[1]);
                    Integer z = Integer.parseInt(split[2]);
                    Float yaw = Float.parseFloat(split[3]);
                    Float pitch = Float.parseFloat(split[3]);

                    endExitLocation = new PersistableLocation(worldName, x, y, z);
                    endExitLocation.setYaw(yaw);
                    endExitLocation.setPitch(pitch);
                }
            } catch (NumberFormatException ignored) {
            }
        }

        split = endExitLocationRaw.split(",");
        if (split.length == 6) {
            try {
                String worldName = split[0];
                if (Bukkit.getWorld(worldName) != null) {
                    Integer x = Integer.parseInt(split[0]);
                    Integer y = Integer.parseInt(split[1]);
                    Integer z = Integer.parseInt(split[2]);
                    Float yaw = Float.parseFloat(split[3]);
                    Float pitch = Float.parseFloat(split[3]);

                    shopLocation = new PersistableLocation(worldName, x, y, z);
                    shopLocation.setYaw(yaw);
                    shopLocation.setPitch(pitch);
                }
            } catch (NumberFormatException ignored) {
            }
        }

        String splitter = " = ";
        for (String entry : potionLimitsUnstored) {
            if (entry.contains(splitter)) {
                split = entry.split(splitter);
                String key = split[0];
                Integer value = Integer.parseInt(split[1]);
                boolean extended = Boolean.parseBoolean(split[2]);

                PotionType effect = PotionType.valueOf(key);
                if (effect != null) {
                    Bukkit.getLogger().log(Level.INFO, "Potion effect limit of " + effect.name() + " set as " + value + " and " + (extended ? "extendable" : "nonextendable."));
                    potionLimits.put(effect, new PotionLimitData(value, extended));
                } else {
                    Bukkit.getLogger().log(Level.WARNING, "Unknown potion effect '" + key + "'.");
                }
            }
        }

        for (String entry : enchantmentLimitsUnstored) {
            if (entry.contains(splitter)) {
                split = entry.split(splitter);
                String key = split[0];
                Integer value = Integer.parseInt(split[1]);

                Enchantment enchantment = Enchantment.getByName(key);
                if (enchantment != null) {
                    Bukkit.getLogger().log(Level.INFO, "Enchantment limit of " + enchantment.getName() + " set as " + value);
                    enchantmentLimits.put(enchantment, value);
                } else {
                    Bukkit.getLogger().log(Level.WARNING, "Unknown enchantment effect '" + key + "'.");
                }
            }
        }
    }

    @Getter
    @Setter
    public class PotionLimitData {

        private int maxLevel;
        private boolean extendable;

        public PotionLimitData(int maxLevel, boolean extendable) {
            this.maxLevel = maxLevel;
            this.extendable = extendable;
        }
    }
}