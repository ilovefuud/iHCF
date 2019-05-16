package us.lemin.hcf.deathban.lives.argument;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.deathban.Deathban;

/**
 * A {@link SubCommand} used to set the base {@link Deathban} time, not including multipliers, etc.
 */
public class LivesSetDeathbanTimeArgument extends SubCommand {

    private final HCF plugin;

    public LivesSetDeathbanTimeArgument(HCF plugin) {
        super("setdeathbantime", "Sets the base deathban time", Rank.ADMIN);
        this.permission = "hcf.command.lives.argument." + getName();
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <time>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Integer duration = JavaUtils.tryParseInt(args[1]);

        if (duration == null) {
            sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
            return;
        }

        plugin.getConfiguration().setDeathbanBaseDurationMinutes(duration);
        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Base death-ban time set to " +
                DurationFormatUtils.formatDurationWords(duration, true, true) + " (not including multipliers, etc).");

    }
}
