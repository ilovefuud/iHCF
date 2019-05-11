package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionMember;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.PlayerFaction;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FactionUninviteArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionUninviteArgument(HCF plugin) {
        super("uninvite", "Revoke an invitation to a player.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <all|playerName>";
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

        FactionMember factionMember = playerFaction.getMember(player);

        if (factionMember.getRole() == Role.MEMBER) {
            player.sendMessage(ChatColor.RED + "You must be a faction officer to un-invite players.");
            return;
        }

        Set<String> invitedPlayerNames = playerFaction.getInvitedPlayerNames();

        if (args[1].equalsIgnoreCase("all")) {
            invitedPlayerNames.clear();
            player.sendMessage(ChatColor.YELLOW + "You have cleared all pending invitations.");
            return;
        }

        if (!invitedPlayerNames.remove(args[1])) {
            player.sendMessage(ChatColor.RED + "There is not a pending invitation for " + args[1] + '.');
            return;
        }

        playerFaction.broadcast(ChatColor.YELLOW + factionMember.getRole().getAstrix() + player.getName() + " has uninvited " +
                plugin.getConfiguration().getRelationColourEnemy() + args[1] + ChatColor.YELLOW + " from the faction.");

    }
}
