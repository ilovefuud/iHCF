package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionArgument;
import com.doctordark.hcf.faction.claim.Claim;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class FactionForceUnclaimHereArgument extends FactionArgument {

    private final HCF plugin;

    public FactionForceUnclaimHereArgument(HCF plugin) {
        super("forceunclaimhere", "Forces land unclaim where you are standing.");
        this.plugin = plugin;
        this.permission = "hcf.command.faction.argument." + getName();
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        if (!(player instanceof Player)) {
            player.sendMessage(ChatColor.RED + "Only players can un-claim land from a faction.");
            return true;
        }

        Player player = (Player) player;
        Claim claimAt = plugin.getFactionManager().getClaimAt(player.getLocation());

        if (claimAt == null) {
            player.sendMessage(ChatColor.RED + "There is not a claim at your current position.");
            return true;
        }

        if (claimAt.getFaction().removeClaim(claimAt, player)) {
            player.sendMessage(ChatColor.YELLOW + "Removed claim " + claimAt.getClaimUniqueID().toString() + " owned by " + claimAt.getFaction().getName() + ".");
            return true;
        }

        player.sendMessage(ChatColor.RED + "Failed to remove claim " + claimAt.getClaimUniqueID().toString());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return Collections.emptyList();
    }
}
