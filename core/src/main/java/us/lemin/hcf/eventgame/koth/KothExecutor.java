package us.lemin.hcf.eventgame.koth;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommandExecutor;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.koth.argument.KothHelpArgument;
import us.lemin.hcf.eventgame.koth.argument.KothNextArgument;
import us.lemin.hcf.eventgame.koth.argument.KothScheduleArgument;
import us.lemin.hcf.eventgame.koth.argument.KothSetCapDelayArgument;
import us.lemin.hcf.timer.type.InvincibilityTimer;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class KothExecutor extends SubCommandExecutor {

    private final HCF plugin;

    //TODO: add help argument

    public KothExecutor(HCF plugin) {
        super("koth", Rank.ADMIN);

        addSubCommands(
                new KothHelpArgument(this),
                new KothNextArgument(plugin),
                new KothScheduleArgument(plugin),
                new KothSetCapDelayArgument(plugin)

        );


        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        verify(sender, args);
    }

}
