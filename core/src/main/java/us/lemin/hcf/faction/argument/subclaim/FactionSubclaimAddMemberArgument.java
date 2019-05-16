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

public class FactionSubclaimAddMemberArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionSubclaimAddMemberArgument(HCF plugin) {
        super("addmember", "Adds a faction member to a subclaim");
        this.plugin = plugin;
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

        Player player = (Player) sender;
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
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

        FactionMember targetMember = playerFaction.getMember(args[3]);

        if (targetMember == null) {
            sender.sendMessage(ChatColor.RED + "Your faction does not have a member named " + args[3] + '.');
            return;
        }

        if (targetMember.getRole() != Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "Faction officers already bypass subclaim protection.");
            return;
        }

        if (!subclaim.getAccessibleMembers().add(targetMember.getUniqueId())) {
            sender.sendMessage(ChatColor.RED + targetMember.getName() + " already has access to subclaim " + subclaim.getName() + '.');
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Added member " + targetMember.getName() + " to subclaim " + subclaim.getName() + '.');

    }
}
