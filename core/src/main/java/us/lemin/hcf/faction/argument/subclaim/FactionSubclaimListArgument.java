package us.lemin.hcf.faction.argument.subclaim;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.claim.Subclaim;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FactionSubclaimListArgument extends SubCommand {

    private final HCF plugin;

    public FactionSubclaimListArgument(HCF plugin) {
        super("list", "List subclaims in this faction");
        this.plugin = plugin;
        this.aliases = new String[]{"listsubs"};
    }

    public String getUsage(String label) {
        return '/' + label + " subclaim " + getName();
    }

    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            return;
        }
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        List<String> subclaimNames = new ArrayList<>();
        for (Claim claim : playerFaction.getClaims()) {
            subclaimNames.addAll(claim.getSubclaims().stream().map(Subclaim::getName).collect(Collectors.toList()));
        }

        if (subclaimNames.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Your faction does not own any subclaims.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Factions' Subclaims (" + subclaimNames.size() + "): " + ChatColor.AQUA + HCF.COMMA_JOINER.join(subclaimNames));
    }
}
