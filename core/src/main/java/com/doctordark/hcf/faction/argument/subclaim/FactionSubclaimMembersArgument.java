package com.doctordark.hcf.faction.argument.subclaim;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.claim.Claim;
import com.doctordark.hcf.faction.claim.Subclaim;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Faction subclaim argument used to list the names of members
 * who have access to a {@link Subclaim}.
 */
public class FactionSubclaimMembersArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionSubclaimMembersArgument(HCF plugin) {
        super("listmembers", "List members of a subclaim");
        this.plugin = plugin;
        this.aliases = new String[]{"listplayers"};
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

        Subclaim subclaim = null;
        for (Claim claim : playerFaction.getClaims()) {
            if ((subclaim = claim.getSubclaim(args[2])) != null) {
                break;
            }
        }

        if (subclaim == null) {
            sender.sendMessage(ChatColor.RED + "Your faction does not have a subclaim named " + args[2] + '.');
            return;
        }

        List<String> memberNames = new ArrayList<>();
        for (UUID accessibleUUID : subclaim.getAccessibleMembers()) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(accessibleUUID);
            String name = target.getName();
            if (name != null) memberNames.add(target.getName());
        }

        sender.sendMessage(ChatColor.YELLOW + "Non-officers accessible of subclaim " +
                subclaim.getName() + " (" + memberNames.size() + "): " +
                ChatColor.AQUA + HCF.COMMA_JOINER.join(memberNames));

    }
}
