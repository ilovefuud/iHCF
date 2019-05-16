package us.lemin.hcf.command.sotw.impl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;

public class SotwEndCommand extends SubCommand {

    private final HCF plugin;

    public SotwEndCommand(HCF plugin) {
        super("end");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] strings, String s) {
        if (plugin.getSotwTimer().cancel()) {
            sender.sendMessage(ChatColor.RED + "Cancelled SOTW protection.");
            return;
        }
        sender.sendMessage(ChatColor.RED + "SOTW protection is not active.");
    }
}
