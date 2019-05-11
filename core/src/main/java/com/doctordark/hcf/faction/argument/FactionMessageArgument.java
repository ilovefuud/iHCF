package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.struct.ChatChannel;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.Arrays;

public class FactionMessageArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionMessageArgument(HCF plugin) {
        super("message", "Sends a message to your faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"msg"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <message>";
    }


    @Override
    public void execute(Player player, Player target, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        String format = String.format(ChatChannel.FACTION.getRawFormat(player), "", HCF.SPACE_JOINER.join(Arrays.copyOfRange(args, 1, args.length)));
        for (Player loopPlayer : playerFaction.getOnlinePlayers()) {
            loopPlayer.sendMessage(format);
        }

    }
}
