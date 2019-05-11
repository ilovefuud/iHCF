package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.claim.Claim;
import com.doctordark.hcf.faction.type.ClaimableFaction;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.util.RegionData;
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
 * Used to claim land for other {@link ClaimableFaction}s.
 */
public class FactionClaimForArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionClaimForArgument(HCF plugin) {
        super("claimfor", "Claims land for another faction.", Rank.ADMIN);
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

        Faction targetFaction = plugin.getFactionManager().getFaction(args[1]);

        if (!(targetFaction instanceof ClaimableFaction)) {
            player.sendMessage(ChatColor.RED + "Claimable faction named " + args[1] + " not found.");
            return;
        }


        RegionData selection = plugin.getRegionManager().getData(player);

        if (selection == null) {
            player.sendMessage(ChatColor.RED + "You must make a region selection to do this.");
            return;
        }

        ClaimableFaction claimableFaction = (ClaimableFaction) targetFaction;

        if (claimableFaction.addClaim(new Claim(claimableFaction, selection.getA(), selection.getB()), player)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully claimed this land for " + ChatColor.RED + targetFaction.getName() + ChatColor.YELLOW + '.');
        }

    }
}
