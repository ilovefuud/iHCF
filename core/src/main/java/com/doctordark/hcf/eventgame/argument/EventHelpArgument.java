package com.doctordark.hcf.eventgame.argument;

import com.doctordark.hcf.eventgame.EventExecutor;
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
public class EventHelpArgument extends SubCommand {

    private final EventExecutor eventExecutor;

    public EventHelpArgument(EventExecutor eventExecutor) {
        super("help", "View help about how events work", Rank.ADMIN);
        this.eventExecutor = eventExecutor;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        sender.sendMessage(ChatColor.AQUA + "*** Event Help ***");
        for (SubCommand argument : eventExecutor.getSubCommandMap().values()) {
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
