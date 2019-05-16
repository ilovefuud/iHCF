package us.lemin.hcf.deathban.lives.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.deathban.Deathban;
import us.lemin.hcf.user.FactionUser;

/**
 * A {@link SubCommand} used to clear all {@link Deathban}s.
 */
public class LivesClearDeathbansArgument extends SubCommand {

    private final HCF plugin;

    public LivesClearDeathbansArgument(HCF plugin) {
        super("cleardeathbans", "Clears the global deathbans", Rank.ADMIN);
        this.plugin = plugin;
        this.aliases = new String[]{"resetdeathbans"};
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        for (FactionUser user : plugin.getUserManager().getUsers().values()) {
            user.removeDeathban();
        }

        Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "All death-bans have been cleared.");
    }
}
