package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.ClickableMessage;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.struct.Relation;
import us.lemin.hcf.faction.struct.Role;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Set;

/**
 * Faction argument used to invite players into {@link Faction}s.
 */
public class FactionInviteArgument extends SubCommand {

    private final HCF plugin;

    public FactionInviteArgument(HCF plugin) {
        super("invite", "Invite a player to the faction.");
        this.plugin = plugin;
        this.aliases = new String[]{ "inv", "invitemember", "inviteplayer", "add"};
        this.playerOnly = true;

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }


    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        if (target == null) {
            player.sendMessage(ChatColor.RED + "That player is not online.");
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        if (playerFaction.getMember(player.getUniqueId()).getRole() == Role.MEMBER) {
            player.sendMessage(ChatColor.RED + "You must a faction officer to invite members.");
            return;
        }

        Set<String> invitedPlayerNames = playerFaction.getInvitedPlayerNames();

        if (playerFaction.getMember(target) != null) {
            player.sendMessage(ChatColor.RED + "'" + target.getName() + "' is already in your faction.");
            return;
        }

        if (!plugin.getEotwHandler().isEndOfTheWorld() && playerFaction.isRaidable()) {
            player.sendMessage(ChatColor.RED + "You may not invite players whilst your faction is raidable.");
            return;
        }

        if (!invitedPlayerNames.add(target.getName())) {
            player.sendMessage(ChatColor.RED + target.getName() + " has already been invited.");
            return;
        }

        PlayerFaction faction = plugin.getFactionManager().getPlayerFaction(target.getUniqueId());
        Relation relation = faction == null ? Relation.NEUTRAL : faction.getRelation(player);

        ClickableMessage message = new ClickableMessage(player.getName()).color(relation.toChatColour())
                .add(" has invited you to join ").color(CC.YELLOW)
                .add(playerFaction.getName()).color(relation.toChatColour())
                .add(". ").color(CC.YELLOW)
                .add("[").color(CC.GRAY)
                .add("Click to Join").color(CC.YELLOW).hover(CC.GREEN + "Click to Join").command('/' + label + " accept " + playerFaction.getName())
                .add("]").color(CC.GRAY);

        message.sendToPlayer(target);
        playerFaction.broadcast(Relation.MEMBER.toChatColour() + player.getName() + ChatColor.YELLOW + " has invited " + relation.toChatColour() + target.getName() + ChatColor.YELLOW + " into the faction.");
    }
}
