package us.lemin.hcf.faction.argument.subclaim;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.claim.Subclaim;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

public class FactionSubclaimRenameArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionSubclaimRenameArgument(HCF plugin) {
        super("rename", "Renames a subclaim");
        this.plugin = plugin;
        this.aliases = new String[]{"changename"};
    }

    public String getUsage(String label) {
        return '/' + label + " subclaim " + getName() + " <oldSubclaimName> <newSubClaimName";
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
            Subclaim next = claim.getSubclaim(args[2]);
            if (next != null) {
                subclaim = next;
                break;
            }
        }

        if (subclaim == null) {
            sender.sendMessage(ChatColor.RED + "Your faction does not have a subclaim named " + args[2] + '.');
            return;
        }

        String oldName = subclaim.getName();
        String newName = args[3];
        subclaim.setName(newName);

        sender.sendMessage(ChatColor.YELLOW + "Renamed subclaim " + oldName + " to " + newName + '.');
    }
}
