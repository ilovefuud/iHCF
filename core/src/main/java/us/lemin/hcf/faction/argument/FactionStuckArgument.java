package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.type.StuckTimer;
import us.lemin.hcf.util.DurationFormatter;

/**
 * Faction argument used to teleport to a nearby {@link org.bukkit.Location} safely if stuck.
 */
public class FactionStuckArgument extends SubCommand {

    private final HCF plugin;

    public FactionStuckArgument(HCF plugin) {
        super("stuck", "Teleport to a safe position.");
        this.plugin = plugin;
        this.playerOnly = true;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
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
