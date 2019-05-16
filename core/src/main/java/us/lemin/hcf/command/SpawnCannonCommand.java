package us.lemin.hcf.command;

import com.google.common.collect.ImmutableList;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.utils.misc.BukkitUtils;
import us.lemin.core.utils.misc.JavaUtils;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.WarzoneFaction;
import us.lemin.hcf.util.ConfigurationService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Command used to launch or aim at a position within the Warzone.
 */
public class SpawnCannonCommand extends PlayerCommand {

    private static final Material SPAWN_CANNON_BLOCK = Material.BEACON;

    private final HCF plugin;

    public SpawnCannonCommand(HCF plugin) {
        super("spawncannon");
        this.plugin = plugin;
    }


    /**
     * Launches the player into the warzone from the spawn cannon.
     *
     * @param player   the player to launch
     * @param location the expected launch landing location
     */
    public void launchPlayer(Player player, Location location) {
        Faction factionAt = plugin.getFactionManager().getFactionAt(location);

        if (!(factionAt instanceof WarzoneFaction)) {
            player.sendMessage(ChatColor.RED + "You can only cannon to areas in the Warzone.");
            return;
        }

        int x = location.getBlockX();
        int z = location.getBlockZ();

        int maxDistance = getMaxCannonDistance(player);

        if (Math.abs(x) > maxDistance || Math.abs(z) > maxDistance) {
            player.sendMessage(ChatColor.RED + "You cannot launch that far from the spawn cannon. Your limit is " + maxDistance + '.');
            return;
        }

        location = BukkitUtils.getHighestLocation(location).add(0, 3, 0);
        player.sendMessage(ChatColor.YELLOW + "Cannoning to " + ChatColor.GREEN + x + ", " + z + ChatColor.YELLOW + '.');
        player.playSound(location, Sound.ENDERMAN_TELEPORT, 1, 1);
        player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND);
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 80, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 80, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1));
    }

    /**
     * Gets the maximum cannon distance a player can go too.
     *
     * @param sender the sender to check for
     * @return the maximum cannon distance for player
     */
    public int getMaxCannonDistance(CommandSender sender) {
        int decrement = 50;
        int radius = ((plugin.getConfiguration().getWarzoneRadiusOverworld() + decrement - 1) / decrement) * decrement;
        for (int i = radius; i > 0; i -= decrement) {
            if (sender.hasPermission("hcf.spawncannon." + i)) {
                return i;
            }
        }

        return 100;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return args.length == 1 ? BukkitUtils.getCompletions(args, COMPLETIONS) : Collections.emptyList();
    }

    private static final ImmutableList<String> COMPLETIONS = ImmutableList.of("aim", "launch");

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /" + getName() + " <launch|aim [x z])>");
            return;
        }

        World world = player.getWorld();

        if (world.getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(ChatColor.RED + "You can only use the spawn cannon in the overworld.");
            return;
        }

        Location location = player.getLocation();

        if (location.getBlock().getRelative(BlockFace.DOWN).getType() != SPAWN_CANNON_BLOCK) {
            player.sendMessage(ChatColor.RED + "You are not on a spawn cannon (" + ChatColor.AQUA + SPAWN_CANNON_BLOCK.name() + ChatColor.RED + ").");
            return;
        }

        if (!plugin.getFactionManager().getFactionAt(location).isSafezone()) {
            player.sendMessage(ChatColor.RED + "You can only use the spawn cannon in safe-zones.");
            return;
        }

        if (args[0].equalsIgnoreCase("aim")) {
            if (!player.hasPermission(this.getPermission() + ".aim")) {
                player.sendMessage(ChatColor.RED + "You do not have access to aim the spawn cannon.");
                return;
            }

            if (args.length < 3) {
                player.sendMessage(ChatColor.RED + "Usage: /" + getName() + ' ' + args[0].toLowerCase() + " <x> <z>");
                return;
            }

            Integer x = JavaUtils.tryParseInt(args[1]);
            Integer z; // lazy load
            if (x == null || (z = JavaUtils.tryParseInt(args[2])) == null) {
                player.sendMessage(ChatColor.RED + "Your x or z co-ordinate was invalid.");
                return;
            }

            launchPlayer(player, new Location(world, x, 0, z));
            return;
        }

        if (args[0].equalsIgnoreCase("launch")) {
            if (!player.hasPermission(this.getPermission() + ".launch")) {
                player.sendMessage(ChatColor.RED + "You do not have access to launch with the spawn cannon.");
                return;
            }

            int min = ConfigurationService.SPAWN_RADIUS_MAP.get(world.getEnvironment());
            int max = plugin.getConfiguration().getWarzoneRadiusOverworld();
            int maxCannonDistance = getMaxCannonDistance(player);
            ThreadLocalRandom random = ThreadLocalRandom.current();

            int x = Math.max(random.nextInt(Math.min(max, maxCannonDistance)), min);
            if (random.nextBoolean()) x = -x;

            int z = Math.max(random.nextInt(Math.min(max, maxCannonDistance)), min);
            if (random.nextBoolean()) z = -z;

            launchPlayer(player, new Location(world, x, 0, z));
            return;
        }

        player.sendMessage(ChatColor.RED + "Usage: /" + getName() + " <launch|aim [x z])>");
    }
}
