package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionMember;
import com.doctordark.hcf.faction.struct.ChatChannel;
import com.doctordark.hcf.faction.struct.Relation;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

/**
 * Faction argument used to accept invitations from {@link Faction}s.
 */
public class FactionAcceptArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionAcceptArgument(HCF plugin) {
        super("accept", "Accept a join request from an existing faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"join", "a"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName>";
    }


    @Override
    public void execute(Player player, Player target, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }


        if (plugin.getFactionManager().getPlayerFaction(player) != null) {
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

        PlayerFaction targetFaction = (PlayerFaction) faction;

        if (targetFaction.getMembers().size() >= plugin.getConfiguration().getFactionMaxMembers()) {
            player.sendMessage(faction.getDisplayName(player) + ChatColor.RED + " is full. Faction limits are at " + plugin.getConfiguration().getFactionMaxMembers() + '.');
            return;
        }

        if (!targetFaction.isOpen() && !targetFaction.getInvitedPlayerNames().contains(player.getName())) {
            player.sendMessage(ChatColor.RED + faction.getDisplayName(player) + ChatColor.RED + " has not invited you.");
            return;
        }

        if (targetFaction.addMember(player, player, player.getUniqueId(), new FactionMember(player, ChatChannel.PUBLIC, Role.MEMBER))) {
            targetFaction.broadcast(Relation.MEMBER.toChatColour() + player.getName() + ChatColor.YELLOW + " has joined the faction.");
        }
    }
}
