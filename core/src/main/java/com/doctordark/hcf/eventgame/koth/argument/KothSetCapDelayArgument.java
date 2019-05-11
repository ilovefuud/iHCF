package com.doctordark.hcf.eventgame.koth.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.eventgame.CaptureZone;
import com.doctordark.hcf.eventgame.faction.KothFaction;
import com.doctordark.hcf.faction.type.Faction;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.misc.JavaUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link SubCommand} used for setting the capture delay of an {@link KothFaction}.
 */
public class KothSetCapDelayArgument extends SubCommand {

    private final HCF plugin;

    public KothSetCapDelayArgument(HCF plugin) {
        super("setcapdelay", "Sets the cap delay of a KOTH");
        this.plugin = plugin;
        this.aliases = new String[]{"setcapturedelay"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <kothName> <capDelay>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[1]);

        if (!(faction instanceof KothFaction)) {
            sender.sendMessage(ChatColor.RED + "There is not a KOTH arena named '" + args[1] + "'.");
            return;
        }

        long duration = JavaUtils.parse(HCF.SPACE_JOINER.join(Arrays.copyOfRange(args, 2, args.length)));

        if (duration == -1L) {
            sender.sendMessage(ChatColor.RED + "Invalid duration, use the correct format: 10m 1s");
            return;
        }

        KothFaction kothFaction = (KothFaction) faction;
        CaptureZone captureZone = kothFaction.getCaptureZone();

        if (captureZone == null) {
            sender.sendMessage(ChatColor.RED + kothFaction.getDisplayName(sender) + ChatColor.RED + " does not have a capture zone.");
            return;
        }

        // Update the remaining time if it is less.
        if (captureZone.isActive() && duration < captureZone.getRemainingCaptureMillis()) {
            captureZone.setRemainingCaptureMillis(duration);
        }

        captureZone.setDefaultCaptureMillis(duration);
        sender.sendMessage(ChatColor.YELLOW + "Set the capture delay of KOTH arena " +
                ChatColor.WHITE + kothFaction.getDisplayName(sender) + ChatColor.YELLOW + " to " +
                ChatColor.WHITE + DurationFormatUtils.formatDurationWords(duration, true, true) + ChatColor.WHITE + '.');

        return;
    }
}
