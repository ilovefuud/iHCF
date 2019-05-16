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

public class FactionKickArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionKickArgument(HCF plugin) {
        super("kick", "Kick a player from the faction.");
        this.plugin = plugin;
        this.aliases = new String[]{"kickmember", "kickplayer"};
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

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.isRaidable() && !plugin.getEotwHandler().isEndOfTheWorld()) {
            player.sendMessage(ChatColor.RED + "You cannot kick players whilst your faction is raidable.");
            return;
        }

        TaskUtil.runAsync(plugin, () -> {
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            FactionMember targetMember = playerFaction.getMember(profile.getId());

            if (targetMember == null) {
                player.sendMessage(ChatColor.RED + "Your faction does not have a member named '" + args[1] + "'.");
                return;
            }

            Role selfRole = playerFaction.getMember(player.getUniqueId()).getRole();

            if (selfRole == Role.MEMBER) {
                player.sendMessage(ChatColor.RED + "You must be a faction officer to kick members.");
                return;
            }

            Role targetRole = targetMember.getRole();

            if (targetRole == Role.LEADER) {
                player.sendMessage(ChatColor.RED + "You cannot kick the faction leader.");
                return;
            }

            if (targetRole == Role.CAPTAIN && selfRole == Role.CAPTAIN) {
                player.sendMessage(ChatColor.RED + "You must be a faction leader to kick captains.");
                return;
            }

            Player onlineTarget = targetMember.toOnlinePlayer();
            if (playerFaction.removeMember(player, onlineTarget, targetMember.getUniqueId(), true, true)) {
                if (onlineTarget != null) {
                    onlineTarget.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "You were kicked from the faction by " + player.getName() + '.');
                }

                playerFaction.broadcast(plugin.getConfiguration().getRelationColourEnemy() + targetMember.getName() + ChatColor.YELLOW + " has been kicked by " +
                        plugin.getConfiguration().getRelationColourTeammate() + playerFaction.getMember(player).getRole().getAstrix() + player.getName() + ChatColor.YELLOW + '.');
            }
        });


    }
}
