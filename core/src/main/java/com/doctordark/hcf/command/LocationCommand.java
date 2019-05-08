package com.doctordark.hcf.command;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.type.Faction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.BaseCommand;

/**
 * Command used to check current the current {@link Faction} at
 * the position of a given {@link Player}s {@link Location}.
 */
public class LocationCommand extends BaseCommand {

    private final HCF plugin;

    public LocationCommand(HCF plugin) {
        super("location");
        this.plugin = plugin;
    }

    @Override
    protected void execute(CommandSender sender, String[] args) {
        Player target;
        if (args.length >= 1 && sender.hasPermission(this.getPermission() + ".others")) {
            target = Bukkit.getPlayer(args[0]);
        } else if (sender instanceof Player) {
            target = (Player) sender;
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /" + this.getName() + " [playerName]");
            return;
        }

        if (target == null || (sender instanceof Player && !((Player) sender).canSee(target))) {
            sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[0] + ChatColor.GOLD + "' not found.");
            return;
        }

        Location location = target.getLocation();
        Faction factionAt = plugin.getFactionManager().getFactionAt(location);
        sender.sendMessage(ChatColor.YELLOW + target.getName() + " is in the territory of " + factionAt.getDisplayName(sender)
                + ChatColor.YELLOW + '(' + (factionAt.isSafezone() ? ChatColor.GREEN + "Non-Deathban" : ChatColor.RED + "Deathban") + ChatColor.YELLOW + ')');

    }
}
