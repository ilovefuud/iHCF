package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.LandMap;
import com.doctordark.hcf.faction.claim.Claim;
import com.doctordark.hcf.user.FactionUser;
import com.doctordark.hcf.util.GuavaCompat;
import com.doctordark.hcf.visualise.VisualType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.UUID;

/**
 * Faction argument used to view a interactive map of {@link Claim}s.
 */
public class FactionMapArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionMapArgument(HCF plugin) {
        super("map", "View all claims around your chunk.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " [factionName]";
    }


    @Override
    public void execute(Player player, Player player1, String[] args, String label) {

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
