package us.lemin.hcf.faction.argument.subclaim;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.claim.Subclaim;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Iterator;

public class FactionSubclaimDeleteArgument extends SubCommand {

    private final HCF plugin;

    public FactionSubclaimDeleteArgument(HCF plugin) {
        super("delete", "Remove a subclaim");
        this.plugin = plugin;
        this.aliases = new String[]{"del", "remove"};
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

        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            sender.sendMessage(ChatColor.RED + "You must be a faction officer to edit subclaims.");
            return;
        }

        for (Claim claim : playerFaction.getClaims()) {
            for (Iterator<Subclaim> iterator = claim.getSubclaims().iterator(); iterator.hasNext(); ) {
                Subclaim subclaim = iterator.next();
                if (subclaim.getName().equalsIgnoreCase(args[2])) {
                    iterator.remove();
                    sender.sendMessage(ChatColor.AQUA + "Removed subclaim named " + subclaim.getName() + '.');
                    return;
                }
            }
        }

        sender.sendMessage(ChatColor.RED + "Your faction does not have a subclaim named " + args[2] + '.');
    }
}
