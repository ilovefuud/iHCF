package us.lemin.hcf.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.user.FactionUser;

/**
 * Command used to toggle the lightning strikes on death for a {@link Player}.
 */
public class ToggleLightningCommand extends PlayerCommand {

    private final HCF plugin;

    public ToggleLightningCommand(HCF plugin) {

        super("togglelightning");
        this.plugin = plugin;
    }


    @Override
    public void execute(Player player, String[] strings) {
        FactionUser factionUser = plugin.getUserManager().getUser(player.getUniqueId());
        boolean newShowLightning = !factionUser.isShowLightning();
        factionUser.setShowLightning(newShowLightning);

        player.sendMessage(ChatColor.AQUA + "You will now " + (newShowLightning ? ChatColor.GREEN + "able" : ChatColor.RED + "unable") +
                ChatColor.AQUA + " to see lightning strikes on death.");

    }
}
