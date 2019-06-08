package us.lemin.hcf.command.pvptimer.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.type.InvincibilityTimer;

public class PvPTimerEnableCommand extends SubCommand {

    private final HCF plugin;

    public PvPTimerEnableCommand( HCF plugin) {
        super("enable");
        this.plugin = plugin;
        this.aliases = new String[] {"off", "remove"};
    }

    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        InvincibilityTimer pvpTimer = plugin.getTimerManager().getInvincibilityTimer();

        if (pvpTimer.getRemaining(player) <= 0L) {
            player.sendMessage(ChatColor.RED + "Your " + pvpTimer.getName() + ChatColor.RED + " timer is currently not active.");
            return;
        }

        player.sendMessage(ChatColor.YELLOW + "Your " + pvpTimer.getName() + ChatColor.YELLOW + " timer is now off.");
        pvpTimer.clearCooldown(player);
    }
}
