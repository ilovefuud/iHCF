package us.lemin.hcf.deathban.lives.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;

/**
 * A {@link SubCommand} used to set the lives of {@link Player}s.
 */
public class LivesSetArgument extends SubCommand {

    private final HCF plugin;

    public LivesSetArgument(HCF plugin) {
        super("set", "Set how much lives a player has", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName> <amount>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Integer amount = JavaUtils.tryParseInt(args[2]);

        if (amount == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
            return;
        }

        TaskUtil.runAsync(plugin, () -> {
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                return;
            }
            plugin.getDeathbanManager().setLives(profile.getId(), amount);
            sender.sendMessage(ChatColor.YELLOW + profile.getName() + " now has " + ChatColor.GOLD + amount + ChatColor.YELLOW + " lives.");
        });
    }
}
