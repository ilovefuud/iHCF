package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionMember;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;

import java.util.Collections;
import java.util.List;

public class FactionForcePromoteArgument extends SubCommand {

    private final HCF plugin;

    public FactionForcePromoteArgument(HCF plugin) {
        super("forcepromote", "Forces the promotion status of a player.", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }

    @Override
    public void execute(CommandSender commandSender, Player player, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getContainingPlayerFaction(args[1]);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        FactionMember factionMember = playerFaction.getMember(args[1]);

        if (factionMember == null) {
            player.sendMessage(ChatColor.RED + "Faction containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        if (factionMember.getRole() != Role.MEMBER) {
            player.sendMessage(ChatColor.RED + factionMember.getName() + " is already a " + factionMember.getRole().getName() + '.');
            return;
        }

        factionMember.setRole(Role.CAPTAIN);
        playerFaction.broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + player.getName() + " has been forcefully assigned as a captain.");

    }
}
