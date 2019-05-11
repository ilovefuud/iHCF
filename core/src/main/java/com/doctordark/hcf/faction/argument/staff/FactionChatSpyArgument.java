package com.doctordark.hcf.faction.argument.staff;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.event.FactionChatEvent;
import com.doctordark.hcf.faction.event.FactionRemoveEvent;
import com.doctordark.hcf.faction.type.Faction;
import com.doctordark.hcf.faction.type.PlayerFaction;
import com.doctordark.hcf.user.FactionUser;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import us.lemin.core.commands.PlayerSubCommand;
import us.lemin.core.player.rank.Rank;

import java.util.*;
import java.util.stream.Collectors;

public class FactionChatSpyArgument extends PlayerSubCommand implements Listener {

    private static final UUID ALL_UUID = UUID.fromString("5a3ed6d1-0239-4e24-b4a9-8cd5b3e5fc72");

    private final HCF plugin;

    public FactionChatSpyArgument(HCF plugin) {
        super("chatspy", "Spy on the chat of a faction.", Rank.ADMIN);
        this.plugin = plugin;
        this.aliases = new String[]{"cs"};
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private static final Joiner USAGE_JOINER = Joiner.on('|');

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <" + USAGE_JOINER.join(COMPLETIONS) + "> [factionName]";
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionRemove(FactionRemoveEvent event) {
        if (event.getFaction() instanceof PlayerFaction) {
            UUID factionUUID = event.getFaction().getUniqueID();
            for (FactionUser user : plugin.getUserManager().getUsers().values()) {
                user.getFactionChatSpying().remove(factionUUID);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onFactionChat(FactionChatEvent event) {
        Player player = event.getPlayer();
        Faction faction = event.getFaction();
        String format = ChatColor.GOLD + "[" + ChatColor.RED + event.getChatChannel().getDisplayName() + ": " + ChatColor.YELLOW + faction.getName() + ChatColor.GOLD + "] " +
                ChatColor.GRAY + event.getFactionMember().getRole().getAstrix() + player.getName() + ": " + ChatColor.YELLOW + event.getMessage();

        Collection<CommandSender> recipients = new HashSet<>(Bukkit.getOnlinePlayers());
        recipients.removeAll(event.getRecipients());
        for (CommandSender recipient : recipients) {
            if (!(recipient instanceof Player)) continue;

            Player target = (Player) recipient;
            FactionUser user = event.isAsynchronous() ? plugin.getUserManager().getUserAsync(target.getUniqueId()) : plugin.getUserManager().getUser(player.getUniqueId());
            Collection<UUID> spying = user.getFactionChatSpying();
            if (spying.contains(ALL_UUID) || spying.contains(faction.getUniqueID())) {
                recipient.sendMessage(format);
            }
        }
    }



    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("list", "add", "del", "clear");

    @Override
    public void execute(Player player, Player player1, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        Set<UUID> currentSpies = plugin.getUserManager().getUser(player.getUniqueId()).getFactionChatSpying();

        if (args[1].equalsIgnoreCase("list")) {
            if (currentSpies.isEmpty()) {
                player.sendMessage(ChatColor.RED + "You are not spying on the chat of any factions.");
                return;
            }

            player.sendMessage(ChatColor.GRAY + "You are currently spying on the chat of (" + currentSpies.size() + " factions): " + ChatColor.RED +
                    Joiner.on(ChatColor.GRAY + ", " + ChatColor.RED).join(currentSpies) + ChatColor.GRAY + '.');

            return;
        }

        if (args[1].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + args[1].toLowerCase() + " <all|factionName|playerName>");
                return;
            }

            Faction faction = plugin.getFactionManager().getFaction(args[2]);

            if (!(faction instanceof PlayerFaction)) {
                player.sendMessage(ChatColor.RED + "Player based faction named or containing member with IGN or UUID " + args[2] + " not found.");
                return;
            }

            if (currentSpies.contains(ALL_UUID) || currentSpies.contains(faction.getUniqueID())) {
                player.sendMessage(ChatColor.RED + "You are already spying on the chat of " + (args[2].equalsIgnoreCase("all") ? "all factions" : args[2]) + '.');
                return;
            }

            if (args[2].equalsIgnoreCase("all")) {
                currentSpies.clear();
                currentSpies.add(ALL_UUID);
                player.sendMessage(ChatColor.GREEN + "You are now spying on the chat of all factions.");
                return;
            }

            if (currentSpies.add(faction.getUniqueID())) {
                player.sendMessage(ChatColor.GREEN + "You are now spying on the chat of " + faction.getDisplayName(player) + ChatColor.GREEN + '.');
            } else {
                player.sendMessage(ChatColor.RED + "You are already spying on the chat of " + faction.getDisplayName(player) + ChatColor.RED + '.');
            }

            return;
        }

        if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("remove")) {
            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /" + label + ' ' + args[1].toLowerCase() + " <playerName>");
                return;
            }

            if (args[2].equalsIgnoreCase("all")) {
                currentSpies.remove(ALL_UUID);
                player.sendMessage(ChatColor.RED + "No longer spying on the chat of all factions.");
                return;
            }

            Faction faction = plugin.getFactionManager().getContainingFaction(args[2]);

            if (faction == null) {
                player.sendMessage(ChatColor.GOLD + "Faction '" + ChatColor.WHITE + args[2] + ChatColor.GOLD + "' not found.");
                return;
            }

            if (currentSpies.remove(faction.getUniqueID())) {
                player.sendMessage(ChatColor.RED + "You are no longer spying on the chat of " + faction.getDisplayName(player) + ChatColor.RED + '.');
            } else {
                player.sendMessage(ChatColor.RED + "You will still not be spying on the chat of " + faction.getDisplayName(player) + ChatColor.RED + '.');
            }

            return;
        }

        if (args[1].equalsIgnoreCase("clear")) {
            currentSpies.clear();
            player.sendMessage(ChatColor.YELLOW + "You are no longer spying the chat of any faction.");
            return;
        }

        player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
    }
}
