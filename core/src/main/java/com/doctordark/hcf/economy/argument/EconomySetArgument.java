package com.doctordark.hcf.economy.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.economy.EconomyManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;

public class EconomySetArgument extends SubCommand {

    private final HCF plugin;

    public EconomySetArgument(HCF plugin) {
        super("set", "Set money of a player.", Rank.ADMIN);
        this.aliases = new String[]{"negate", "minus", "subtract", "remove"};
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {

        TaskUtil.runAsync(plugin, () -> {
            if (args.length < 3) {
                sender.sendMessage(CC.RED + "Usage: /" + label + " give <player> <amount>" );
            }
            Integer amount = JavaUtils.tryParseInt(args[2]);
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            if (amount == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
                return;
            }

            int newBalance = plugin.getEconomyManager().setBalance(profile.getId(), amount);
            sender.sendMessage(ChatColor.YELLOW + "Set balance of " + profile.getName() + " to " + EconomyManager.ECONOMY_SYMBOL + JavaUtils.format(newBalance) + '.');
        });


    }
}
