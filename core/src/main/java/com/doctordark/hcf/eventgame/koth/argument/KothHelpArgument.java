package com.doctordark.hcf.eventgame.koth.argument;

import com.doctordark.hcf.eventgame.koth.KothExecutor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;

/**
 * A {@link SubCommand} used for viewing the help of King Of The Hill events.
 */
public class KothHelpArgument extends SubCommand {

    private final KothExecutor kothExecutor;

    public KothHelpArgument(KothExecutor kothExecutor) {
        super("help", "View help about how KOTH's work", Rank.ADMIN);
        this.kothExecutor = kothExecutor;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        sender.sendMessage(ChatColor.AQUA + "*** KotH Help ***");
        for (SubCommand argument : kothExecutor.getSubCommandMap().values()) {
            if (argument != this) {
                String permission = argument.getPermission();
                if (permission == null || sender.hasPermission(permission) ||
                        (sender instanceof Player && CorePlugin.getInstance().getProfileManager().getProfile((Player) sender).hasRank(requiredRank))) {
                    sender.sendMessage(CC.GRAY + '/' + label + ' ' + argument.getName() + " - " + argument.getDescription() + '.');
                }
            }
        }
        sender.sendMessage(ChatColor.GRAY + "/fac show <kothName> - View information about a KOTH.");
    }
}
