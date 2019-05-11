package com.doctordark.hcf.faction.argument;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.struct.Relation;
import com.doctordark.hcf.faction.struct.Role;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.message.ClickableMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.doctordark.hcf.util.SpigotUtils.toBungee;

/**
 * Faction argument used to invite players into {@link Faction}s.
 */
public class FactionInviteArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionInviteArgument(HCF plugin) {
        super("invite", "Invite a player to the faction.");
        this.plugin = plugin;
        this.aliases = new String[]{ "inv", "invitemember", "inviteplayer", "add"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }


    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
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

        Relation relation = plugin.getFactionManager().getPlayerFaction(target.getUniqueId()).getRelation(player);

        ClickableMessage message = new ClickableMessage(player.getName()).color(relation.toChatColour())
                .add(" has invited you to join ").color(CC.YELLOW)
                .add(playerFaction.getName()).color(relation.toChatColour())
                .add(".").color(CC.YELLOW)
                .add("Click to accept this invitation").color(CC.YELLOW)
                .add("[").color(CC.GRAY)
                .add("Click to Join").color(CC.YELLOW).hover(CC.GREEN + "Click to Join").command('/' + label + " accept " + playerFaction.getName())
                .add("]").color(CC.GRAY);

        net.md_5.bungee.api.ChatColor enemyRelationColor = toBungee(Relation.ENEMY.toChatColour());
        ComponentBuilder builder = new ComponentBuilder(player.getName()).color(enemyRelationColor);
        builder.append(" has invited you to join ", ComponentBuilder.FormatRetention.NONE).color(net.md_5.bungee.api.ChatColor.YELLOW);
        builder.append(playerFaction.getName()).color(enemyRelationColor).append(". ", ComponentBuilder.FormatRetention.NONE).color(net.md_5.bungee.api.ChatColor.YELLOW);
        builder.append("Click here").color(net.md_5.bungee.api.ChatColor.GREEN).
                event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + label + " accept " + playerFaction.getName())).
                event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to join ").color(net.md_5.bungee.api.ChatColor.AQUA).
                        append(playerFaction.getName(), ComponentBuilder.FormatRetention.NONE).color(enemyRelationColor).
                        append(".", ComponentBuilder.FormatRetention.NONE).color(net.md_5.bungee.api.ChatColor.AQUA).create()));
        builder.append(" to accept this invitation.", ComponentBuilder.FormatRetention.NONE).color(net.md_5.bungee.api.ChatColor.YELLOW);
        target.spigot().sendMessage(builder.create());
        playerFaction.broadcast(Relation.MEMBER.toChatColour() + player.getName() + ChatColor.YELLOW + " has invited " + Relation.ENEMY.toChatColour() + target.getName() + ChatColor.YELLOW + " into the faction.");
    }
}
