package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.type.Faction;

public class FactionShowArgument extends SubCommand {

    private final HCF plugin;

    public FactionShowArgument(HCF plugin) {
        super("show", "Get details about a faction.");
        this.aliases = new String[]{"info", "who"};
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " [playerName|factionName]";
    }


    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        Faction playerFaction = null;
        Faction namedFaction;

        if (args.length < 2) {


            namedFaction = plugin.getFactionManager().getPlayerFaction(player);

            if (namedFaction == null) {
                player.sendMessage(ChatColor.RED + "You are not in a faction.");
                return;
            }
        } else {
            namedFaction = plugin.getFactionManager().getFaction(args[1]);
            playerFaction = plugin.getFactionManager().getContainingPlayerFaction(args[1]);

            if (namedFaction == null && playerFaction == null) {
                player.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
                return;
            }
        }

        if (namedFaction != null) {
            namedFaction.printDetails(player);
        }

        if (playerFaction != null && namedFaction != playerFaction) {
            playerFaction.printDetails(player);
        }

    }
}
