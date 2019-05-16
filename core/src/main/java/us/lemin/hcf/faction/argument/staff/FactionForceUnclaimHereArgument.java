package us.lemin.hcf.faction.argument.staff;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.Claim;

public class FactionForceUnclaimHereArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionForceUnclaimHereArgument(HCF plugin) {
        super("forceunclaimhere", "Forces land unclaim where you are standing.", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }


        Claim claimAt = plugin.getFactionManager().getClaimAt(player.getLocation());

        if (claimAt == null) {
            player.sendMessage(ChatColor.RED + "There is not a claim at your current position.");
            return;
        }

        if (claimAt.getFaction().removeClaim(claimAt, player)) {
            player.sendMessage(ChatColor.YELLOW + "Removed claim " + claimAt.getClaimUniqueID().toString() + " owned by " + claimAt.getFaction().getName() + ".");
            return;
        }

        player.sendMessage(ChatColor.RED + "Failed to remove claim " + claimAt.getClaimUniqueID().toString());

    }
}
