package us.lemin.hcf.command.sotw;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import us.lemin.core.commands.SubCommandExecutor;
import us.lemin.core.utils.misc.BukkitUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.command.sotw.impl.SotwEndCommand;
import us.lemin.hcf.command.sotw.impl.SotwStartCommand;

import java.util.Collections;
import java.util.List;

public class SotwCommand extends SubCommandExecutor {

    private static final List<String> COMPLETIONS = ImmutableList.of("start", "end");

    private final HCF plugin;

    public SotwCommand(HCF plugin) {
        super("sotw");
        this.plugin = plugin;
        addSubCommands(new SotwStartCommand(plugin), new SotwEndCommand(plugin));

    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return args.length == 1 ? BukkitUtils.getCompletions(args, COMPLETIONS) : Collections.emptyList();

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        verify(sender, args);
    }
}
