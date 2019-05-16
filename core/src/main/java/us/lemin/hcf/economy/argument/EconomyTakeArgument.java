package us.lemin.hcf.economy.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.economy.EconomyManager;

public class EconomyTakeArgument extends SubCommand {

    private final HCF plugin;

    public EconomyTakeArgument(HCF plugin) {
        super("take", "Take money from a player.", Rank.ADMIN);
        this.aliases = new String[]{"negate", "minus", "subtract", "remove"};
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        TaskUtil.runAsync(plugin, () -> {
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            if (args.length < 3) {
                sender.sendMessage(CC.RED + "Usage: /" + label + " give <player> <amount>" );
            }
            Integer amount = JavaUtils.tryParseInt(args[2]);
            if (amount == null) {
                sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
                return;
            }

            int newBalance = plugin.getEconomyManager().subtractBalance(profile.getId(), amount);

            sender.sendMessage(new String[]{
                    ChatColor.YELLOW + "Taken " + EconomyManager.ECONOMY_SYMBOL + JavaUtils.format(amount) + " from balance of " + profile.getName() + '.',
                    ChatColor.YELLOW + "Balance of " + profile.getName() + " is now " + EconomyManager.ECONOMY_SYMBOL + newBalance + '.'
            });
        });


    }
}
