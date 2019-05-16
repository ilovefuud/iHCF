package us.lemin.hcf.faction.argument;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.concurrent.TimeUnit;

public class FactionRenameArgument extends PlayerSubCommand {

    private static final long FACTION_RENAME_DELAY_MILLIS = TimeUnit.SECONDS.toMillis(15L);
    private static final String FACTION_RENAME_DELAY_WORDS = DurationFormatUtils.formatDurationWords(FACTION_RENAME_DELAY_MILLIS, true, true);

    private final HCF plugin;

    public FactionRenameArgument(HCF plugin) {
        super("rename", "Change the name of your faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"changename", "setname"};
    }


    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <newFactionName>";
    }


    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
            player.sendMessage(ChatColor.RED + "You must be a faction leader to edit the name.");
            return;
        }

        String newName = args[1];

        if (plugin.getConfiguration().getFactionDisallowedNames().contains(newName.toLowerCase())) {
            player.sendMessage(ChatColor.RED + "'" + newName + "' is a blocked faction name.");
            return;
        }

        int value = plugin.getConfiguration().getFactionNameMinCharacters();

        if (newName.length() < value) {
            player.sendMessage(ChatColor.RED + "Faction names must have at least " + value + " characters.");
            return;
        }

        value = plugin.getConfiguration().getFactionNameMaxCharacters();

        if (newName.length() > value) {
            player.sendMessage(ChatColor.RED + "Faction names cannot be longer than " + value + " characters.");
            return;
        }

        if (!JavaUtils.isAlphanumeric(newName)) {
            player.sendMessage(ChatColor.RED + "Faction names may only be alphanumeric.");
            return;
        }

        if (plugin.getFactionManager().getFaction(newName) != null) {
            player.sendMessage(ChatColor.RED + "Faction " + newName + ChatColor.RED + " already exists.");
            return;
        }

        long difference = (playerFaction.lastRenameMillis - System.currentTimeMillis()) + FACTION_RENAME_DELAY_MILLIS;

        if (!player.isOp() && difference > 0L) {
            player.sendMessage(ChatColor.RED + "There is a faction rename delay of " + FACTION_RENAME_DELAY_WORDS + ". Therefore you need to wait another " +
                    DurationFormatUtils.formatDurationWords(difference, true, true) + " to rename your faction.");

            return;
        }

        playerFaction.setName(args[1], player);
    }
}
