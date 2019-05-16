package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.Relation;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;

/**
 * Faction argument used to demote players to members in {@link Faction}s.
 */
public class FactionDemoteArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionDemoteArgument(HCF plugin) {
        super("demote", "Demotes a player to a member.");
        this.plugin = plugin;
        this.aliases = new String[]{"uncaptain", "delcaptain", "delofficer"};
    }

    public String getUsage(String label) {
        return '/' + "faction" + ' ' + getName() + " <playerName>";
    }


    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
            player.sendMessage(ChatColor.RED + "You must be a officer to edit the roster.");
            return;
        }

        FactionMember targetMember = playerFaction.getMember(args[1]);

        if (targetMember == null) {
            player.sendMessage(ChatColor.RED + "That player is not in your faction.");
            return;
        }

        if (targetMember.getRole() != Role.CAPTAIN) {
            player.sendMessage(ChatColor.RED + "You can only demote faction captains.");
            return;
        }

        targetMember.setRole(Role.MEMBER);
        playerFaction.broadcast(Relation.MEMBER.toChatColour() + targetMember.getName() + ChatColor.YELLOW + " has been demoted from a faction captain.");
    }
}
