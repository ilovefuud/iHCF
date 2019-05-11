package com.doctordark.hcf.deathban.lives.argument;

import com.doctordark.hcf.deathban.lives.LivesExecutor;
import com.doctordark.hcf.eventgame.koth.KothExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

/**
 * A {@link SubCommand} used for viewing the help of King Of The Hill events.
 */
public class LivesHelpArgument extends SubCommand {

    private final LivesExecutor livesExecutor;

    public LivesHelpArgument(LivesExecutor livesExecutor) {
        super("help", "View help about how lives work");
        this.livesExecutor = livesExecutor;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        sender.sendMessage(ChatColor.AQUA + "*** Lives Help ***");
        for (SubCommand argument : livesExecutor.getSubCommandMap().values()) {
            if (argument != this) {
                String permission = argument.getPermission();
                if (permission == null || sender.hasPermission(permission) ||
                        (sender instanceof Player && CorePlugin.getInstance().getProfileManager().getProfile((Player) sender).hasRank(requiredRank))) {
                    sender.sendMessage(CC.GRAY + '/' + label + ' ' + argument.getName() + " - " + argument.getDescription() + '.');
                }
            }
        }
    }
}
