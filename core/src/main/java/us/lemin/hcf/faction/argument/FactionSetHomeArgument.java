package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

public class FactionSetHomeArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionSetHomeArgument(HCF plugin) {
        super("sethome", "Sets the faction home location.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(Player player, Player player1, String[] strings, String s) {

        if (plugin.getConfiguration().getMaxHeightFactionHome() != -1 && player.getLocation().getY() > plugin.getConfiguration().getMaxHeightFactionHome()) {
            player.sendMessage(ChatColor.RED + "You can not set your faction home above y " + plugin.getConfiguration().getMaxHeightFactionHome() + ".");
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        FactionMember factionMember = playerFaction.getMember(player);

        if (factionMember.getRole() == Role.MEMBER) {
            player.sendMessage(ChatColor.RED + "You must be a faction officer to set the home.");
            return;
        }

        Location location = player.getLocation();

        boolean insideTerritory = false;
        for (Claim claim : playerFaction.getClaims()) {
            if (claim.contains(location)) {
                insideTerritory = true;
                break;
            }
        }

        if (!insideTerritory) {
            player.sendMessage(ChatColor.RED + "You may only set your home in your territory.");
            return;
        }

        playerFaction.setHome(location);
        playerFaction.broadcast(plugin.getConfiguration().getRelationColourTeammate() + factionMember.getRole().getAstrix() +
                player.getName() + ChatColor.YELLOW + " has updated the faction home.");

    }
}
