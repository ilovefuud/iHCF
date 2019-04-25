package com.doctordark.hcf.command;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.timer.PlayerTimer;
import com.doctordark.hcf.util.DurationFormatter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;

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