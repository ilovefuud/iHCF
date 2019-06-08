package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.LandMap;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.user.FactionUser;
import us.lemin.hcf.util.GuavaCompat;
import us.lemin.hcf.visualise.VisualType;

import java.util.UUID;

/**
 * Faction argument used to view a interactive map of {@link Claim}s.
 */
public class FactionMapArgument extends SubCommand {

    private final HCF plugin;

    public FactionMapArgument(HCF plugin) {
        super("map", "View all claims around your chunk.");
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

        UUID uuid = player.getUniqueId();

        final FactionUser factionUser = plugin.getUserManager().getUser(uuid);
        final VisualType visualType;
        if (args.length < 2) {
            visualType = VisualType.CLAIM_MAP;
        } else if ((visualType = GuavaCompat.getIfPresent(VisualType.class, args[1]).orNull()) == null) {
            player.sendMessage(ChatColor.RED + "Visual type " + args[1] + " not found.");
            return;
        }

        boolean newShowingMap = !factionUser.isShowClaimMap();
        if (newShowingMap) {
            if (!LandMap.updateMap(player, plugin, visualType, true)) {
                return;
            }
        } else {
            plugin.getVisualiseHandler().clearVisualBlocks(player, visualType, null);
            player.sendMessage(ChatColor.RED + "Claim pillars are no longer shown.");
        }

        factionUser.setShowClaimMap(newShowingMap);
        return;
    }
}
