package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionMember;
import com.doctordark.hcf.faction.struct.ChatChannel;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.player.rank.Rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Faction argument used to forcefully join {@link Faction}s.
 */
public class FactionForceJoinArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionForceJoinArgument(HCF plugin) {
        super("forcejoin", "Forcefully join a faction.", Rank.ADMIN);
        this.plugin = plugin;
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

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction != null) {
            player.sendMessage(ChatColor.RED + "You are already in a faction.");
            return;
        }

        Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (faction == null) {
            player.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        if (!(faction instanceof PlayerFaction)) {
            player.sendMessage(ChatColor.RED + "You can only join player factions.");
            return;
        }

        playerFaction = (PlayerFaction) faction;
        if (playerFaction.addMember(player, player, player.getUniqueId(), new FactionMember(player, ChatChannel.PUBLIC, Role.MEMBER))) {
            playerFaction.broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + player.getName() + " has forcefully joined the faction.");
        }


    }
}
