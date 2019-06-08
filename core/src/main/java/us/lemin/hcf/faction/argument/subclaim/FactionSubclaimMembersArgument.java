package us.lemin.hcf.faction.argument.subclaim;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.claim.Subclaim;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Faction subclaim argument used to list the names of members
 * who have access to a {@link Subclaim}.
 */
public class FactionSubclaimMembersArgument extends SubCommand {

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
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        TaskUtil.runAsync(plugin, () -> {
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
                ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(accessibleUUID);
                if (profile != null) {
                    String name = profile.getName();
                    if (name != null) memberNames.add(profile.getName());
                }
            }

            sender.sendMessage(ChatColor.YELLOW + "Non-officers accessible of subclaim " +
                    subclaim.getName() + " (" + memberNames.size() + "): " +
                    ChatColor.AQUA + HCF.COMMA_JOINER.join(memberNames));

        });

    }
}
