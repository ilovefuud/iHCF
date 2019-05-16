package us.lemin.hcf.economy.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.hcf.economy.EconomyCommand;

/**
 * A {@link SubCommand} used for viewing the help of King Of The Hill events.
 */
public class EconomyHelpArgument extends SubCommand {

    private final EconomyCommand economyCommand;

    public EconomyHelpArgument(EconomyCommand economyCommand) {
        super("help", "View help about how the economy works", Rank.ADMIN);
        this.economyCommand = economyCommand;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        sender.sendMessage(ChatColor.AQUA + "*** Economy Help ***");
        for (SubCommand argument : economyCommand.getSubCommandMap().values()) {
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
