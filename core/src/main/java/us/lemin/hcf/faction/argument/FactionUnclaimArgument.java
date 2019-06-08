package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class FactionUnclaimArgument extends SubCommand {

    private final HCF plugin;

    public FactionUnclaimArgument(HCF plugin) {
        super("unclaim", "Unclaims land from your faction.");
        this.plugin = plugin;
        this.playerOnly = true;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " [all]";
    }



    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        FactionMember factionMember = playerFaction.getMember(player);

        if (factionMember.getRole() != Role.LEADER) {
            player.sendMessage(ChatColor.RED + "You must be a faction leader to unclaim land.");
            return;
        }

        Collection<Claim> factionClaims = playerFaction.getClaims();

        if (factionClaims.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Your faction does not own any claims.");
            return;
        }

        // Find out what claims the player wants removed.
        Collection<Claim> removingClaims;
        if (args.length > 1 && args[1].equalsIgnoreCase("all")) {
            removingClaims = new ArrayList<>(factionClaims);
        } else {
            Location location = player.getLocation();
            Claim claimAt = plugin.getFactionManager().getClaimAt(location);
            if (claimAt == null || !factionClaims.contains(claimAt)) {
                player.sendMessage(ChatColor.RED + "Your faction does not own a claim here.");
                return;
            }

            removingClaims = Collections.singleton(claimAt);
        }

        if (!playerFaction.removeClaims(removingClaims, player)) {
            player.sendMessage(ChatColor.RED + "Error when removing claims, please contact an Administrator.");
            return;
        }

        int removingAmount = removingClaims.size();
        playerFaction.broadcast(ChatColor.RED + ChatColor.BOLD.toString() + factionMember.getRole().getAstrix() +
                player.getName() + " has removed " + removingAmount + " claim" + (removingAmount > 1 ? "s" : "") + '.');

        return;
    }
}
