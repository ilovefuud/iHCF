package us.lemin.hcf.faction;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.BaseCommand;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.argument.*;
import us.lemin.hcf.faction.argument.staff.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to handle the command and tab completion for the faction command.
 */
public class FactionExecutor extends BaseCommand {

    private static final List<String> COMPLETIONS = ImmutableList.of("start", "end");

    public final Map<String, SubCommand> subCommandMap;

    public FactionExecutor(HCF plugin) {
        super("faction");
        setAliases("f", "fac", "clan", "squad", "team", "t");

        Map<String, SubCommand> subCommands = new HashMap<>();

        subCommands.put("accept", new FactionAcceptArgument(plugin));
        subCommands.put("ally", new FactionAllyArgument(plugin));
        subCommands.put("announcement", new FactionAnnouncementArgument(plugin));
        subCommands.put("chat", new FactionChatArgument(plugin));
        subCommands.put("claim", new FactionClaimArgument(plugin));
        subCommands.put("claimchunk", new FactionClaimChunkArgument(plugin));
        subCommands.put("claims", new FactionClaimsArgument(plugin));
        subCommands.put("create", new FactionCreateArgument(plugin));
        subCommands.put("demote", new FactionDemoteArgument(plugin));
        subCommands.put("deposit", new FactionDepositArgument(plugin));
        subCommands.put("disband", new FactionDisbandArgument(plugin));
        subCommands.put("help", new FactionHelpArgument(this));
        subCommands.put("home", new FactionHomeArgument(this, plugin));
        subCommands.put("invite", new FactionInviteArgument(plugin));
        subCommands.put("invtes", new FactionInvitesArgument(plugin));
        subCommands.put("kick", new FactionKickArgument(plugin));
        subCommands.put("leader", new FactionLeaderArgument(plugin));
        subCommands.put("leave", new FactionLeaveArgument(plugin));
        subCommands.put("list", new FactionListArgument(plugin));
        subCommands.put("map", new FactionMapArgument(plugin));
        subCommands.put("message", new FactionMessageArgument(plugin));
        subCommands.put("open", new FactionOpenArgument(plugin));
        subCommands.put("promote", new FactionPromoteArgument(plugin));
        subCommands.put("rename", new FactionRenameArgument(plugin));
        subCommands.put("sethome", new FactionSetHomeArgument(plugin));
        subCommands.put("show", new FactionShowArgument(plugin));
        subCommands.put("stuck", new FactionStuckArgument(plugin));
        subCommands.put("subclaim", new FactionSubclaimArgumentExecutor(plugin));
        subCommands.put("unally", new FactionUnallyArgument(plugin));
        subCommands.put("unclaim", new FactionUnclaimArgument(plugin));
        subCommands.put("uninvite", new FactionUninviteArgument(plugin));
        subCommands.put("unsubclaim", new FactionUnsubclaimArgument(plugin));
        subCommands.put("withdraw", new FactionWithdrawArgument(plugin));
        subCommands.put("chatspy", new FactionChatSpyArgument(plugin));
        subCommands.put("claimfor", new FactionClaimForArgument(plugin));
        subCommands.put("clearclaims", new FactionClearClaimsArgument(plugin));
        subCommands.put("edit", new FactionEditArgument(plugin));
        subCommands.put("forcedemote", new FactionForceDemoteArgument(plugin));
        subCommands.put("forcejoin", new FactionForceJoinArgument(plugin));
        subCommands.put("forcekick", new FactionForceKickArgument(plugin));
        subCommands.put("forceleader", new FactionForceLeaderArgument(plugin));
        subCommands.put("forcepromote", new FactionForcePromoteArgument(plugin));
        subCommands.put("forceunclaimhere", new FactionForceUnclaimHereArgument(plugin));
        subCommands.put("mute", new FactionMuteArgument(plugin));
        subCommands.put("remove", new FactionRemoveArgument(plugin));
        subCommands.put("setdeathbanmultiplier", new FactionSetDeathbanMultiplierArgument(plugin));
        subCommands.put("setdtr", new FactionSetDtrArgument(plugin));
        subCommands.put("setdtrregen", new FactionSetDtrRegenArgument(plugin));


        subCommandMap = ImmutableMap.copyOf(subCommands);
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        String arg = args.length < 1 ? "help" : args[0].toLowerCase();
        SubCommand subCommand = subCommandMap.get(arg);

        if (subCommand == null) {
            for (SubCommand loop : subCommandMap.values()) {
                if (loop.getAliases() == null) continue;
                if (Arrays.stream(loop.getAliases())
                        .anyMatch(arg::equalsIgnoreCase)) {
                    Player target = args.length > 1 ? plugin.getServer().getPlayer(args[1]) : null;
                    loop.execute(sender, target, args, getLabel());
                    break;
                }
            }
        } else {
            Player target = args.length > 1 ? plugin.getServer().getPlayer(args[1]) : null;
            subCommand.execute(sender, target, args, getLabel());
        }


    }
}
