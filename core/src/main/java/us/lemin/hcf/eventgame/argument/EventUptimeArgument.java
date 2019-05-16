package us.lemin.hcf.eventgame.argument;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.EventTimer;
import us.lemin.hcf.eventgame.faction.EventFaction;
import us.lemin.hcf.util.DateTimeFormats;

/**
 * A {@link SubCommand} argument used for checking the uptime of current event.
 */
public class EventUptimeArgument extends SubCommand {

    private final HCF plugin;

    public EventUptimeArgument(HCF plugin) {
        super("uptime", "Check the uptime of an event");
        this.plugin = plugin;
        this.permission = "hcf.command.event.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        EventTimer eventTimer = plugin.getTimerManager().getEventTimer();

        if (eventTimer.getRemaining() <= 0L) {
            sender.sendMessage(ChatColor.RED + "There is not a running event.");
            return;
        }

        EventFaction eventFaction = eventTimer.getEventFaction();
        sender.sendMessage(ChatColor.YELLOW + "Up-time of " + eventTimer.getName() + " timer" +
                (eventFaction == null ? "" : ": " + ChatColor.BLUE + '(' + eventFaction.getDisplayName(sender) + ChatColor.BLUE + ')') +
                ChatColor.YELLOW + " is " + ChatColor.GRAY + DurationFormatUtils.formatDurationWords(eventTimer.getUptime(), true, true) + ChatColor.YELLOW + ", started at " +
                ChatColor.GOLD + DateTimeFormats.HR_MIN_AMPM_TIMEZONE.format(eventTimer.getStartStamp()) + ChatColor.YELLOW + '.');
    }
}
