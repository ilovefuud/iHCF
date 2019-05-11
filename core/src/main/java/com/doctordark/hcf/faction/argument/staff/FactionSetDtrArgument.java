package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.misc.JavaUtils;

/**
 * Faction argument used to set the DTR of {@link Faction}s.
 */
public class FactionSetDtrArgument extends SubCommand {

    private final HCF plugin;

    public FactionSetDtrArgument(HCF plugin) {
        super("setdtr", "Sets the DTR of a faction.", Rank.ADMIN);
        this.plugin = plugin;
        this.permission = "hcf.command.faction.argument." + getName();
        this.aliases = new String[]{"dtr"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName|factionName> <newDtr>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Double newDTR = JavaUtils.tryParseDouble(args[2]);

        if (newDTR == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
            return;
        }

        if (args[1].equalsIgnoreCase("all")) {
            for (Faction faction : plugin.getFactionManager().getFactions()) {
                if (faction instanceof PlayerFaction) {
                    ((PlayerFaction) faction).setDeathsUntilRaidable(newDTR);
                }
            }

            Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set DTR of all factions to " + newDTR + '.');
            return;
        }

        Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (faction == null) {
            sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "You can only set DTR of player factions.");
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;
        double previousDtr = playerFaction.getDeathsUntilRaidable();
        newDTR = playerFaction.setDeathsUntilRaidable(newDTR);

        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set DTR of " + faction.getName() + " from " + previousDtr + " to " + newDTR + '.');

    }
}
