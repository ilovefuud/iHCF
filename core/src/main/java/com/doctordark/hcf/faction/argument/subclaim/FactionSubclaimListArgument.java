package com.doctordark.hcf.faction.argument.subclaim;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.faction.claim.Claim;
import com.doctordark.hcf.faction.claim.Subclaim;
import com.doctordark.hcf.faction.type.PlayerFaction;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerSubCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FactionSubclaimListArgument extends PlayerSubCommand {

    private final HCF plugin;

    public FactionSubclaimListArgument(HCF plugin) {
        super("list", "List subclaims in this faction");
        this.plugin = plugin;
        this.aliases = new String[]{"listsubs"};
    }

    public String getUsage(String label) {
        return '/' + label + " subclaim " + getName();
    }

    @Override
    public void execute(Player sender, Player player1, String[] args, String label) {
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(sender);

        if (playerFaction == null) {
            sender.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        List<String> subclaimNames = new ArrayList<>();
        for (Claim claim : playerFaction.getClaims()) {
            subclaimNames.addAll(claim.getSubclaims().stream().map(Subclaim::getName).collect(Collectors.toList()));
        }

        if (subclaimNames.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "Your faction does not own any subclaims.");
            return;
        }

        sender.sendMessage(ChatColor.YELLOW + "Factions' Subclaims (" + subclaimNames.size() + "): " + ChatColor.AQUA + HCF.COMMA_JOINER.join(subclaimNames));
    }
}
