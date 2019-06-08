package us.lemin.hcf.eventgame.conquest;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommandExecutor;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.type.InvincibilityTimer;

/**
 * Command used to manage the {@link InvincibilityTimer} of {@link Player}s.
 */
public class ConquestExecutor extends SubCommandExecutor {

    private final HCF plugin;

    //TODO: add help argument

    public ConquestExecutor(HCF plugin) {
        super("conquest", Rank.ADMIN);
        this.plugin = plugin;
        addSubCommand(new ConquestSetpointsArgument(plugin));
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
       verify(sender, args);
    }

}
