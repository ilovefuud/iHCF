package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.misc.JavaUtils;

/**
 * Faction argument used to create a new {@link Faction}.
 */
public class FactionCreateArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionCreateArgument(HCF plugin) {
        super("create", "Create a faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"make", "define"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName>";
    }


    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        String name = args[1];

        if (plugin.getConfiguration().getFactionDisallowedNames().contains(name.toLowerCase())) {
            player.sendMessage(ChatColor.RED + "'" + name + "' is a blocked faction name.");
            return;
        }

        int value = plugin.getConfiguration().getFactionNameMinCharacters();

        if (name.length() < value) {
            player.sendMessage(ChatColor.RED + "Faction names must have at least " + value + " characters.");
            return;
        }

        value = plugin.getConfiguration().getFactionNameMaxCharacters();

        if (name.length() > value) {
            player.sendMessage(ChatColor.RED + "Faction names cannot be longer than " + value + " characters.");
            return;
        }

        if (!JavaUtils.isAlphanumeric(name)) {
            player.sendMessage(ChatColor.RED + "Faction names may only be alphanumeric.");
            return;
        }

        if (plugin.getFactionManager().getFaction(name) != null) {
            player.sendMessage(ChatColor.RED + "Faction '" + name + "' already exists.");
            return;
        }

        if (plugin.getFactionManager().getPlayerFaction((Player) player) != null) {
            player.sendMessage(ChatColor.RED + "You are already in a faction.");
            return;
        }

        plugin.getFactionManager().createFaction(new PlayerFaction(name), player);
    }
}
