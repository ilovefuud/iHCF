package us.lemin.hcf.deathban.lives;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommandExecutor;
import us.lemin.hcf.HCF;
import us.lemin.hcf.deathban.lives.argument.*;
import us.lemin.hcf.timer.type.InvincibilityTimer;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class LivesExecutor extends SubCommandExecutor {

    private final HCF plugin;
    //TODO: add help argument

    public LivesExecutor(HCF plugin) {
        super("lives");

        addSubCommands(
                new LivesCheckArgument(plugin),
                new LivesCheckDeathbanArgument(plugin),
                new LivesClearDeathbansArgument(plugin),
                new LivesGiveArgument(plugin),
                new LivesReviveArgument(plugin),
                new LivesSetArgument(plugin),
                new LivesSetDeathbanTimeArgument(plugin),
                new LivesHelpArgument(this)
        );

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        verify(sender, args);
    }

}
