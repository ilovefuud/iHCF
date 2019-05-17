package us.lemin.hcf;

import com.google.common.base.Joiner;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import us.lemin.core.api.inventoryapi.InventoryManager;
import us.lemin.core.api.tablistapi.tab.TabHandler;
import us.lemin.hcf.combatlog.LoggerListener;
import us.lemin.hcf.command.*;
import us.lemin.hcf.command.pvptimer.PvpTimerCommand;
import us.lemin.hcf.command.sotw.SotwCommand;
import us.lemin.hcf.deathban.*;
import us.lemin.hcf.deathban.lives.LivesExecutor;
import us.lemin.hcf.economy.*;
import us.lemin.hcf.eventgame.CaptureZone;
import us.lemin.hcf.eventgame.EventExecutor;
import us.lemin.hcf.eventgame.EventScheduler;
import us.lemin.hcf.eventgame.conquest.ConquestExecutor;
import us.lemin.hcf.eventgame.crate.KeyListener;
import us.lemin.hcf.eventgame.crate.KeyManager;
import us.lemin.hcf.eventgame.eotw.EotwCommand;
import us.lemin.hcf.eventgame.eotw.EotwHandler;
import us.lemin.hcf.eventgame.eotw.EotwListener;
import us.lemin.hcf.eventgame.faction.CapturableFaction;
import us.lemin.hcf.eventgame.faction.ConquestFaction;
import us.lemin.hcf.eventgame.faction.KothFaction;
import us.lemin.hcf.eventgame.koth.KothExecutor;
import us.lemin.hcf.faction.FactionExecutor;
import us.lemin.hcf.faction.FactionManager;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.FlatFileFactionManager;
import us.lemin.hcf.faction.claim.*;
import us.lemin.hcf.faction.type.*;
import us.lemin.hcf.listener.*;
import us.lemin.hcf.listener.fixes.*;
import us.lemin.hcf.notimplemented.region.RegionListener;
import us.lemin.hcf.notimplemented.region.RegionManager;
import us.lemin.hcf.notimplemented.shop.ShopListener;
import us.lemin.hcf.notimplemented.shop.ShopManager;
import us.lemin.hcf.notimplemented.shop.inventory.*;
import us.lemin.hcf.pvpclass.PvpClassManager;
import us.lemin.hcf.pvpclass.bard.EffectRestorer;
import us.lemin.hcf.scoreboard.ScoreboardHandler;
import us.lemin.hcf.sotw.SotwListener;
import us.lemin.hcf.sotw.SotwTimer;
import us.lemin.hcf.timer.TimerExecutor;
import us.lemin.hcf.timer.TimerManager;
import us.lemin.hcf.user.FactionUser;
import us.lemin.hcf.user.UserManager;
import us.lemin.hcf.util.DateTimeFormats;
import us.lemin.hcf.util.ImageFolder;
import us.lemin.hcf.util.SignHandler;
import us.lemin.hcf.visualise.PacketHandler;
import us.lemin.hcf.visualise.VisualiseHandler;
import us.lemin.hcf.visualise.WallBorderListener;
import us.lemin.spigot.LeminSpigot;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class HCF extends JavaPlugin {

    public static final Joiner SPACE_JOINER = Joiner.on(' ');
    public static final Joiner COMMA_JOINER = Joiner.on(", ");

    @Getter
    private static HCF plugin;

    @Getter
    private ThreadLocalRandom random = ThreadLocalRandom.current();

    @Getter
    private Configuration configuration;

    @Getter
    private ClaimHandler claimHandler;

    @Getter
    private LoggerListener combatLogListener;

    @Getter
    private DeathbanManager deathbanManager;

    @Getter
    private EconomyManager economyManager;

    @Getter
    private EffectRestorer effectRestorer;

    @Getter
    private EotwHandler eotwHandler;

    @Getter
    private EventScheduler eventScheduler;

    @Getter
    private FactionManager factionManager;

    @Getter
    private ImageFolder imageFolder;

    @Getter
    private KeyManager keyManager;

    @Getter
    private PvpClassManager pvpClassManager;

    @Getter
    private ScoreboardHandler scoreboardHandler;

    @Getter
    private SotwTimer sotwTimer;

    @Getter
    private TimerManager timerManager;

    @Getter
    private UserManager userManager;

    @Getter
    private RegionManager regionManager;

    @Getter
    private VisualiseHandler visualiseHandler;

    @Getter
    private WorldEditPlugin worldEdit;

    @Getter
    private TabHandler tabHandler;

    @Getter
    private SignHandler signHandler;

    @Getter
    private InventoryManager inventoryManager;

    @Getter
    private ShopManager shopManager;


    @Getter
    private boolean paperPatch;

    private boolean configurationLoaded = true;

    @Override
    public void onEnable() {
        registerConfiguration();
        if (!configurationLoaded) {
            getLogger().severe("Disabling plugin..");
            setEnabled(false);
            return;
        }

        HCF.plugin = this;
        DateTimeFormats.reload(configuration.getServerTimeZone());        // Initialise the static fields.
        ///////////////////////////
        Plugin wep = getServer().getPluginManager().getPlugin("WorldEdit");  // Initialise WorldEdit hook.
        worldEdit = wep instanceof WorldEditPlugin && wep.isEnabled() ? (WorldEditPlugin) wep : null;

        registerSerialization();
        registerCommands();
        registerManagers();
        registerListeners();
        registerOptionals();

        paperPatch = false;
        /* TODO: BROKEN: Method does not exist
        try {
            Team team = getServer().getScoreboardManager().createNewTeam("lookup");
            team.unregister();
        } catch (NoSuchMethodError ex) {
            paperPatch = false;
        } */

        //TODO: More reliable, SQL based.
        long dataSaveInterval = TimeUnit.MINUTES.toMillis(20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                saveData();
            }
        }.runTaskTimerAsynchronously(this, dataSaveInterval, dataSaveInterval);

        LeminSpigot.INSTANCE.addPacketHandler(new PacketHandler(this)); // Initialise Visualize Packet Handler


    }

    private void saveData() {
        deathbanManager.saveDeathbanData();
        economyManager.saveEconomyData();
        factionManager.saveFactionData();
        keyManager.saveKeyData();
        timerManager.saveTimerData();
        userManager.saveUserData();
    }

    @Override
    public void onDisable() {
        /*if (!configurationLoaded) {
            // Ignore everything.
            return;
        }

        try {
            String configFileName = "src/config.cdl";
            configuration.save(new File(getDataFolder(), configFileName), HCF.class.getResource("/" + configFileName));
        } catch (IOException | InvalidConfigurationException ex) {
            getLogger().warning("Unable to save config.");
            ex.printStackTrace();
        }
        */
        getServer().getOnlinePlayers().forEach(HumanEntity::closeInventory);
        combatLogListener.removeAllVillagerLoggers();
        pvpClassManager.onDisable();
        scoreboardHandler.clearBoards();

        saveData();

        HCF.plugin = null; // Always uninitialise last.
    }

    private void registerConfiguration() {
        configuration = new Configuration(this);
        /*try {
            String configFileName = "config.cdl";
            File file = new File(getDataFolder(), configFileName);
            if (!file.exists()) {
                saveResource(configFileName, false);
            }

            configuration.load(file, HCF.class.getResource("/" + configFileName));
            configuration.updateFields();
        } catch (IOException | InvalidConfigurationException ex) {
            getLogger().log(Level.SEVERE, "Failed to load configuration", ex);
            configurationLoaded = false;
        }*/
    }

    //TODO: More reliable, SQL based.
    private void registerSerialization() {
        ConfigurationSerialization.registerClass(CaptureZone.class);
        ConfigurationSerialization.registerClass(Deathban.class);
        ConfigurationSerialization.registerClass(Claim.class);
        ConfigurationSerialization.registerClass(Subclaim.class);
        ConfigurationSerialization.registerClass(Deathban.class);
        ConfigurationSerialization.registerClass(FactionUser.class);
        ConfigurationSerialization.registerClass(ClaimableFaction.class);
        ConfigurationSerialization.registerClass(ConquestFaction.class);
        ConfigurationSerialization.registerClass(CapturableFaction.class);
        ConfigurationSerialization.registerClass(KothFaction.class);
        ConfigurationSerialization.registerClass(EndPortalFaction.class);
        ConfigurationSerialization.registerClass(Faction.class);
        ConfigurationSerialization.registerClass(FactionMember.class);
        ConfigurationSerialization.registerClass(PlayerFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.class);
        ConfigurationSerialization.registerClass(SpawnFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.NorthRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.EastRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.SouthRoadFaction.class);
        ConfigurationSerialization.registerClass(RoadFaction.WestRoadFaction.class);
    }

    private void registerListeners() {
        PluginManager manager = getServer().getPluginManager();
        manager.registerEvents(new BlockHitFixListener(), this);
        manager.registerEvents(new BlockJumpGlitchFixListener(), this);
        manager.registerEvents(new BoatGlitchFixListener(this), this);
        manager.registerEvents(new BookDisenchantListener(this), this);
        manager.registerEvents(new BottledExpListener(this), this);
        manager.registerEvents(new ClaimWandListener(this), this);
        manager.registerEvents(combatLogListener = new LoggerListener(this), this);
        manager.registerEvents(new CoreListener(this), this);
        manager.registerEvents(new CrowbarListener(this), this);
        manager.registerEvents(new DeathListener(this), this);
        manager.registerEvents(new DeathbanListener(this), this);
        manager.registerEvents(new EnchantLimitListener(this), this);
        manager.registerEvents(new EnderChestRemovalListener(this), this);
        manager.registerEvents(new EntityLimitListener(this), this);
        manager.registerEvents(new EotwListener(this), this);
        manager.registerEvents(new EventSignListener(), this);
        manager.registerEvents(new ExpMultiplierListener(this), this);
        manager.registerEvents(new FactionListener(this), this);
        manager.registerEvents(new FurnaceSmeltSpeedListener(this), this);
        manager.registerEvents(new InfinityArrowFixListener(this), this);
        manager.registerEvents(new KeyListener(this), this);
        manager.registerEvents(new PearlGlitchListener(this), this);
        manager.registerEvents(new PortalListener(this), this);
        manager.registerEvents(new PotionLimitListener(this), this);
        manager.registerEvents(new ProtectionListener(this), this);
        manager.registerEvents(new SubclaimWandListener(this), this);
        manager.registerEvents(new SignSubclaimListener(this), this);
        manager.registerEvents(new ShopSignListener(this), this);
        manager.registerEvents(new SkullListener(), this);
        manager.registerEvents(new SotwListener(this), this);
        manager.registerEvents(new BeaconStrengthFixListener(this), this);
        manager.registerEvents(new VoidGlitchFixListener(), this);
        manager.registerEvents(new WallBorderListener(this), this);
        manager.registerEvents(new WorldListener(), this);
        manager.registerEvents(new RegionListener(this), this);
        manager.registerEvents(new ShopListener(this), this);
        manager.registerEvents(signHandler = new SignHandler(this), this);
    }

    private void registerCommands() {
        registerCommands(
                new AngleCommand(),
                new LivesExecutor(this),
                new StaffReviveCommand(this),
                new ConquestExecutor(this),
                new EconomyCommand(this),
                new EotwCommand(this),
                new EventExecutor(this),
                new FactionExecutor(this),
                new GoppleCommand(this),
                new KothExecutor(this),
                new LivesExecutor(this),
                new LocationCommand(this),
                new LogoutCommand(this),
                new MapKitCommand(this),
                new PayCommand(this),
                new PvpTimerCommand(this),
                new RegenCommand(this),
                new ServerTimeCommand(this),
                new SotwCommand(this),
                new SpawnCannonCommand(this),
                new TimerExecutor(this),
                new ToggleCapzoneEntryCommand(this),
                new ToggleLightningCommand(this),
                new ToggleSidebarCommand(this)
        );
    }

    private void registerManagers() {
        claimHandler = new ClaimHandler(this);
        deathbanManager = new FlatFileDeathbanManager(this);
        economyManager = new FlatFileEconomyManager(this);
        effectRestorer = new EffectRestorer(this);
        eotwHandler = new EotwHandler(this);
        eventScheduler = new EventScheduler(this);
        factionManager = new FlatFileFactionManager(this);
        imageFolder = new ImageFolder(this);
        keyManager = new KeyManager(this);
        pvpClassManager = new PvpClassManager(this);
        sotwTimer = new SotwTimer();
        timerManager = new TimerManager(this); // Needs to be registered before ScoreboardHandler.
        scoreboardHandler = new ScoreboardHandler(this);
        userManager = new UserManager(this);
        visualiseHandler = new VisualiseHandler();
        regionManager = new RegionManager();
        shopManager = new ShopManager(this);
        inventoryManager = new InventoryManager(this);
        inventoryManager.registerPlayerWrapper(new ShopHubPlayerWrapper(this));
        inventoryManager.registerPlayerWrapper(new ShopOrePlayerWrapper(this));
        inventoryManager.registerPlayerWrapper(new ShopMiscPlayerWrapper(this));
        inventoryManager.registerPlayerWrapper(new ShopFarmPlayerWrapper(this));
        inventoryManager.registerPlayerWrapper(new ShopSpawnerPlayerWrapper(this));

    }

    private void registerOptionals() {
        if (configuration.isScoreboardTablistEnabled()) {
            tabHandler = new TabHandler(this, configuration.getScoreboardTablistUpdateRate());
        }
        if (configuration.isHandleChat()) {
            plugin.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }
        if (configuration.isSpawnCannon()) {
            registerCommands(new SpawnCannonCommand(this));
        }
        if (!configuration.isKitmap()) {

        }
    }

    private void registerCommands(Command... commands) {
        try {
            final Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            final boolean accessible = commandMapField.isAccessible();

            commandMapField.setAccessible(true);

            final CommandMap commandMap = (CommandMap) commandMapField.get(getServer());

            Arrays.stream(commands).forEach(command -> commandMap.register(command.getName(), getName(), command));

            commandMapField.setAccessible(accessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("An error occurred while registering commands", e);
        }

    }

    private boolean authenticate() {
        /*boolean authenticated = false;
        String key = "";
        System.out.println("AUTHENTICATING WITH " + key + ".");
        try {
            Socket socket = new Socket("lemin.us", 25600);
            ObjectOutputStream message = new ObjectOutputStream(socket.getOutputStream());
            message.writeChars(key);
            ObjectInputStream reply = new ObjectInputStream(socket.getInputStream());
            authenticated = reply.readBoolean();
            message.close();
            reply.close();
        } catch (IOException e) {
            System.out.println("AUTHENTICATION FAILED.");
            authenticated = true;
            e.printStackTrace();
        }
        return authenticated;*/
        return true;
    }
}
