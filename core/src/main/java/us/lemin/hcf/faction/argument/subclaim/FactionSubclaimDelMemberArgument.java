package us.lemin.hcf.faction.argument.subclaim;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.claim.Subclaim;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

/**
 * Faction subclaim argument used to remove a members access to a {@link Subclaim}.
 */
public class FactionSubclaimDelMemberArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionSubclaimDelMemberArgument(HCF plugin) {
        super("delmember", "Removes a member from a subclaim");
        this.plugin = plugin;
        this.aliases = new String[]{"addplayer", "deleteplayer", "removeplayer", "delplayer", "revoke"};
    }

    public String getUsage(String label) {
        return '/' + label + " subclaim " + getName() + " <subclaimName> <memberName>";
    }

    @Override
    public void execute(Player sender, Player player1, String[] args, String label) {
        if (args.length < 4) {
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

        FactionMember factionTarget = playerFaction.getMember(args[3]);

        if (factionTarget == null) {
            sender.sendMessage(ChatColor.RED + "Your faction does not have a member named " + args[3] + '.');
            return;
        }

        if (factionTarget.getRole() != Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "Faction officers already bypass subclaim protection.");
            return;
        }

        if (!subclaim.getAccessibleMembers().remove(factionTarget.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + factionTarget.getName() + " will continue to not have access to subclaim " + subclaim.getName() + '.');
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Removed member " + factionTarget.getName() + " from subclaim " + subclaim.getName() + '.');

    }
}
