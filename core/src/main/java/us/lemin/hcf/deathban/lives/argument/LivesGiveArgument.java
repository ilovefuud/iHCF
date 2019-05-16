package us.lemin.hcf.deathban.lives.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;

/**
 * A {@link SubCommand} used to give lives to {@link Player}s.
 */
public class LivesGiveArgument extends SubCommand {

    private final HCF plugin;

    public LivesGiveArgument(HCF plugin) {
        super("give", "Give lives to a player");
        this.plugin = plugin;
        this.aliases = new String[]{"transfer", "send", "pay", "add"};
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

        if (amount <= 0) {
            sender.sendMessage(ChatColor.RED + "The amount of lives must be positive.");
            return;
        }

        TaskUtil.runAsync(plugin, () -> {
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                return;
            } else {
                Player onlineTarget = plugin.getServer().getPlayer(profile.getId());
                if (sender instanceof Player) {
                    Player onlinePlayer = (Player) sender;
                    int ownedLives = plugin.getDeathbanManager().getLives(player.getUniqueId());

                    if (amount > ownedLives) {
                        sender.sendMessage(ChatColor.RED + "You tried to give " + profile.getName() + ' ' + CC.YELLOW + amount + CC.RED + " lives, but you only have " + CC.YELLOW + ownedLives + CC.RED + '.');
                    }
                }

                plugin.getDeathbanManager().addLives(profile.getId(), amount);
                sender.sendMessage(ChatColor.YELLOW + "You have sent " + ChatColor.GOLD + profile.getName() + ChatColor.YELLOW + ' ' + amount + ' ' + (amount > 1 ? "lives" : "life") + ChatColor.YELLOW + '.');
                if (onlineTarget != null) {
                    onlineTarget.sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.YELLOW + " has sent you " + ChatColor.GOLD + amount + ' ' + (amount > 1 ? "lives" : "life") + ChatColor.YELLOW + '.');
                }
            }
        });
    }
}
