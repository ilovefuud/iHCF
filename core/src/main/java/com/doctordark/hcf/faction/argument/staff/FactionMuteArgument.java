package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FactionMuteArgument extends SubCommand {

    private final HCF plugin;

    public FactionMuteArgument(HCF plugin) {
        super("mute", "Mutes every member in this faction.", Rank.ADMIN);
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <factionName> <time:(e.g. 1h2s)> <reason>";
    }

    @Override
    public void execute(CommandSender commandSender, Player player, String[] args, String label) {
        if (args.length < 3) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (!(faction instanceof PlayerFaction)) {
            player.sendMessage(ChatColor.RED + "Player faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        PlayerFaction playerFaction = (PlayerFaction) faction;
        String extraArgs = HCF.SPACE_JOINER.join(Arrays.copyOfRange(args, 2, args.length));
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        for (UUID uuid : playerFaction.getMembers().keySet()) {
            String commandLine = "mute " + uuid.toString() + " " + extraArgs;
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Executing " + ChatColor.RED + commandLine);
            console.getServer().dispatchCommand(player, commandLine);
        }

        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "Executed mute action on faction " + playerFaction.getName() + ".");
        return;
    }
}
