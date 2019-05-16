package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

public class FactionOpenArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionOpenArgument(HCF plugin) {
        super("open", "Opens the faction to the public.");
        this.plugin = plugin;
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(Player player, Player player1, String[] strings, String s) {
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        FactionMember factionMember = playerFaction.getMember(player.getUniqueId());

        if (factionMember.getRole() != Role.LEADER) {
            player.sendMessage(ChatColor.RED + "You must be a faction leader to do this.");
            return;
        }

        boolean newOpen = !playerFaction.isOpen();
        playerFaction.setOpen(newOpen);
        playerFaction.broadcast(ChatColor.YELLOW + player.getName() + " has " + (newOpen ? ChatColor.GREEN + "opened" : ChatColor.RED + "closed") + ChatColor.YELLOW + " the faction to public.");
    }
}
