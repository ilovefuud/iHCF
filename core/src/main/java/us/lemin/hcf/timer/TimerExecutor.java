package us.lemin.hcf.timer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommandExecutor;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.argument.TimerCheckArgument;
import us.lemin.hcf.timer.argument.TimerSetArgument;
import us.lemin.hcf.timer.type.InvincibilityTimer;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class TimerExecutor extends SubCommandExecutor {

    private final HCF plugin;

    //TODO: add help argument

    public TimerExecutor(HCF plugin) {
        super("timer", Rank.ADMIN);
        addSubCommands(
                new TimerSetArgument(plugin),
                new TimerCheckArgument(plugin)
        );
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
       verify(sender, args);
    }

}
