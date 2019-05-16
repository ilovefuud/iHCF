package us.lemin.hcf.faction.argument;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.misc.BukkitUtils;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.faction.FactionExecutor;
import us.lemin.hcf.faction.type.Faction;

/**
 * Faction argument used to show help on how to use {@link Faction}s.
 */
public class FactionHelpArgument extends SubCommand {

    private static final int HELP_PER_PAGE = 10;

    private ImmutableMultimap<Integer, String> pages;
    private final FactionExecutor executor;


    public FactionHelpArgument(FactionExecutor executor) {
        super("help", "View help on how to use factions.");
        this.executor = executor;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    private void showPage(CommandSender player, String label, int pageNumber) {
        // Create the multimap.
        if (pages == null) {
            boolean isPlayer = player instanceof Player;
            int val = 1;
            int count = 0;
            Multimap<Integer, String> pages = ArrayListMultimap.create();

            CoreProfile profile = isPlayer ? CorePlugin.getInstance().getProfileManager().getProfile((Player) player) : null;

            for (SubCommand argument : executor.subCommandMap.values()) {
                if (argument == this) continue;

                // Check the permission and if the player can access.
                // String permission = argument.getPermission();
                // if (permission != null && !player.hasPermission(permission)) continue;

                if (isPlayer && !profile.hasRank(argument.getRequiredRank())) continue;
                if (argument.getClass().isAssignableFrom(PlayerSubCommand.class) && !isPlayer) continue;

                count++;
                pages.get(val).add(ChatColor.BLUE + "/" + label + ' ' + argument.getName() + " > " + ChatColor.GRAY + argument.getDescription());
                if (count % HELP_PER_PAGE == 0) {
                    val++;
                }
            }

            // Finally assign it.
            this.pages = ImmutableMultimap.copyOf(pages);
        }

        int totalPageCount = (pages.size() / HELP_PER_PAGE) + 1;

        if (pageNumber < 1) {
            player.sendMessage(ChatColor.RED + "You cannot view a page less than 1.");
            return;
        }

        if (pageNumber > totalPageCount) {
            player.sendMessage(ChatColor.RED + "There are only " + totalPageCount + " pages.");
            return;
        }

        player.sendMessage(ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
        player.sendMessage(ChatColor.GOLD + " Faction Help " + ChatColor.WHITE + "(Page " + pageNumber + '/' + totalPageCount + ')');
        for (String message : pages.get(pageNumber)) {
            player.sendMessage("  " + message);
        }

        player.sendMessage(ChatColor.GOLD + " You are currently on " + ChatColor.WHITE + "Page " + pageNumber + '/' + totalPageCount + ChatColor.GOLD + '.');
        player.sendMessage(ChatColor.GOLD + " To view other pages, use " + ChatColor.YELLOW + '/' + label + ' ' + getName() + " <page#>" + ChatColor.GOLD + '.');
        player.sendMessage(ChatColor.GOLD + BukkitUtils.STRAIGHT_LINE_DEFAULT);
    }

    @Override
    public void execute(CommandSender commandSender, Player target, String[] args, String label) {
        if (args.length < 2) {
            showPage(commandSender, "faction", 1);
            return;
        }

        Integer page = JavaUtils.tryParseInt(args[1]);

        if (page == null) {
            commandSender.sendMessage(ChatColor.RED + "'" + args[1] + "' is not a valid number.");
            return;
        }

        showPage(commandSender, label, page);
    }
}
