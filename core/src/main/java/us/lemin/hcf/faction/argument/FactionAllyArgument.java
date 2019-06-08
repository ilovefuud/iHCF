package us.lemin.hcf.faction.argument;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.event.FactionRelationCreateEvent;
import us.lemin.hcf.faction.struct.Relation;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Collection;
import java.util.UUID;

/**
 * Faction argument used to request or accept ally {@link Relation} invitations from a {@link Faction}.
 */

public class FactionAllyArgument extends SubCommand {

    private static final Relation RELATION = Relation.ALLY;

    private final HCF plugin;

    public FactionAllyArgument(HCF plugin) {
        super("ally", "Make an ally pact with other factions.");
        this.plugin = plugin;
        this.aliases = new String[]{"alliance"};
        this.playerOnly = true;

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName>";
    }


    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        if (plugin.getConfiguration().getFactionMaxAllies() <= 0) {
            player.sendMessage(ChatColor.RED + "Allies are disabled this map.");
            return;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage(ChatColor.RED + "You must be an officer to make relation wishes.");
            return;
        }

        Faction containingFaction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (!(containingFaction instanceof PlayerFaction)) {
            player.sendMessage(ChatColor.RED + "Player faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        PlayerFaction targetFaction = (PlayerFaction) containingFaction;

        if (playerFaction == targetFaction) {
            player.sendMessage(ChatColor.RED + "You cannot send " + RELATION.getDisplayName() + ChatColor.RED + " requests to your own faction.");
            return;
        }

        Collection<UUID> allied = playerFaction.getAllied();

        if (allied.size() >= plugin.getConfiguration().getFactionMaxAllies()) {
            player.sendMessage(ChatColor.RED + "Your faction already has reached the alliance limit, which is " + plugin.getConfiguration().getFactionMaxAllies() + '.');
            return;
        }

        if (targetFaction.getAllied().size() >= plugin.getConfiguration().getFactionMaxAllies()) {
            player.sendMessage(targetFaction.getDisplayName(player) + ChatColor.RED + " has reached their maximum alliance limit, which is " +
                    plugin.getConfiguration().getFactionMaxAllies() + '.');

            return;
        }

        if (allied.contains(targetFaction.getUniqueID())) {
            player.sendMessage(ChatColor.RED + "Your faction already is " + RELATION.getDisplayName() + 'd' + ChatColor.RED + " with " +
                    targetFaction.getDisplayName(playerFaction) + ChatColor.RED + '.');

            return;
        }

        // Their faction has already requested us, lets' accept.
        if (targetFaction.getRequestedRelations().remove(playerFaction.getUniqueID()) != null) {
            FactionRelationCreateEvent event = new FactionRelationCreateEvent(playerFaction, targetFaction, RELATION);
            Bukkit.getPluginManager().callEvent(event);

            targetFaction.getRelations().put(playerFaction.getUniqueID(), RELATION);
            targetFaction.broadcast(ChatColor.YELLOW + "Your faction is now " + RELATION.getDisplayName() + 'd' + ChatColor.YELLOW +
                    " with " + playerFaction.getDisplayName(targetFaction) + ChatColor.YELLOW + '.');

            playerFaction.getRelations().put(targetFaction.getUniqueID(), RELATION);
            playerFaction.broadcast(ChatColor.YELLOW + "Your faction is now " + RELATION.getDisplayName() + 'd' + ChatColor.YELLOW +
                    " with " + targetFaction.getDisplayName(playerFaction) + ChatColor.YELLOW + '.');

            return;
        }

        if (playerFaction.getRequestedRelations().putIfAbsent(targetFaction.getUniqueID(), RELATION) != null) {
            player.sendMessage(ChatColor.YELLOW + "Your faction has already requested to " + RELATION.getDisplayName() +
                    ChatColor.YELLOW + " with " + targetFaction.getDisplayName(playerFaction) + ChatColor.YELLOW + '.');

            return;
        }

        // Handle the request.
        playerFaction.broadcast(targetFaction.getDisplayName(playerFaction) + ChatColor.YELLOW + " were informed that you wish to be " + RELATION.getDisplayName() + ChatColor.YELLOW + '.');
        targetFaction.broadcast(playerFaction.getDisplayName(targetFaction) + ChatColor.YELLOW + " has sent a request to be " + RELATION.getDisplayName() + ChatColor.YELLOW +
                ". Use " + plugin.getConfiguration().getRelationColourAlly() + "/faction " + "ally" + ' ' + playerFaction.getName() + ChatColor.YELLOW + " to accept.");


    }
}
