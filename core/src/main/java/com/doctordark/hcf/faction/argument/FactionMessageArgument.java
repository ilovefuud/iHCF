package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.FactionArgument;
import com.doctordark.hcf.faction.struct.ChatChannel;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class FactionMessageArgument extends FactionArgument {

    private final HCF plugin;

    public FactionMessageArgument(HCF plugin) {
        super("message", "Sends a message to your faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"msg"};
    }

    @Override
    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <message>";
    }

    @Override
    public boolean onCommand(CommandSender player, Command command, String label, String[] args) {
        if (!(player instanceof Player)) {
            player.sendMessage(ChatColor.RED + "Only players can use faction chat.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return true;
        }

        Player player = (Player) player;
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return true;
        }

        String format = String.format(ChatChannel.FACTION.getRawFormat(player), "", HCF.SPACE_JOINER.join(Arrays.copyOfRange(args, 1, args.length)));
        for (Player target : playerFaction.getOnlinePlayers()) {
            target.sendMessage(format);
        }

        return true;
    }
}
