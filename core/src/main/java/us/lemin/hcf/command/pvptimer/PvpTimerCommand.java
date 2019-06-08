package us.lemin.hcf.command.pvptimer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.misc.BukkitUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.command.pvptimer.impl.PvPTimerEnableCommand;
import us.lemin.hcf.command.pvptimer.impl.PvPTimerRemainingCommand;
import us.lemin.hcf.timer.type.InvincibilityTimer;

import java.util.*;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class PvpTimerCommand extends PlayerCommand {

    private final HCF plugin;
    private final Map<String, SubCommand> subCommandMap;


    public PvpTimerCommand(HCF plugin) {
        super("pvptimer");
        Map<String, SubCommand> subCommands = new HashMap<>();


        subCommands.put("enable", new PvPTimerEnableCommand(plugin));
        subCommands.put("remaining", new PvPTimerRemainingCommand(plugin));

        subCommandMap = ImmutableMap.copyOf(subCommands);

        this.plugin = plugin;
    }

    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("enable", "time");

    /**
     * Prints the usage of this command to a sender.
     *
     * @param sender the sender to print for
     */
    private void printUsage(CommandSender sender, InvincibilityTimer pvpTimer) {
        sender.sendMessage(ChatColor.GOLD + "*** " + pvpTimer.getName() + " Timer Help ***");
        sender.sendMessage(ChatColor.GRAY + "/" + getName() + " enable - Removes your " + pvpTimer.getName() + ChatColor.GRAY + " timer.");
        sender.sendMessage(ChatColor.GRAY + "/" + getName() + " time - Check remaining " + pvpTimer.getName() + ChatColor.GRAY + " time.");
        sender.sendMessage(ChatColor.GRAY + "/lives - Life and deathban related commands.");
    }


    @Override
    public void execute(Player sender, String[] args) {
        String arg = args.length < 1 ? "help" : args[0].toLowerCase();
        SubCommand subCommand = subCommandMap.get(arg);

        InvincibilityTimer pvpTimer = plugin.getTimerManager().getInvincibilityTimer();
        Player target = args.length > 1 ? plugin.getServer().getPlayer(args[1]) : null;

        if (subCommand == null) {
            for (SubCommand loop : subCommandMap.values()) {
                if (loop.getAliases() == null) continue;
                if (Arrays.stream(loop.getAliases())
                        .anyMatch(arg::equalsIgnoreCase)) {
                    loop.verify(sender, target, args, getLabel());
                    return;
                }
            }
            printUsage(sender, pvpTimer);
        } else {
            subCommand.verify(sender, target, args, getLabel());
        }


    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return args.length == 1 ? BukkitUtils.getCompletions(args, COMPLETIONS) : Collections.emptyList();
    }
}
