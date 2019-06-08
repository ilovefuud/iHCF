package us.lemin.hcf.faction.argument.subclaim;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.claim.ClaimHandler;
import us.lemin.hcf.faction.claim.ClaimSelection;
import us.lemin.hcf.faction.claim.Subclaim;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Map;
import java.util.UUID;

public class FactionSubclaimCreateArgument extends SubCommand {

    private final HCF plugin;

    public FactionSubclaimCreateArgument(HCF plugin) {
        super("create", "Create a subclaim with a selection");
        this.plugin = plugin;
        this.aliases = new String[]{"make", "build"};
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
        }        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        UUID uuid = player.getUniqueId();

        if (playerFaction.getMember(uuid).getRole() == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "You must be a faction officer to create subclaims.");
            return;
        }

        if (args[2].length() < plugin.getConfiguration().getFactionSubclaimNameMinCharacters()) {
            sender.sendMessage(ChatColor.RED + "Subclaim names must have at least " + plugin.getConfiguration().getFactionSubclaimNameMinCharacters() + " characters.");
            return;
        }

        if (args[2].length() > plugin.getConfiguration().getFactionNameMaxCharacters()) {
            sender.sendMessage(ChatColor.RED + "Subclaim names cannot be longer than " + plugin.getConfiguration().getFactionSubclaimNameMaxCharacters() + " characters.");
            return;
        }

        if (!JavaUtils.isAlphanumeric(args[2])) {
            sender.sendMessage(ChatColor.RED + "Subclaim names may only be alphanumeric.");
            return;
        }

        for (Claim claim : playerFaction.getClaims()) {
            if (claim.getSubclaim(args[2]) != null) {
                sender.sendMessage(ChatColor.RED + "Your faction already has a subclaim named " + args[2] + '.');
                return;
            }
        }

        Map<UUID, ClaimSelection> selectionMap = plugin.getClaimHandler().claimSelectionMap;
        ClaimSelection claimSelection = selectionMap.get(uuid);

        if (claimSelection == null || !claimSelection.hasBothPositionsSet()) {
            sender.sendMessage(ChatColor.RED + "You have not set both positions of this subclaim.");
            return;
        }

        Subclaim subclaim = new Subclaim(playerFaction, claimSelection.getPos1(), claimSelection.getPos2());
        subclaim.setY1(ClaimHandler.MIN_CLAIM_HEIGHT);
        subclaim.setY2(ClaimHandler.MAX_CLAIM_HEIGHT);
        subclaim.setName(args[2]);
        if (plugin.getClaimHandler().tryCreatingSubclaim(player, subclaim)) {
            plugin.getVisualiseHandler().clearVisualBlock(player, subclaim.getMinimumPoint());
            plugin.getVisualiseHandler().clearVisualBlock(player, subclaim.getMaximumPoint());
            selectionMap.remove(uuid);
        }

    }
}
