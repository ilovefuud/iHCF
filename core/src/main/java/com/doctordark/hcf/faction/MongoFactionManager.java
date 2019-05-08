/*package com.doctordark.hcf.faction;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.claim.Claim;
import com.doctordark.hcf.faction.type.*;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Table;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import us.lemin.core.CorePlugin;
import us.lemin.core.storage.database.MongoStorage;
import us.lemin.core.storage.flatfile.Config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MongoFactionManager implements Listener, FactionManager{

    private Config config;
    private final HCF plugin;
    private final MongoStorage mongoStorage;

    // The default claimless factions.
    private final WarzoneFaction warzone;
    private final WildernessFaction wilderness;

    // Cached for faster lookup for factions. Potentially usage Guava Cache for
    // future implementations (database).
    private final Table<String, Long, Claim> claimPositionMap = HashBasedTable.create();
    private final Map<UUID, UUID> factionPlayerUuidMap = new HashMap<>();
    private final Map<UUID, Faction> factionUUIDMap = new HashMap<>();
    private final Map<String, UUID> factionNameMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);


    public MongoFactionManager(HCF plugin) {
        this.plugin = plugin;
        this.mongoStorage = CorePlugin.getInstance().getMongoStorage();
        this.warzone = new WarzoneFaction();
        this.wilderness = new WildernessFaction();
        this.reloadFactionData();
    }

    @Override
    public Map<String, ?> getFactionNameMap() {
        return null;
    }

    @Override
    public ImmutableList<Faction> getFactions() {
        return null;
    }

    @Override
    public Claim getClaimAt(Location location) {
        return null;
    }

    @Override
    public Claim getClaimAt(World world, int x, int z) {
        return null;
    }

    @Override
    public Faction getFactionAt(Location location) {
        return null;
    }

    @Override
    public Faction getFactionAt(Block block) {
        return null;
    }

    @Override
    public Faction getFactionAt(World world, int x, int z) {
        return null;
    }

    @Override
    public Faction getFaction(String factionName) {
        return null;
    }

    @Override
    public Faction getFaction(UUID factionUUID) {
        return null;
    }

    @Override
    public PlayerFaction getContainingPlayerFaction(String search) {
        return null;
    }

    @Override
    public PlayerFaction getPlayerFaction(Player player) {
        return null;
    }

    @Override
    public PlayerFaction getPlayerFaction(UUID uuid) {
        return null;
    }

    @Override
    public Faction getContainingFaction(String id) {
        return null;
    }

    @Override
    public boolean containsFaction(Faction faction) {
        return false;
    }

    @Override
    public boolean createFaction(Faction faction) {
        return false;
    }

    @Override
    public boolean createFaction(Faction faction, CommandSender sender) {
        return false;
    }

    @Override
    public boolean removeFaction(Faction faction, CommandSender sender) {
        return false;
    }

    @Override
    public void reloadFactionData() {
        this.factionNameMap.clear();
        MongoCursor<Document> cursor = mongoStorage.getAllDocuments("factions");

        while (cursor.hasNext()) {
            Document nextFaction = cursor.next();

        }


        Object object = config.get("factions");
        if (object instanceof MemorySection) {
            MemorySection section = (MemorySection) object;
            for (String factionName : section.getKeys(false)) {
                Object next = config.get(section.getCurrentPath() + '.' + factionName);
                if (next instanceof Faction) {
                    cacheFaction((Faction) next);
                }
            }
        } else if (object instanceof List<?>) {
            List<?> list = (List<?>) object;
            for (Object next : list) {
                if (next instanceof Faction) {
                    cacheFaction((Faction) next);
                }
            }
        }

        Set<Faction> adding = new HashSet<>();
        if (!factionNameMap.containsKey("NorthRoad")) { //TODO: more reliable
            adding.add(new RoadFaction.NorthRoadFaction());
            adding.add(new RoadFaction.EastRoadFaction());
            adding.add(new RoadFaction.SouthRoadFaction());
            adding.add(new RoadFaction.WestRoadFaction());
        }

        if (!factionNameMap.containsKey("Spawn")) { //TODO: more reliable
            adding.add(new SpawnFaction());
        }

        if (!this.factionNameMap.containsKey("EndPortal")) { //TODO: more reliable
            adding.add(new EndPortalFaction());
        }

        // Now load the Spawn, etc factions.
        for (Faction added : adding) {
            this.cacheFaction(added);
            Bukkit.getConsoleSender().sendMessage(ChatColor.BLUE + "Faction " + added.getName() + " not found, created.");
        }
    }

    @Override
    public void saveFactionData() {

    }
}
*/