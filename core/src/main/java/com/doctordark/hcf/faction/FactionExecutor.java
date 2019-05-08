package com.doctordark.hcf.faction;

import com.doctordark.hcf.HCF;
import com.doctordark.hcf.command.sotw.impl.SotwEndCommand;
import com.doctordark.hcf.command.sotw.impl.SotwStartCommand;
import com.doctordark.hcf.faction.argument.*;
import com.doctordark.hcf.faction.argument.staff.*;
import com.doctordark.util.command.CommandArgument;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.commands.SubCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to handle the command and tab completion for the faction command.
 */
public class FactionExecutor extends PlayerCommand {

    private static final List<String> COMPLETIONS = ImmutableList.of("start", "end");

    public final Map<String, SubCommand> subCommandMap;

    public FactionExecutor(HCF plugin) {
        super("faction");
        setAliases("f", "fac", "clan", "squad", "team", "t");

        Map<String, SubCommand> subCommands = new HashMap<>();

        subCommands.put("accept", new SotwStartCommand(plugin));
        subCommands.put("end", new SotwEndCommand(plugin));
        subCommands.put("cancel", new SotwEndCommand(plugin));

        subCommandMap = ImmutableMap.copyOf(subCommands);

        addArgument(new FactionAcceptArgument(plugin));
        addArgument(new FactionAllyArgument(plugin));
        addArgument(new FactionAnnouncementArgument(plugin));
        addArgument(new FactionChatArgument(plugin));
        addArgument(new FactionChatSpyArgument(plugin));
        addArgument(new FactionClaimArgument(plugin));
        addArgument(new FactionClaimChunkArgument(plugin));
        addArgument(new FactionClaimForArgument(plugin));
        addArgument(new FactionClaimsArgument(plugin));
        addArgument(new FactionClearClaimsArgument(plugin));
        addArgument(new FactionCreateArgument(plugin));
        addArgument(new FactionDemoteArgument(plugin));
        addArgument(new FactionDepositArgument(plugin));
        addArgument(new FactionDisbandArgument(plugin));
        addArgument(new FactionSetDtrRegenArgument(plugin));
        addArgument(new FactionForceDemoteArgument(plugin));
        addArgument(new FactionForceJoinArgument(plugin));
        addArgument(new FactionForceKickArgument(plugin));
        addArgument(new FactionForceLeaderArgument(plugin));
        addArgument(new FactionForcePromoteArgument(plugin));
        addArgument(new FactionForceUnclaimHereArgument(plugin));
        addArgument(helpArgument = new FactionHelpArgument(this));
        addArgument(new FactionHomeArgument(this, plugin));
        addArgument(new FactionInviteArgument(plugin));
        addArgument(new FactionInvitesArgument(plugin));
        addArgument(new FactionKickArgument(plugin));
        addArgument(new FactionLeaderArgument(plugin));
        addArgument(new FactionLeaveArgument(plugin));
        addArgument(new FactionListArgument(plugin));
        addArgument(new FactionMapArgument(plugin));
        addArgument(new FactionMessageArgument(plugin));
        addArgument(new FactionMuteArgument(plugin));
        addArgument(new FactionOpenArgument(plugin));
        addArgument(new FactionRemoveArgument(plugin));
        addArgument(new FactionRenameArgument(plugin));
        addArgument(new FactionPromoteArgument(plugin));
        addArgument(new FactionSetDtrArgument(plugin));
        addArgument(new FactionSetDeathbanMultiplierArgument(plugin));
        addArgument(new FactionSetHomeArgument(plugin));
        addArgument(new FactionShowArgument(plugin));
        addArgument(new FactionStuckArgument(plugin));
        addArgument(new FactionSubclaimArgumentExecutor(plugin));
        addArgument(new FactionUnclaimArgument(plugin));
        addArgument(new FactionUnallyArgument(plugin));
        addArgument(new FactionUninviteArgument(plugin));
        addArgument(new FactionUnsubclaimArgument(plugin));
        addArgument(new FactionWithdrawArgument(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            helpArgument.onCommand(sender, command, label, args);
            return true;
        }

        CommandArgument argument = getArgument(args[0]);
        if (argument != null) {
            String permission = argument.getPermission();
            if (permission == null || sender.hasPermission(permission)) {
                argument.onCommand(sender, command, label, args);
                return true;
            }
        }

        helpArgument.onCommand(sender, command, label, args);
        return true;
    }

    @Override
    public void execute(Player player, String[] args) {
        String arg = args.length < 1 ? "help" : args[0].toLowerCase();
        SubCommand subCommand = subCommandMap.get(arg);
        Player target = args.length > 1 ? plugin.getServer().getPlayer(args[1]) : null;

        subCommand.execute(player, target, args, getLabel());
    }
}
