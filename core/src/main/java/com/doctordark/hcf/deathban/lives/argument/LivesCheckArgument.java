package com.doctordark.hcf.deathban.lives.argument;

import com.doctordark.hcf.HCF;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;

/**
 * A {@link SubCommand} used to check how many lives a {@link Player} has.
 */
public class LivesCheckArgument extends SubCommand {

    private final HCF plugin;

    public LivesCheckArgument(HCF plugin) {
        super("check", "Check how much lives a player has", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " [playerName]";
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (player != null) {
            int targetLives = plugin.getDeathbanManager().getLives(player.getUniqueId());
            sender.sendMessage(player.getName() + ChatColor.YELLOW + " has " + ChatColor.AQUA + targetLives + ChatColor.YELLOW + ' ' + (targetLives == 1 ? "life" : "lives") + '.');
            return;
        }

        // This is so fucking gross
        TaskUtil.runAsync(plugin, () -> {
            ProfileUtil.MojangProfile profile;
            if (args.length > 1) {
                profile = ProfileUtil.lookupProfile(args[1]);
            } else if (sender instanceof Player) {
                profile = ProfileUtil.lookupProfile(sender.getName());
            } else {
                sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
                return;
            }
            if (profile == null) {
                sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            } else {
                int targetLives = plugin.getDeathbanManager().getLives(profile.getId());
                sender.sendMessage(profile.getName() + ChatColor.YELLOW + " has " + ChatColor.AQUA + targetLives + ChatColor.YELLOW + ' ' + (targetLives == 1 ? "life" : "lives") + '.');
            }
        });
    }
}
