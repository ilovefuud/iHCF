package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

public class FactionDisbandArgument extends SubCommand {

    private final HCF plugin;

    public FactionDisbandArgument(HCF plugin) {
        super("disband", "Disband your faction.");
        this.plugin = plugin;
        this.playerOnly = true;

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }
        if (!plugin.getConfiguration().isKitmap()) {
            if (playerFaction.isRaidable() && !plugin.getEotwHandler().isEndOfTheWorld()) {
                player.sendMessage(ChatColor.RED + "You cannot disband your faction while it is raidable.");
                return;
            }
        }

        if (playerFaction.getMember(player.getUniqueId()).getRole() != Role.LEADER) {
            player.sendMessage(ChatColor.RED + "You must be a leader to disband the faction.");
            return;
        }

        plugin.getFactionManager().removeFaction(playerFaction, player);
    }
}
