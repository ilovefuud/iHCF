package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.type.ClaimableFaction;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Collection;

/**
 * Faction argument used to check {@link Claim}s made by {@link Faction}s.
 */
public class FactionClaimsArgument extends SubCommand {

    private final HCF plugin;

    public FactionClaimsArgument(HCF plugin) {
        super("claims", "View all claims for a faction.");
        this.plugin = plugin;
        this.playerOnly = true;

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " [factionName]";
    }

    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        PlayerFaction selfFaction = plugin.getFactionManager().getPlayerFaction(player);
        ClaimableFaction targetFaction;
        if (args.length < 2) {


            if (selfFaction == null) {
                player.sendMessage(ChatColor.RED + "You are not in a faction.");
                return;
            }

            targetFaction = selfFaction;
        } else {
            Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

            if (faction == null) {
                player.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
                return;
            }

            if (!(faction instanceof ClaimableFaction)) {
                player.sendMessage(ChatColor.RED + "You can only check the claims of factions that can have claims.");
                return;
            }

            targetFaction = (ClaimableFaction) faction;
        }

        Collection<Claim> claims = targetFaction.getClaims();

        if (claims.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Faction " + targetFaction.getDisplayName(player) + ChatColor.RED + " has no claimed land.");
            return;
        }

        if (!player.isOp() && (targetFaction instanceof PlayerFaction && ((PlayerFaction) targetFaction).getHome() == null)) {
            if (selfFaction != targetFaction) {
                player.sendMessage(ChatColor.RED + "You cannot view the claims of " + targetFaction.getDisplayName(player) + ChatColor.RED + " because their home is unset.");
                return;
            }
        }

        player.sendMessage(ChatColor.GOLD + "Claims of " + targetFaction.getDisplayName(player) + ChatColor.GOLD + " (" + claims.size() + "):");

        for (Claim claim : claims) {
            player.sendMessage(ChatColor.GRAY + " " + claim.getFormattedName());
        }
    }
}
