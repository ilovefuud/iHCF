package com.doctordark.hcf.command;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.timer.PlayerTimer;
import com.doctordark.hcf.util.DurationFormatter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;

import java.util.Collections;
import java.util.List;

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

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }

        Player player = (Player) sender;

        PlayerTimer timer = plugin.getTimerManager().getGappleTimer();
        long remaining = timer.getRemaining(player);

        if (remaining <= 0L) {
            sender.sendMessage(ChatColor.RED + "Your " + timer.getName() + ChatColor.RED + " timer is currently not active.");
            return true;
        }

        sender.sendMessage(ChatColor.YELLOW + "Your " + timer.getName() + ChatColor.YELLOW + " timer is active for another "
                + ChatColor.BOLD + DurationFormatter.getRemaining(remaining, true, false) + ChatColor.YELLOW + '.');

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
