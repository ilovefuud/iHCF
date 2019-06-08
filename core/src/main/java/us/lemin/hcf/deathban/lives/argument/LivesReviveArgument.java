package us.lemin.hcf.deathban.lives.argument;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import us.lemin.core.commands.SubCommand;
import us.lemin.core.utils.message.Messages;
import us.lemin.core.utils.plugin.TaskUtil;
import us.lemin.core.utils.profile.ProfileUtil;
import us.lemin.hcf.HCF;
import us.lemin.hcf.deathban.Deathban;
import us.lemin.hcf.faction.struct.Relation;
import us.lemin.hcf.faction.type.PlayerFaction;
import us.lemin.hcf.user.FactionUser;

import java.util.UUID;

/**
 * A {@link SubCommand} used to revive {@link Deathban}ned {@link Player}s.
 */
public class LivesReviveArgument extends SubCommand {

    private static final String REVIVE_BYPASS_PERMISSION = "hcf.revive.bypass";
    private static final String PROXY_CHANNEL_NAME = "BungeeCord";

    private final HCF plugin;

    public LivesReviveArgument(HCF plugin) {
        super("revive", "Revive a death-banned player");
        this.plugin = plugin;
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, PROXY_CHANNEL_NAME);
    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName() + " <playerName>";
    }

    @Override
    public void execute(CommandSender sender, Player player, String[] args, String label) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: " + getUsage(label));
            return;
        }

        TaskUtil.runAsync(plugin, () -> {
            ProfileUtil.MojangProfile profile = ProfileUtil.lookupProfile(args[1]);
            if (profile == null) {
                sender.sendMessage(Messages.PLAYER_NOT_FOUND);
                return;
            }

            UUID targetUUID = profile.getId();
            FactionUser factionTarget = plugin.getUserManager().getUser(targetUUID);
            Deathban deathban = factionTarget.getDeathban();

            if (deathban == null || !deathban.isActive()) {
                sender.sendMessage(ChatColor.RED + profile.getName() + " is not death-banned.");
                return;
            }

            Relation relation = Relation.NEUTRAL;
            if (sender instanceof Player) {
                if (!sender.hasPermission(REVIVE_BYPASS_PERMISSION)) {
                    if (plugin.getEotwHandler().isEndOfTheWorld()) {
                        sender.sendMessage(ChatColor.RED + "You cannot revive players during EOTW.");
                        return;
                    }
                }

                Player playerSender = (Player) sender;
                UUID playerUUID = playerSender.getUniqueId();
                int selfLives = plugin.getDeathbanManager().getLives(playerUUID);

                if (selfLives <= 0) {
                    sender.sendMessage(ChatColor.RED + "You do not have any lives.");
                    return;
                }

                plugin.getDeathbanManager().setLives(playerUUID, selfLives - 1);
                PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(playerSender);
                relation = playerFaction == null ? Relation.NEUTRAL : playerFaction.getFactionRelation(plugin.getFactionManager().getPlayerFaction(targetUUID));
                sender.sendMessage(ChatColor.YELLOW + "You have used a life to revive " + relation.toChatColour() + profile.getName() + ChatColor.YELLOW + '.');
            } else {
                sender.sendMessage(ChatColor.YELLOW + "You have revived " + plugin.getConfiguration().getRelationColourTeammate() + profile.getName() + ChatColor.YELLOW + '.');
            }

            if (sender instanceof PluginMessageRecipient) {
                //NOTE: This server needs at least 1 player online.
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Message");
                out.writeUTF(args[1]);

                String serverDisplayName = ChatColor.GREEN + "HCF"; //TODO: Non hard-coded server display name.
                out.writeUTF(relation.toChatColour() + sender.getName() + ChatColor.GOLD + " has just revived you from " + serverDisplayName + ChatColor.GOLD + '.');
                ((PluginMessageRecipient) sender).sendPluginMessage(plugin, PROXY_CHANNEL_NAME, out.toByteArray());
            }

            factionTarget.removeDeathban();
        });
    }
}
