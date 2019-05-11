package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionManager;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.misc.JavaUtils;

public class FactionSetDtrRegenArgument extends SubCommand {

    private final HCF plugin;

    public FactionSetDtrRegenArgument(HCF plugin) {
        super("setdtrregen", "Sets the DTR cooldown of a faction.", Rank.ADMIN);
        this.plugin = plugin;
        this.aliases = new String[]{"setdtrregeneration"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName|factionName> <newRegen>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        long newRegen = JavaUtils.parse(args[2]);

        if (newRegen < 0L) {
            sender.sendMessage(ChatColor.RED + "Faction DTR regeneration duration cannot be negative.");
            return;
        }

        if (newRegen > FactionManager.MAX_DTR_REGEN_MILLIS) {
            sender.sendMessage(ChatColor.RED + "Cannot set factions DTR regen above " + FactionManager.MAX_DTR_REGEN_WORDS + ".");
            return;
        }

        Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (faction == null) {
            sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        if (!(faction instanceof PlayerFaction)) {
            sender.sendMessage(ChatColor.RED + "This type of faction does not use DTR.");
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;
        long previousRegenRemaining = playerFaction.getRemainingRegenerationTime();
        playerFaction.setRemainingRegenerationTime(newRegen);

        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Set DTR regen of " + faction.getName() +
                (previousRegenRemaining > 0L ? " from " + DurationFormatUtils.formatDurationWords(previousRegenRemaining, true, true) : "") + " to " +
                DurationFormatUtils.formatDurationWords(newRegen, true, true) + '.');

    }
}
