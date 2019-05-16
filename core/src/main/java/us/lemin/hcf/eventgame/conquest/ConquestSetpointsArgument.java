package us.lemin.hcf.eventgame.conquest;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.EventType;
import us.lemin.hcf.eventgame.tracker.ConquestTracker;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;

public class ConquestSetpointsArgument extends SubCommand {

    private final HCF plugin;

    public ConquestSetpointsArgument(HCF plugin) {
        super("setpoints", "Sets the points of a faction in the Conquest event", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName> <amount>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[1]);

        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "Faction " + args[1] + " is either not found or is not a player faction.");
            return;
        }

        Integer amount = JavaUtils.tryParseInt(args[2]);

        if (amount == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a number.");
            return;
        }

        if (amount > plugin.getConfiguration().getConquestRequiredVictoryPoints()) {
            sender.sendMessage(ChatColor.RED + "Maximum points for Conquest is " + plugin.getConfiguration().getConquestRequiredVictoryPoints() + '.');
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;
        ((ConquestTracker) EventType.CONQUEST.getEventTracker()).setPoints(playerFaction, amount);

        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set the points of faction " + playerFaction.getName() + " to " + amount + '.');
    }
}
