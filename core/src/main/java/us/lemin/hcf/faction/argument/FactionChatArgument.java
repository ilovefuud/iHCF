package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.struct.ChatChannel;
import us.lemin.hcf.faction.type.PlayerFaction;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class FactionChatArgument extends SubCommand {

    private final HCF plugin;

    public FactionChatArgument(HCF plugin) {
        super("chat", "Switch between different chat modes.");
        this.plugin = plugin;
        this.aliases = new String[]{"c"};
        this.playerOnly = true;

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " [fac|public|ally] [message]";
    }


    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(player);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        FactionMember member = playerFaction.getMember(player.getUniqueId());
        ChatChannel currentChannel = member.getChatChannel();
        ChatChannel parsed = args.length >= 2 ? ChatChannel.parse(args[1], null) : currentChannel.getRotation();

        if (parsed == null && currentChannel != ChatChannel.PUBLIC) {
            Collection<Player> recipients = playerFaction.getOnlinePlayers();
            if (currentChannel == ChatChannel.ALLIANCE) {
                for (PlayerFaction ally : playerFaction.getAlliedFactions()) {
                    recipients.addAll(ally.getOnlinePlayers());
                }
            }

            String format = String.format(currentChannel.getRawFormat(player), "", HCF.SPACE_JOINER.join(Arrays.copyOfRange(args, 1, args.length)));
            for (Player recipient : recipients) {
                recipient.sendMessage(format);
            }

            // spawn radius, border, allies, minigames,
            return;
        }

        ChatChannel newChannel = parsed == null ? currentChannel.getRotation() : parsed;
        member.setChatChannel(newChannel);

        player.sendMessage(ChatColor.YELLOW + "You are now in " + ChatColor.AQUA + Objects.requireNonNull(newChannel).getDisplayName().toLowerCase() + ChatColor.YELLOW + " chat mode.");
    }
}
