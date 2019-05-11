package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.type.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FactionShowArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionShowArgument(HCF plugin) {
        super("show", "Get details about a faction.");


        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " [playerName|factionName]";
    }


    @Override
    public void execute(Player player, Player target, String[] args, String label) {
        Faction playerFaction = null;
        Faction namedFaction;

        if (args.length < 2) {


            namedFaction = plugin.getFactionManager().getPlayerFaction(player);

            if (namedFaction == null) {
                player.sendMessage(ChatColor.RED + "You are not in a faction.");
                return;
            }
        } else {
            namedFaction = plugin.getFactionManager().getFaction(args[1]);
            playerFaction = plugin.getFactionManager().getContainingPlayerFaction(args[1]);

            if (namedFaction == null && playerFaction == null) {
                player.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
                return;
            }
        }

        if (namedFaction != null) {
            namedFaction.printDetails(player);
        }

        if (playerFaction != null && namedFaction != playerFaction) {
            playerFaction.printDetails(player);
        }

        return;
    }
}
