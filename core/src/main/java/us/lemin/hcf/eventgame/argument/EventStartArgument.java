package us.lemin.hcf.eventgame.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.faction.EventFaction;
import us.lemin.hcf.faction.type.Faction;

/**
 * A {@link SubCommand} used for starting an event.
 */
public class EventStartArgument extends SubCommand {

    private final HCF plugin;

    public EventStartArgument(HCF plugin) {
        super("start", "Starts an event");
        this.plugin = plugin;
        this.aliases = new String[]{"begin"};
        this.permission = "hcf.command.event.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <eventName>";
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[1]);

        if (!(faction instanceof EventFaction)) {
            sender.sendMessage(ChatColor.RED + "There is not an event faction named '" + args[1] + "'.");
            return;
        }

        if (plugin.getTimerManager().getEventTimer().tryContesting(((EventFaction) faction), sender)) {
            sender.sendMessage(ChatColor.YELLOW + "Successfully contested " + faction.getName() + '.');
        }
    }
}
