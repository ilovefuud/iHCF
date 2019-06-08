package us.lemin.hcf.eventgame;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommandExecutor;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.argument.*;
import us.lemin.hcf.timer.type.InvincibilityTimer;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class EventExecutor extends SubCommandExecutor {

    private final HCF plugin;

    //TODO: add help argument

    public EventExecutor(HCF plugin) {
        super("event", Rank.ADMIN);

        addSubCommands(
                new EventAddLootTableArgument(plugin),
                new EventCancelArgument(plugin),
                new EventCreateArgument(plugin),
                new EventDeleteArgument(plugin),
                new EventDelLootTableArgument(plugin),
                new EventRenameArgument(plugin),
                new EventSetAreaArgument(plugin),
                new EventSetCapzoneArgument(plugin),
                new EventSetLootArgument(plugin),
                new EventStartArgument(plugin),
                new EventUptimeArgument(plugin),
                new EventHelpArgument(this)
        );

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        verify(sender, args);
    }

}
