package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.claim.Claim;
import com.doctordark.hcf.faction.type.ClaimableFaction;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.util.RegionData;
import com.doctordark.util.command.CommandArgument;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used to claim land for other {@link ClaimableFaction}s.
 */
public class FactionEditArgument extends CommandArgument {

    private final HCF plugin;

    public FactionEditArgument(HCF plugin) {
        super("edit", "Edit the land of another nonplayer faction.");
        this.plugin = plugin;
        this.permission = "hcf.oldcommands.faction.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName>";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only executable by players.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        Faction targetFaction = plugin.getFactionManager().getFaction(args[1]);

        if (!(targetFaction instanceof ClaimableFaction)) {
            sender.sendMessage(ChatColor.RED + "Claimable faction named " + args[1] + " not found.");
            return true;
        }

        Player player = (Player) sender;

        RegionData selection = plugin.getRegionManager().getData(player);

        if (selection == null) {
            sender.sendMessage(ChatColor.RED + "You must make a region selection to do this.");
            return true;
        }

        ClaimableFaction claimableFaction = (ClaimableFaction) targetFaction;

        if (claimableFaction.addClaim(new Claim(claimableFaction, selection.getA(), selection.getB()), sender)) {
            sender.sendMessage(ChatColor.YELLOW + "Successfully claimed this land for " + ChatColor.RED + targetFaction.getName() + ChatColor.YELLOW + '.');
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2 || !(sender instanceof Player)) {
            return Collections.emptyList();
        }

        if (args[1].isEmpty()) {
            return null;
        }

        Player player = (Player) sender;
        List<String> results = new ArrayList<>(plugin.getFactionManager().getFactionNameMap().keySet());
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (player.canSee(target) && !results.contains(target.getName())) {
                results.add(target.getName());
            }
        }

        return results;
    }
}
