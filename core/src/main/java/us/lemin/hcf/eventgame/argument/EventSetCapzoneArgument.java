package us.lemin.hcf.eventgame.argument;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.cuboid.Cuboid;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.CaptureZone;
import us.lemin.hcf.eventgame.faction.CapturableFaction;
import us.lemin.hcf.eventgame.faction.ConquestFaction;
import us.lemin.hcf.eventgame.faction.EventFaction;
import us.lemin.hcf.eventgame.faction.KothFaction;
import us.lemin.hcf.eventgame.tracker.ConquestTracker;
import us.lemin.hcf.eventgame.tracker.KothTracker;
import us.lemin.hcf.faction.FactionManager;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.util.RegionData;

import java.util.Collection;

/**
 * An {@link PlayerSubCommand} used for setting the {@link CaptureZone}s of an {@link EventFaction}.
 */
public class EventSetCapzoneArgument extends PlayerSubCommand {

    private final HCF plugin;

    public EventSetCapzoneArgument(HCF plugin) {
        super("setcapzone", "Sets the capture zone of an event");
        this.plugin = plugin;
        this.aliases = new String[]{"setcapturezone", "setcap", "setcappoint", "setcapturepoint", "setcappoint"};
        this.permission = "hcf.command.event.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <eventName>";
    }

    @Override
    public void execute(Player sender, Player target, String[] args, String label) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }


        RegionData selection = plugin.getRegionManager().getData(sender);

        if (plugin.getRegionManager().isDataValid(sender)) {
            sender.sendMessage(ChatColor.RED + "You must make a selection to do this.");
            return;
        }
        Cuboid cuboid = new Cuboid(selection.getA(), selection.getB());
        if (cuboid.getWidth() < CaptureZone.MINIMUM_SIZE_AREA || cuboid.getLength() < CaptureZone.MINIMUM_SIZE_AREA) {
            sender.sendMessage(ChatColor.RED + "Capture zones must be at least " + CaptureZone.MINIMUM_SIZE_AREA + 'x' + CaptureZone.MINIMUM_SIZE_AREA + '.');
            return;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[1]);

        if (!(faction instanceof CapturableFaction)) {
            sender.sendMessage(ChatColor.RED + "There is not a capturable faction named '" + args[1] + "'.");
            return;
        }

        CapturableFaction capturableFaction = (CapturableFaction) faction;
        Collection<Claim> claims = capturableFaction.getClaims();

        if (claims.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Capture zones can only be inside the event claim.");
            return;
        }

        Claim claim = new Claim(faction, cuboid);

        World world = claim.getWorld();
        int minimumX = claim.getMinimumX();
        int maximumX = claim.getMaximumX();

        int minimumZ = claim.getMinimumZ();
        int maximumZ = claim.getMaximumZ();

        FactionManager factionManager = plugin.getFactionManager();
        for (int x = minimumX; x <= maximumX; x++) {
            for (int z = minimumZ; z <= maximumZ; z++) {
                Faction factionAt = factionManager.getFactionAt(world, x, z);
                if (factionAt != capturableFaction) {
                    sender.sendMessage(ChatColor.RED + "Capture zones can only be inside the event claim.");
                    return;
                }
            }
        }

        CaptureZone captureZone;
        if (capturableFaction instanceof ConquestFaction) {
            if (args.length < 3) {
                sender.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + getName() + ' ' + faction.getName() + " <red|blue|green|yellow>");
                return;
            }

            ConquestFaction conquestFaction = (ConquestFaction) capturableFaction;
            ConquestFaction.ConquestZone conquestZone = ConquestFaction.ConquestZone.getByName(args[2]);
            if (conquestZone == null) {
                sender.sendMessage(ChatColor.RED + "There is no conquest zone named '" + args[2] + "'.");
                sender.sendMessage(ChatColor.RED + "Did you mean?: " + HCF.COMMA_JOINER.join(ConquestFaction.ConquestZone.getNames()));
                return;
            }

            captureZone = new CaptureZone(conquestZone.getName(), conquestZone.getColor().toString(), claim, ConquestTracker.DEFAULT_CAP_MILLIS);
            conquestFaction.setZone(conquestZone, captureZone);
        } else if (capturableFaction instanceof KothFaction) {
            ((KothFaction) capturableFaction).setCaptureZone(captureZone = new CaptureZone(capturableFaction.getName(), claim, KothTracker.DEFAULT_CAP_MILLIS));
        } else {
            sender.sendMessage(ChatColor.RED + "Can only set capture zones for Conquest or KOTH factions.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Set capture zone " + captureZone.getDisplayName() + ChatColor.YELLOW + " for faction " + faction.getName() + ChatColor.YELLOW + '.');
    }
}
