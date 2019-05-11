package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionMember;
import com.doctordark.hcf.faction.struct.Relation;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;

import java.util.UUID;

public class FactionPromoteArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionPromoteArgument(HCF plugin) {
        super("promote", "Promotes a player to a captain.");
        this.plugin = plugin;
        this.aliases = new String[]{"captain", "officer", "mod", "moderator"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }


    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        UUID uuid = player.getUniqueId();

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(uuid);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.getMember(uuid).getRole() != Role.LEADER) {
            player.sendMessage(ChatColor.RED + "You must be a faction leader to assign members as a captain.");
            return;
        }

        TaskUtil.runAsync(plugin, () -> {
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            FactionMember targetMember = playerFaction.getMember(profile.getId());

            if (targetMember == null) {
                player.sendMessage(ChatColor.RED + "That player is not in your faction.");
                return;
            }

            if (targetMember.getRole() != Role.MEMBER) {
                player.sendMessage(ChatColor.RED + "You can only assign captains to members, " + targetMember.getName() + " is a " + targetMember.getRole().getName() + '.');
                return;
            }

            Role role = Role.CAPTAIN;
            targetMember.setRole(role);

            playerFaction.broadcast(Relation.MEMBER.toChatColour() + role.getAstrix() + targetMember.getName() + ChatColor.YELLOW + " has been assigned as a faction captain.");

        });
    }
}
