package com.doctordark.hcf.eventgame.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.eventgame.EventTimer;
import com.doctordark.hcf.faction.type.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;

/**
 * A {@link SubCommand} used for cancelling the current running event.
 */
public class EventCancelArgument extends SubCommand {

    private final HCF plugin;

    public EventCancelArgument(HCF plugin) {
        super("cancel", "Cancels a running event");
        this.aliases = new String[]{"stop", "end"};
        this.plugin = plugin;
        this.permission = "hcf.command.event.argument." + getName();
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    @Override
    public void execute(CommandSender commandSender, Player player, String[] strings, String s) {
        EventTimer eventTimer = plugin.getTimerManager().getEventTimer();
        Faction eventFaction = eventTimer.getEventFaction();

        if (!eventTimer.clearCooldown()) {
            commandSender.sendMessage(ChatColor.RED + "There is not a running event.");
            return;
        }

        Bukkit.broadcastMessage(commandSender.getName() + ChatColor.YELLOW + " has cancelled " + (eventFaction == null ? "the active event" : eventFaction.getName() + ChatColor.YELLOW) + ".");
        return;
    }
}
