package us.lemin.hcf.faction.argument.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.type.Faction;

/**
 * Faction argument used to forcefully remove {@link Faction}s.
 */
public class FactionRemoveArgument extends SubCommand {

    private final ConversationFactory factory;
    private final HCF plugin;

    public FactionRemoveArgument(final HCF plugin) {
        super("remove", "Remove a faction.", Rank.ADMIN);
        this.plugin = plugin;
        this.aliases = new String[]{"delete", "forcedisband", "forceremove"};
        this.factory = new ConversationFactory(plugin).
                withFirstPrompt(new RemoveAllPrompt(plugin)).
                withEscapeSequence("/no").
                withTimeout(10).
                withModality(false).
                withLocalEcho(true);
    }


    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <all|factionName>";
    }


    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        if (args[1].equalsIgnoreCase("all")) {
            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage(ChatColor.RED + "This command can be only executed from console.");
                return;
            }

            Conversable conversable = (Conversable) sender;
            conversable.beginConversation(factory.buildConversation(conversable));
            return;
        }

        Faction faction = plugin.getFactionManager().getContainingFaction(args[1]);

        if (faction == null) {
            sender.sendMessage(ChatColor.RED + "Faction named or containing member with IGN or UUID " + args[1] + " not found.");
            return;
        }

        if (plugin.getFactionManager().removeFaction(faction, sender)) {
            Command.broadcastCommandMessage(sender, ChatColor.YELLOW + "Disbanded faction " + faction.getName() + ChatColor.YELLOW + '.');
        }

    }

    private static class RemoveAllPrompt extends StringPrompt {

        private final HCF plugin;

        public RemoveAllPrompt(HCF plugin) {
            this.plugin = plugin;
        }

        @Override
        public String getPromptText(ConversationContext context) {
            return ChatColor.YELLOW + "Are you sure you want to do this? " + ChatColor.RED + ChatColor.BOLD + "All factions" + ChatColor.YELLOW + " will be cleared. " +
                    "Type " + ChatColor.GREEN + "yes" + ChatColor.YELLOW + " to confirm or " + ChatColor.RED + "no" + ChatColor.YELLOW + " to deny.";
        }

        @Override
        public Prompt acceptInput(ConversationContext context, String string) {
            switch (string.toLowerCase()) {
                case "yes": {
                    for (Faction faction : plugin.getFactionManager().getFactions()) {
                        plugin.getFactionManager().removeFaction(faction, Bukkit.getConsoleSender());
                    }

                    Conversable conversable = context.getForWhom();
                    Bukkit.broadcastMessage(ChatColor.GOLD.toString() + ChatColor.BOLD +
                            "All factions have been disbanded" + (conversable instanceof CommandSender ? " by " + ((CommandSender) conversable).getName() : "") + '.');

                    return Prompt.END_OF_CONVERSATION;
                }
                case "no": {
                    context.getForWhom().sendRawMessage(ChatColor.BLUE + "Cancelled the process of disbanding all factions.");
                    return Prompt.END_OF_CONVERSATION;
                }
                default: {
                    context.getForWhom().sendRawMessage(ChatColor.RED + "Unrecognized response. Process of disbanding all factions cancelled.");
                    return Prompt.END_OF_CONVERSATION;
                }
            }
        }
    }
}
