package us.lemin.hcf.deathban.lives.argument;

import com.google.common.base.Strings;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.deathban.Deathban;
import us.lemin.hcf.util.DateTimeFormats;

/**
 * A {@link SubCommand} used to check the {@link Deathban} of a {@link Player}.
 */
public class LivesCheckDeathbanArgument extends SubCommand {

    private final HCF plugin;

    public LivesCheckDeathbanArgument(HCF plugin) {
        super("checkdeathban", "Check the deathban cause of player", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }


        Player target = plugin.getServer().getPlayer(args[1]);
        TaskUtil.runAsync(plugin, () -> {
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[1] + ChatColor.GOLD + "' not found.");
                return;
            }

            Deathban deathban = plugin.getUserManager().getUser(profile.getId()).getDeathban();

            if (deathban == null || !deathban.isActive()) {
                sender.sendMessage(ChatColor.RED + profile.getName() + " is not death-banned.");
                return;
            }

            sender.sendMessage(ChatColor.DARK_AQUA + "Deathban cause of " + profile.getName() + '.');
            sender.sendMessage(ChatColor.GRAY + " Time: " + DateTimeFormats.HR_MIN.format(deathban.getCreationMillis()));
            sender.sendMessage(ChatColor.GRAY + " Duration: " + DurationFormatUtils.formatDurationWords(deathban.getInitialDuration(), true, true));

            Location location = deathban.getDeathPoint();
            if (location != null) {
                sender.sendMessage(ChatColor.GRAY + " Location: (" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ") - " + location.getWorld().getName());
            }

            sender.sendMessage(ChatColor.GRAY + " Reason: [" + Strings.nullToEmpty(deathban.getReason()) + ChatColor.GREEN + "]");
        });
    }
}
