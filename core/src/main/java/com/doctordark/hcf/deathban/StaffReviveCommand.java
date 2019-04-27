package com.doctordark.hcf.deathban;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.user.FactionUser;
import com.doctordark.hcf.util.profile.ProfileLookupCallback;
import com.doctordark.hcf.util.profile.ProfileUtil;
import com.doctordark.util.BukkitUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StaffReviveCommand implements CommandExecutor, TabCompleter {

    private final HCF plugin;

    public StaffReviveCommand(HCF plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <playerName>");
            return true;
        }

        ProfileUtil.lookupProfileAsync(plugin, args[0], (result, success) -> {
            if (success) {
                UUID targetUUID = result.getId();
                FactionUser factionTarget = HCF.getPlugin().getUserManager().getUser(targetUUID);
                Deathban deathban = factionTarget.getDeathban();

                if (deathban == null || !deathban.isActive()) {
                    sender.sendMessage(ChatColor.RED + result.getName() + " is not death-banned.");
                    return;
                }

                factionTarget.removeDeathban();
                Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Staff revived " + result.getName() + ".");
            } else {
                sender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[0] + ChatColor.GOLD + "' not found.");
            }
        });

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        List<String> results = new ArrayList<>();
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            for (FactionUser factionUser : plugin.getUserManager().getUsers().values()) {
                Deathban deathban = factionUser.getDeathban();
                if (deathban != null && deathban.isActive()) {
                    ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(factionUser.getUserUUID());
                    String name = profile != null ? profile.getName() : null;
                    if (name != null) {
                        results.add(name);
                    }
                }
            }
        });


        return BukkitUtils.getCompletions(args, results);
    }
}
