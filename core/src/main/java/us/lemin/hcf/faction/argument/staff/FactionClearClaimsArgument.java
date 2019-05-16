package us.lemin.hcf.faction.argument.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.type.ClaimableFaction;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;

/**
 * Faction argument used to set the DTR Regeneration cooldown of {@link Faction}s.
 */
public class FactionClearClaimsArgument extends SubCommand {

    private final ConversationFactory factory;
    private final HCF plugin;

    public FactionClearClaimsArgument(final HCF plugin) {
        super("clearclaims", "Clears the claims of a faction.", Rank.ADMIN);
        this.plugin = plugin;
        this.factory = new ConversationFactory(plugin).
                withFirstPrompt(new ClaimClearAllPrompt(plugin)).
                withEscapeSequence("/no").
                withTimeout(10).
                withModality(false).
                withLocalEcho(true);
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName|factionName|all>";
    }



    @Override
    public void execute(CommandSender commandSender, Player player, String[] args, String label) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        if (args[1].equalsIgnoreCase("all")) {
            if (!(player instanceof ConsoleCommandSender)) {
                player.sendMessage(ChatColor.RED + "This command can be only executed from console.");
                return;
            }

            player.beginConversation(factory.buildConversation(player));
            return;
        }

        Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (faction == null) {
            player.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        if (faction instanceof ClaimableFaction) {
            ClaimableFaction claimableFaction = (ClaimableFaction) faction;
            claimableFaction.removeClaims(claimableFaction.getClaims(), player);
            if (claimableFaction instanceof PlayerFaction) {
                ((PlayerFaction) claimableFaction).broadcast(ChatColor.GOLD.toString() + ChatColor.BOLD + "Your claims have been forcefully wiped by " + player.getName() + '.');
            }
        }

        player.sendMessage(ChatColor.GOLD.toString() + ChatColor.BOLD + "Claims belonging to " + faction.getName() + " have been forcefully wiped.");

    }

    private static class ClaimClearAllPrompt extends StringPrompt {

        private final HCF plugin;

        public ClaimClearAllPrompt(HCF plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.YELLOW + "Are you sure you want to do this? " + ChatColor.RED + ChatColor.BOLD + "All claims" + ChatColor.YELLOW + " will be cleared. " +
                    "Type " + ChatColor.GREEN + "yes" + ChatColor.YELLOW + " to confirm or " + ChatColor.RED + "no" + ChatColor.YELLOW + " to deny.";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String string) {
            switch (string.toLowerCase()) {
                case "yes": {
                    for (Faction faction : plugin.getFactionManager().getFactions()) {
                        if (faction instanceof ClaimableFaction) {
                            ClaimableFaction claimableFaction = (ClaimableFaction) faction;
                            claimableFaction.removeClaims(claimableFaction.getClaims(), Bukkit.getConsoleSender());
                        }
                    }

                    Conversable conversable = context.getForWhom();
                    Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD +
                            "All claims have been cleared" + (conversable instanceof CommandSender ? " by " + ((CommandSender) conversable).getName() : "") + '.');

                    return Prompt.END_OF_CONVERSATION;
                }
                case "no": {
                    context.getForWhom().sendRawMessage(ChatColor.BLUE + "Cancelled the process of clearing all faction claims.");
                    return Prompt.END_OF_CONVERSATION;
                }
                default: {
                    context.getForWhom().sendRawMessage(ChatColor.RED + "Unrecognized response. Process of clearing all faction claims cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }
            }
        }
    }
}
