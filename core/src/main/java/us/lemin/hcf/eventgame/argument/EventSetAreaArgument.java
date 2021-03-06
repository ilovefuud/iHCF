package us.lemin.hcf.eventgame.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.cuboid.Cuboid;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.faction.EventFaction;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.util.RegionData;

/**
 * A {@link SubCommand} used for setting the area of an {@link EventFaction}.
 */
public class EventSetAreaArgument extends SubCommand {

    private static final int MIN_EVENT_CLAIM_AREA = 8;

    private final HCF plugin;

    public EventSetAreaArgument(HCF plugin) {
        super("setarea", "Sets the area of an event");
        this.plugin = plugin;
        this.aliases = new String[]{"setclaim", "setclaimarea", "setland"};
        this.permission = "hcf.command.event.argument." + getName();
        this.playerOnly = true;

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <kothName>";
    }

    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }


        RegionData selection = plugin.getRegionManager().getData(player);

        if (plugin.getRegionManager().isDataValid(player)) {
            sender.sendMessage(ChatColor.RED + "You must make a selection to do this.");
            return;
        }
        Cuboid cuboid = new Cuboid(selection.getA(), selection.getB());

        if (cuboid.getWidth() < MIN_EVENT_CLAIM_AREA || cuboid.getLength() < MIN_EVENT_CLAIM_AREA) {
            sender.sendMessage(ChatColor.RED + "Event claim areas must be at least " + MIN_EVENT_CLAIM_AREA + 'x' + MIN_EVENT_CLAIM_AREA + '.');
            return;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[1]);

        if (!(faction instanceof EventFaction)) {
            sender.sendMessage(ChatColor.RED + "There is not an event faction named '" + args[1] + "'.");
            return;
        }

        ((EventFaction) faction).setClaim(cuboid, sender);

        sender.sendMessage(ChatColor.YELLOW + "Updated the claim for event " + faction.getName() + ChatColor.YELLOW + '.');
    }
}
