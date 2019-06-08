package us.lemin.hcf.faction;

import org.bukkit.command.CommandSender;
import us.lemin.core.commands.SubCommandExecutor;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.argument.*;
import us.lemin.hcf.faction.argument.staff.*;

/**
 * Class to handle the command and tab completion for the faction command.
 */
public class FactionExecutor extends SubCommandExecutor {


    public FactionExecutor(HCF plugin) {
        super("faction");
        setAliases("f", "fac", "clan", "squad", "team", "t");

        addSubCommands(
                new FactionAcceptArgument(plugin),
                new FactionAllyArgument(plugin),
                new FactionAnnouncementArgument(plugin),
                new FactionChatArgument(plugin),
                new FactionChatSpyArgument(plugin),
                new FactionClaimArgument(plugin),
                new FactionClaimChunkArgument(plugin),
                new FactionClaimForArgument(plugin),
                new FactionClaimsArgument(plugin),
                new FactionClearClaimsArgument(plugin),
                new FactionCreateArgument(plugin),
                new FactionDemoteArgument(plugin),
                new FactionDepositArgument(plugin),
                new FactionDisbandArgument(plugin),
                new FactionSetDtrRegenArgument(plugin),
                new FactionForceDemoteArgument(plugin),
                new FactionForceJoinArgument(plugin),
                new FactionForceKickArgument(plugin),
                new FactionForceLeaderArgument(plugin),
                new FactionForcePromoteArgument(plugin),
                new FactionForceUnclaimHereArgument(plugin),
                new FactionHelpArgument(this),
                new FactionHomeArgument(this, plugin),
                new FactionInviteArgument(plugin),
                new FactionInvitesArgument(plugin),
                new FactionKickArgument(plugin),
                new FactionLeaderArgument(plugin),
                new FactionLeaveArgument(plugin),
                new FactionListArgument(plugin),
                new FactionMapArgument(plugin),
                new FactionMessageArgument(plugin),
                new FactionMuteArgument(plugin),
                new FactionOpenArgument(plugin),
                new FactionRemoveArgument(plugin),
                new FactionRenameArgument(plugin),
                new FactionPromoteArgument(plugin),
                new FactionSetDtrArgument(plugin),
                new FactionSetDeathbanMultiplierArgument(plugin),
                new FactionSetHomeArgument(plugin),
                new FactionShowArgument(plugin),
                new FactionStuckArgument(plugin),
                new FactionSubclaimArgumentExecutor(plugin),
                new FactionUnclaimArgument(plugin),
                new FactionUnallyArgument(plugin),
                new FactionUninviteArgument(plugin),
                new FactionUnsubclaimArgument(plugin),
                new FactionWithdrawArgument(plugin)
                );
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        verify(sender, args);
    }
}
