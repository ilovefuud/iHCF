package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.UUID;

public class FactionLeaderArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionLeaderArgument(HCF plugin) {
        super("leader", "Sets the new leader for your faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"setleader", "newleader"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }



    @Override
    public void execute(Player player, Player target, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        TaskUtil.runAsync(plugin, ()-> {
            PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player.getUniqueId());

            if (playerFaction == null) {
                player.sendMessage(ChatColor.RED + "You are not in a faction.");
                return;
            }

            UUID uuid = player.getUniqueId();
            FactionMember selfMember = playerFaction.getMember(uuid);
            Role selfRole = selfMember.getRole();

            if (selfRole != Role.LEADER) {
                player.sendMessage(ChatColor.RED + "You must be the current faction leader to transfer the faction.");
                return;
            }
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            FactionMember targetMember = playerFaction.getMember(profile.getId());

            if (targetMember == null) {
                player.sendMessage(ChatColor.RED + "Player '" + args[1] + "' is not in your faction.");
                return;
            }

            if (targetMember.getUniqueId().equals(uuid)) {
                player.sendMessage(ChatColor.RED + "You are already the faction leader.");
                return;
            }

            targetMember.setRole(Role.LEADER);
            selfMember.setRole(Role.CAPTAIN);

            ChatColor colour = plugin.getConfiguration().getRelationColourTeammate();
            playerFaction.broadcast(colour + selfMember.getRole().getAstrix() + selfMember.getName() + ChatColor.YELLOW +
                    " has transferred the faction to " + colour + targetMember.getRole().getAstrix() + targetMember.getName() + ChatColor.YELLOW + '.');

        });
    }
}
