package us.lemin.hcf.economy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.misc.BukkitUtils;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.economy.argument.EconomyGiveArgument;
import us.lemin.hcf.economy.argument.EconomyHelpArgument;
import us.lemin.hcf.economy.argument.EconomySetArgument;
import us.lemin.hcf.economy.argument.EconomyTakeArgument;

import java.util.*;

/**
 * Command used to check a players' balance.
 */
public class EconomyCommand extends PlayerCommand {

    private final HCF plugin;
    @Getter
    private final Map<String, SubCommand> subCommandMap;


    public EconomyCommand(HCF plugin) {
        super("economy");
        setAliases("$", "bal", "balance", "money", "eco");
        this.plugin = plugin;

        Map<String, SubCommand> subCommands = new HashMap<>();

        subCommands.put("give", new EconomyGiveArgument(plugin));
        subCommands.put("take", new EconomyTakeArgument(plugin));
        subCommands.put("set", new EconomySetArgument(plugin));
        subCommands.put("help", new EconomyHelpArgument(this));

        subCommandMap = ImmutableMap.copyOf(subCommands);

    }


    private static final ImmutableList<String> COMPLETIONS_SECOND = ImmutableList.of("add", "set", "take");


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1:
                List<String> results = Lists.newArrayList("top");
                if (sender.hasPermission(getPermission() + ".staff")) {
                    Player senderPlayer = sender instanceof Player ? (Player) sender : null;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (senderPlayer == null || senderPlayer.canSee(player)) {
                            results.add(player.getName());
                        }
                    }
                }

                return BukkitUtils.getCompletions(args, results);
            case 2:
                if (!args[0].equals("top") && sender.hasPermission(getPermission() + ".staff")) {
                    return BukkitUtils.getCompletions(args, COMPLETIONS_SECOND);
                }
            default:
                return Collections.emptyList();
        }
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (!checkSubcommands(sender, args, false)) {
            TaskUtil.runAsync(plugin, () -> {
                ProfileUtil.MojangProfile profile;

                if (args.length > 0 && sender.hasPermission(getPermission() + ".staff")) {
                    profile = ProfileUtil.lookupProfile(args[0]);
                } else if (args.length < 2) {
                    profile = ProfileUtil.lookupProfile(sender.getName());
                } else {
                    sender.sendMessage(ChatColor.RED + "Usage: /" + getLabel() + " <playerName>");
                    return;
                }

                if (profile == null) {
                    if (sender.hasPermission(getPermission() + ".staff")) {
                        checkSubcommands(sender, args, true);
                    } else {
                        sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                    }
                    return;
                }

                UUID uuid = profile.getId();
                int balance = plugin.getEconomyManager().getBalance(uuid);

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.GOLD + (sender.getName().equalsIgnoreCase(profile.getName()) ? "Your balance" : "Balance of " + profile.getName()) + " is " +
                            ChatColor.WHITE + EconomyManager.ECONOMY_SYMBOL + balance + ChatColor.GOLD + '.');

                }
            });
        }
    }

    private boolean checkSubcommands(Player sender, String [] args, boolean help) {
        String arg = args.length < 1 ? null : args[0].toLowerCase();
        if (help) arg = "help";
        if (arg == null) {
            return false;
        }

        SubCommand subCommand = subCommandMap.get(arg);

        boolean subCommandFound = false;

        if (subCommand == null) {
            for (SubCommand loop : subCommandMap.values()) {
                if (loop.getAliases() == null) continue;
                if (Arrays.stream(loop.getAliases())
                        .anyMatch(arg::equalsIgnoreCase)) {
                    Player target = args.length > 0 ? plugin.getServer().getPlayer(args[0]) : null;
                    loop.execute(sender, target, args, getLabel());
                    subCommandFound = true;
                    break;
                }
            }
        } else {
            Player target = args.length > 0 ? plugin.getServer().getPlayer(args[0]) : null;
            subCommand.execute(sender, target, args, getLabel());
            subCommandFound = true;
        }
        return subCommandFound;
    }
}
