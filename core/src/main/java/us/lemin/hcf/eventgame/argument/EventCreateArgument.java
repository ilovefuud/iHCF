package us.lemin.hcf.eventgame.argument;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.faction.ConquestFaction;
import us.lemin.hcf.eventgame.faction.KothFaction;
import us.lemin.hcf.faction.type.Faction;

public class EventCreateArgument extends SubCommand {

    private final HCF plugin;

    public EventCreateArgument(HCF plugin) {
        super("create", "Defines a new event");
        this.plugin = plugin;
        this.permission = "hcf.command.event.argument." + getName();
        this.aliases = new String[]{"make", "define"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <eventName> <Conquest|KOTH|Citadel>";
    }



    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Faction faction = plugin.getFactionManager().getFaction(args[1]);

        if (faction != null) {
            sender.sendMessage(ChatColor.RED + "There is already a faction named " + args[1] + '.');
            return;
        }

        switch (args[2].toUpperCase()) {
            case "CONQUEST":
                faction = new ConquestFaction(args[1]);
                break;
            case "KOTH":
                faction = new KothFaction(args[1]);
                break;
            default:
                sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
                return;
        }

        plugin.getFactionManager().createFaction(faction, sender);

        sender.sendMessage(ChatColor.YELLOW + "Created event faction " + ChatColor.WHITE + faction.getDisplayName(sender) + ChatColor.YELLOW + " with type " + WordUtils.capitalizeFully(args[2]) + '.');
        return;
    }
}
