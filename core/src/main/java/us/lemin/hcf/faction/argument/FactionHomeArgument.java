package us.lemin.hcf.faction.argument;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import us.lemin.core.commands.SubCommand;
import us.lemin.hcf.HCF;
import us.lemin.hcf.eventgame.faction.EventFaction;
import us.lemin.hcf.faction.FactionExecutor;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;
import us.lemin.hcf.timer.PlayerTimer;
import us.lemin.hcf.util.DurationFormatter;

import java.util.UUID;

/**
 * Faction argument used to teleport to {@link Faction} home {@link Location}s.
 */
public class FactionHomeArgument extends SubCommand {

    private final FactionExecutor factionExecutor;
    private final HCF plugin;

    public FactionHomeArgument(FactionExecutor factionExecutor, HCF plugin) {
        super("home", "Teleport to the faction home.");
        this.factionExecutor = factionExecutor;
        this.plugin = plugin;
        this.playerOnly = true;

    }

    public String getUsage(String label) {
        return '/' + label + ' ' + getName();
    }

    @Override
    public void execute(CommandSender sender, Player target, String[] args, String label) {
        Player player;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else{
            return;
        }
        if (args.length >= 2 && args[1].equalsIgnoreCase("set")) {
            factionExecutor.getSubCommandMap().get("sethome").verify(player, target, args, label);
            return;
        }

        UUID uuid = player.getUniqueId();

        PlayerTimer timer = plugin.getTimerManager().getEnderPearlTimer();
        long remaining = timer.getRemaining(player);

        if (remaining > 0L) {
            player.sendMessage(ChatColor.RED + "You cannot warp whilst your " + timer.getName() + ChatColor.RED + " timer is active.");
            return;
        }

        if (!plugin.getConfiguration().isKitmap() && (timer = plugin.getTimerManager().getCombatTimer()).getRemaining(player) > 0L) {
            player.sendMessage(ChatColor.RED + "You cannot warp whilst your " + timer.getDisplayName() + ChatColor.RED + " timer is active.");
            return;
        }

        PlayerFaction playerFaction = plugin.getFactionManager().getPlayerFaction(uuid);

        if (playerFaction == null) {
            player.sendMessage(ChatColor.RED + "You are not in a faction.");
            return;
        }

        Location home = playerFaction.getHome();

        if (home == null) {
            player.sendMessage(ChatColor.RED + "Your faction does not have a home set.");
            return;
        }

        if (plugin.getConfiguration().getMaxHeightFactionHome() != -1 && home.getY() > plugin.getConfiguration().getMaxHeightFactionHome()) {
            player.sendMessage(ChatColor.RED + "Your faction home height is above the limit which is " + plugin.getConfiguration().getMaxHeightFactionHome() +
                    ", travel to your current home location at (x. " + home.getBlockX() + ", z. " + home.getBlockZ() + ") and re-set it at a lower height to fix this.");

            return;
        }

        Faction factionAt = plugin.getFactionManager().getFactionAt(player.getLocation());

        if (factionAt instanceof EventFaction) {
            player.sendMessage(ChatColor.RED + "You cannot warp whilst in event zones.");
            return;
        }

        if (factionAt != playerFaction && factionAt instanceof PlayerFaction && plugin.getConfiguration().isAllowTeleportingInEnemyTerritory()) {
            player.sendMessage(ChatColor.RED + "You may not warp in enemy claims. Use " + ChatColor.YELLOW + '/' + label + " stuck" + ChatColor.RED + " if trapped.");
            return;
        }

        long millis;
        if (factionAt.isSafezone()) {
            millis = 0L;
        } else {
            String worldName;
            switch (player.getWorld().getEnvironment()) {
                case THE_END:
                    worldName = "End";
                    millis = plugin.getConfiguration().getFactionHomeTeleportDelayEndMillis();
                    break;
                case NETHER:
                    worldName = "Nether";
                    millis = plugin.getConfiguration().getFactionHomeTeleportDelayNetherMillis();
                    break;
                case NORMAL:
                    worldName = "Overworld";
                    millis = plugin.getConfiguration().getFactionHomeTeleportDelayOverworldMillis();
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognised environment");
            }

            if (millis == -1L) {
                player.sendMessage(ChatColor.RED + "Home teleports are disabled in the " + worldName + ".");
                return;
            }
        }

        if (factionAt != playerFaction && factionAt instanceof PlayerFaction) {
            millis *= 2L;
        }

        plugin.getTimerManager().getTeleportTimer().teleport(player, home, millis,
                ChatColor.AQUA + "Teleporting to your faction home in " + ChatColor.LIGHT_PURPLE + DurationFormatter.getRemaining(millis, true, false) + ChatColor.AQUA + ". Do not move or take damage.",
                PlayerTeleportEvent.TeleportCause.COMMAND);
    }
}
