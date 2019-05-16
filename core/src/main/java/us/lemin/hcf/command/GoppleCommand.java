package us.lemin.hcf.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.PlayerTimer;
import us.lemin.hcf.util.DurationFormatter;

/**
 * Command used to check remaining Notch Apple cooldown time for {@link Player}.
 */
public class GoppleCommand extends PlayerCommand {

    private final HCF plugin;

    public GoppleCommand(HCF plugin) {
        super("gopple");
        this.plugin = plugin;
        this.setAliases("gapple");
    }

    @Override
    public void execute(Player player, String[] strings) {

        PlayerTimer timer = plugin.getTimerManager().getGappleTimer();
        long remaining = timer.getRemaining(player);

        if (remaining <= 0L) {
            player.sendMessage(ChatColor.RED + "Your " + timer.getName() + ChatColor.RED + " timer is currently not active.");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Your " + timer.getName() + ChatColor.YELLOW + " timer is active for another "
                + ChatColor.BOLD + DurationFormatter.getRemaining(remaining, true, false) + ChatColor.YELLOW + '.');
    }

}