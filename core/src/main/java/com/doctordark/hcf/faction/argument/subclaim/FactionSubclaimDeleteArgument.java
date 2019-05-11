package com.doctordark.hcf.faction.argument.subclaim;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.claim.Claim;
import com.doctordark.hcf.faction.claim.Subclaim;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.Iterator;

public class FactionSubclaimDeleteArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionSubclaimDeleteArgument(HCF plugin) {
        super("delete", "Remove a subclaim");
        this.plugin = plugin;
        this.aliases = new String[]{"del", "remove"};
    }

    public String getUsage(String label) {
        return '/' + label + " subclaim " + getName() + " <subclaimName>";
    }


    @Override
    public void execute(Player sender, Player player1, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(sender);

        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.getMember(sender.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "You must be a faction officer to edit subclaims.");
            return;
        }

        for (Claim claim : playerFaction.getClaims()) {
            for (Iterator<Subclaim> iterator = claim.getSubclaims().iterator(); iterator.hasNext(); ) {
                Subclaim subclaim = iterator.next();
                if (subclaim.getName().equalsIgnoreCase(args[2])) {
                    iterator.remove();
                    sender.sendMessage(ChatColor.AQUA + "Removed subclaim named " + subclaim.getName() + '.');
                    return;
                }
            }
        }

        sender.sendMessage(ChatColor.RED + "Your faction does not have a subclaim named " + args[2] + '.');
    }
}
