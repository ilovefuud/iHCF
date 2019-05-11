package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.timer.type.StuckTimer;
import com.doctordark.hcf.util.DurationFormatter;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

/**
 * Faction argument used to teleport to a nearby {@link org.bukkit.Location} safely if stuck.
 */
public class FactionStuckArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionStuckArgument(HCF plugin) {
        super("stuck", "Teleport to a safe position.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    @Override
    public void execute(Player player, Player player1, String[] strings, String s) {
        if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(ChatColor.RED + "You can only use this command from the overworld.");
            return;
        }

        StuckTimer stuckTimer = plugin.getTimerManager().getStuckTimer();

        if (!stuckTimer.setCooldown(player, player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "Your " + stuckTimer.getName() + ChatColor.RED + " timer is already active.");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + stuckTimer.getName() + ChatColor.YELLOW + " timer has started. " +
                "Teleport will occur in " + ChatColor.AQUA + DurationFormatter.getRemaining(stuckTimer.getRemaining(player), true, false) + ChatColor.YELLOW + ". " +
                "This will cancel if you move more than " + StuckTimer.MAX_MOVE_DISTANCE + " blocks.");
    }
}
