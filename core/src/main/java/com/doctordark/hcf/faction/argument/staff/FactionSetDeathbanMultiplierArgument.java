package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.type.Faction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.misc.JavaUtils;

public class FactionSetDeathbanMultiplierArgument extends SubCommand {

    private static final double MIN_MULTIPLIER = 0.0;
    private static final double MAX_MULTIPLIER = 5.0;

    private final HCF plugin;

    public FactionSetDeathbanMultiplierArgument(HCF plugin) {
        super("setdeathbanmultiplier", "Sets the deathban multiplier of a faction.", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName|factionName> <newMultiplier>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (faction == null) {
            sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        Double multiplier = JavaUtils.tryParseDouble(args[2]);

        if (multiplier == null) {
            sender.sendMessage(ChatColor.RED + "'" + args[2] + "' is not a valid number.");
            return;
        }

        if (multiplier < MIN_MULTIPLIER) {
            sender.sendMessage(ChatColor.RED + "Deathban multipliers may not be less than " + MIN_MULTIPLIER + '.');
            return;
        }

        if (multiplier > MAX_MULTIPLIER) {
            sender.sendMessage(ChatColor.RED + "Deathban multipliers may not be more than " + MAX_MULTIPLIER + '.');
            return;
        }

        double previousMultiplier = faction.getDeathbanMultiplier();
        faction.setDeathbanMultiplier(multiplier);

        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set deathban multiplier of " + faction.getName() + " from " + previousMultiplier + " to " + multiplier + '.');

    }
}
