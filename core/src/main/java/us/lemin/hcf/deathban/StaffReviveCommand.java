package us.lemin.hcf.deathban;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.misc.BukkitUtils;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.user.FactionUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StaffReviveCommand extends BaseCommand {

    private final HCF plugin;

    public StaffReviveCommand(HCF plugin) {
        super("staffrevive", Rank.ADMIN);
        this.plugin = plugin;
    }



    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
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


    @Override
    protected void execute(CommandSender commandSender, String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage(ChatColor.RED + "Usage: /" + getLabel() + " <playerName>");
            return;
        }

        ProfileUtil.lookupProfileAsync(plugin, args[0], (result, success) -> {
            if (success) {
                UUID targetUUID = result.getId();
                FactionUser factionTarget = HCF.getPlugin().getUserManager().getUser(targetUUID);
                Deathban deathban = factionTarget.getDeathban();

                if (deathban == null || !deathban.isActive()) {
                    commandSender.sendMessage(ChatColor.RED + result.getName() + " is not death-banned.");
                    return;
                }

                factionTarget.removeDeathban();
                Command.broadcastCommandMessage(commandSender, ChatColor.YELLOW + "Staff revived " + result.getName() + ".");
            } else {
                commandSender.sendMessage(ChatColor.GOLD + "Player '" + ChatColor.WHITE + args[0] + ChatColor.GOLD + "' not found.");
            }
        });
    }
}
