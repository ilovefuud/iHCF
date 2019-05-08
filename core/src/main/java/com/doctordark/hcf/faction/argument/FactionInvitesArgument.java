package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionArgument;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import com.google.common.base.Joiner;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Faction argument used to check invites for {@link Faction}s.
 */
public class FactionInvitesArgument extends FactionArgument {

    private static final Joiner JOINER = Joiner.on(ChatColor.WHITE + ", " + ChatColor.GRAY);

    private final HCF plugin;

    public FactionInvitesArgument(HCF plugin) {
        super("invites", "View faction invitations.");
        this.plugin = plugin;
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
        if (!(player instanceof Player)) {
            player.sendMessage(ChatColor.RED + "Only players can have faction invites.");
            return true;
        }

        List<String> receivedInvites = new ArrayList<>();
        for (Faction faction : plugin.getFactionManager().getFactions()) {
            if (faction instanceof PlayerFaction) {
                PlayerFaction targetPlayerFaction = (PlayerFaction) faction;
                if (targetPlayerFaction.getInvitedPlayerNames().contains(player.getName())) {
                    receivedInvites.add(targetPlayerFaction.getDisplayName(player));
                }
            }
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction((Player) player);

        if (playerFaction != null) {
            Set<String> sentInvites = playerFaction.getInvitedPlayerNames();
            player.sendMessage(ChatColor.AQUA + "Sent by " + playerFaction.getDisplayName(player) + ChatColor.AQUA + " (" + sentInvites.size() + ')' + ChatColor.DARK_AQUA + ": " +
                    ChatColor.GRAY + (sentInvites.isEmpty() ? "Your faction has not invited anyone." : JOINER.join(sentInvites) + '.'));
        }

        player.sendMessage(ChatColor.AQUA + "Requested (" + receivedInvites.size() + ')' + ChatColor.DARK_AQUA + ": " +
                ChatColor.GRAY + (receivedInvites.isEmpty() ? "No factions have invited you." : JOINER.join(receivedInvites) + '.'));

        return true;
    }
}
