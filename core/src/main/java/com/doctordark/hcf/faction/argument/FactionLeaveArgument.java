package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.struct.Relation;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.UUID;

public class FactionLeaveArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionLeaveArgument(HCF plugin) {
        super("leave", "Leave your current faction.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    @Override
    public void execute(Player player, Player target, String[] args, String label) {
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        UUID uuid = player.getUniqueId();
        if (playerFaction.getMember(uuid).getRole() == Role.LEADER) {
            player.sendMessage(ChatColor.RED + "You cannot leave factions as a leader. Either use " + ChatColor.GOLD + '/' + label + " disband" + ChatColor.RED + " or " +
                    ChatColor.GOLD + '/' + label + " leader" + ChatColor.RED + '.');

            return;
        }

        if (playerFaction.removeMember(player, player, player.getUniqueId(), false, false)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully left the faction.");
            playerFaction.broadcast(Relation.ENEMY.toChatColour() + player.getName() + ChatColor.YELLOW + " has left the faction.");
        }

    }
}
