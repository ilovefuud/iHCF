package us.lemin.hcf.faction.argument.staff;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.type.ClaimableFaction;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.util.RegionData;

/**
 * Used to claim land for other {@link ClaimableFaction}s.
 */
public class FactionEditArgument extends SubCommand {

    private final HCF plugin;

    public FactionEditArgument(HCF plugin) {
        super("edit", "Edit the land of another nonplayer faction.", Rank.ADMIN);
        this.plugin = plugin;
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
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction targetFaction = plugin.getFactionManager().getFaction(args[1]);

        if (!(targetFaction instanceof ClaimableFaction)) {
            player.sendMessage(ChatColor.RED + "Claimable faction named " + args[1] + " not found.");
            return;
        }


        RegionData selection = plugin.getRegionManager().getData(player);

        if (selection == null) {
            player.sendMessage(ChatColor.RED + "You must make a region selection to do this.");
            return;
        }

        ClaimableFaction claimableFaction = (ClaimableFaction) targetFaction;

        if (claimableFaction.addClaim(new Claim(claimableFaction, selection.getA(), selection.getB()), player)) {
            player.sendMessage(ChatColor.YELLOW + "Successfully claimed this land for " + ChatColor.RED + targetFaction.getName() + ChatColor.YELLOW + '.');
        }

    }
}
