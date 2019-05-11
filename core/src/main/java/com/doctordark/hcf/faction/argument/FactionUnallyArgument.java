package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.event.FactionRelationRemoveEvent;
import com.doctordark.hcf.faction.struct.Relation;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class FactionUnallyArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionUnallyArgument(HCF plugin) {
        super("unally", "Remove an ally pact with other factions.");
        this.plugin = plugin;
        this.aliases = new String[]{"unalliance", "neutral"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <all|factionName>";
    }



    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (plugin.getConfiguration().getFactionMaxAllies() <= 0) {
            player.sendMessage(ChatColor.RED + "Allies are disabled this map.");
            return;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage(ChatColor.RED + "You must be a faction officer to edit relations.");
            return;
        }

        Relation relation = Relation.ALLY;
        Collection<PlayerFaction> targetFactions = new HashSet<>();

        if (args[1].equalsIgnoreCase("all")) {
            Collection<PlayerFaction> allies = playerFaction.getAlliedFactions();
            if (allies.isEmpty()) {
                player.sendMessage(ChatColor.RED + "Your faction has no allies.");
                return;
            }

            targetFactions.addAll(allies);
        } else {
            Faction searchedFaction = plugin.getFactionManager().getContainingFaction(args[1]);

            if (!(searchedFaction instanceof PlayerFaction)) {
                player.sendMessage(ChatColor.RED + "Player faction named or containing member with IGN or UUID " + args[1] + " not found.");
                return;
            }

            targetFactions.add((PlayerFaction) searchedFaction);
        }

        for (PlayerFaction targetFaction : targetFactions) {
            if (playerFaction.getRelations().remove(targetFaction.getUniqueID()) == null || targetFaction.getRelations().remove(playerFaction.getUniqueID()) == null) {
                player.sendMessage(ChatColor.RED + "Your faction is not " + relation.getDisplayName() + ChatColor.RED + " with " + targetFaction.getDisplayName(playerFaction) + ChatColor.RED + '.');
                return;
            }

            FactionRelationRemoveEvent event = new FactionRelationRemoveEvent(playerFaction, targetFaction, Relation.ALLY);
            Bukkit.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                player.sendMessage(ChatColor.RED + "Could not drop " + relation.getDisplayName() + " with " + targetFaction.getDisplayName(playerFaction) + ChatColor.RED + ".");
                return;
            }

            // Inform the affected factions.
            playerFaction.broadcast(ChatColor.YELLOW + "Your faction has dropped its " + relation.getDisplayName() + ChatColor.YELLOW + " with " +
                    targetFaction.getDisplayName(playerFaction) + ChatColor.YELLOW + '.');

            targetFaction.broadcast(ChatColor.YELLOW + playerFaction.getDisplayName(targetFaction) + ChatColor.YELLOW + " has dropped their " + relation.getDisplayName() +
                    ChatColor.YELLOW + " with your faction.");
        }

    }
}
