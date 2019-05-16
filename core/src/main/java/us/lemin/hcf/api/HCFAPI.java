package us.lemin.hcf.api;

import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import us.lemin.hcf.Configuration;
import us.lemin.hcf.HCF;
import us.lemin.hcf.faction.FactionMember;
import us.lemin.hcf.faction.claim.Claim;
import us.lemin.hcf.faction.type.Faction;
import us.lemin.hcf.faction.type.PlayerFaction;
import us.lemin.hcf.listener.ProtectionListener;
import us.lemin.hcf.user.FactionUser;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class HCFAPI {

    /**
     * @param playerFaction The faction whose home location you wish to receive.
     * @return Faction's home location.
     */
    public static Location getHomeLocation(PlayerFaction playerFaction) {
        if (playerFaction.getHome() != null) {
            return playerFaction.getHome();
        }
        return null;
    }

    /**
     * Gets a {@link PlayerFaction} of a {@link org.bukkit.entity.Player}.
     *
     * @param player the {@link org.bukkit.entity.Player} to lookup
     * @return the {@link PlayerFaction} containing {@link org.bukkit.entity.Player}
     * @deprecated use of {@link org.bukkit.Bukkit#getOfflinePlayer(UUID)} in main thread
     */
    public static PlayerFaction getPlayerTeam(Player player) {
        return HCF.getPlugin().getFactionManager().getPlayerFaction(player);
    }

    /**
     * Gets all the available {@link Faction}s held by the manager.
     *
     * @return an immutable list of {@link Faction}s
     */
    public static ImmutableList<Faction> getTeams() {
        return HCF.getPlugin().getFactionManager().getFactions();
    }

    /**
     * Gets the faction which owns a {@link Claim} at the position of a given {@link Block}.
     *
     * @param block the {@link Block} to get at
     * @return the {@link Faction} owning the claim at {@link Block}
     */
    public static Faction getTeamAtBlock(Block block) {
        return HCF.getPlugin().getFactionManager().getFactionAt(block);
    }

    /**
     * Gets the faction which owns a {@link Claim} at a
     * given {@link Location}.
     *
     * @param location the {@link Location} to check
     * @return the {@link Faction} owning the claim at {@link Location}
     */
    public static Faction getTeamAtLocation(Location location) {
        return HCF.getPlugin().getFactionManager().getFactionAt(location);
    }

    /**
     * Gets a {@link Faction} which owns a {@link Claim} at specific
     * co-ordinates.
     *
     * @param world the {@link World} to get for
     * @param x     the x coordinate to get at
     * @param z     the z coordinate to get at
     * @return the {@link Faction} owning the {@link Claim}
     */
    public static Faction getTeamAtWorld(World world, int x, int z) {
        return HCF.getPlugin().getFactionManager().getFactionAt(world, x, z);
    }

    /**
     * @param playerFaction The faction whose DTR you wish to receive.
     * @return DTR.
     */
    public static double getDeathsUntilRaidable(PlayerFaction playerFaction) {
        if (playerFaction != null) {
            return playerFaction.getDeathsUntilRaidable();
        }
        return 0.0;
    }

    /**
     * @param playerFaction The faction whose maximum DTR you wish to receive.
     * @return Faction's maximum DTR.
     */
    public static double getMaximumDeathsUntilRaidable(PlayerFaction playerFaction) {
        if (playerFaction != null) {
            return playerFaction.getMaximumDeathsUntilRaidable();
        }
        return 0.0;
    }

    /**
     * @param player The player whose faction's online members you wish to receive.
     * @return Faction's online players.
     */
    public static Set<Player> getTeamOnlinePlayers(Player player) {
        return HCF.getPlugin().getFactionManager().getPlayerFaction(player).getOnlinePlayers();
    }

    /**
     * @param player The player whose faction's online members you wish to receive.
     * @return Faction's online members.
     */
    public static Map<UUID, FactionMember> getTeamOnlineMembers(Player player) {
        return HCF.getPlugin().getFactionManager().getPlayerFaction(player).getOnlineMembers();
    }

    /**
     * @param player The player whose faction's members you wish to receive.
     * @return Faction's members.
     */
    public static Map<UUID, FactionMember> getTeamMembers(Player player) {
        return HCF.getPlugin().getFactionManager().getPlayerFaction(player).getMembers();
    }

    /**
     * @param playerFaction The player whose faction's name you wish to receive.
     * @return Faction's balance.
     */
    public static int getTeamBalance(PlayerFaction playerFaction) {
        if (playerFaction != null) {
            return playerFaction.getBalance();
        }
        return -1;
    }

    /**
     * @param player The player whose faction's name you wish to receive.
     * @return Faction's name.
     */
    public static String getTeamName(Player player) {
        if (HCF.getPlugin().getFactionManager().getPlayerFaction(player) != null) {
            return HCF.getPlugin().getFactionManager().getPlayerFaction(player).getName();
        }
        return null;
    }

    /**
     * @param uuid The uuid of the player's balance you wish to receive.
     * @return Amount of money.
     */
    public static int getPlayerBalance(UUID uuid) {
        return HCF.getPlugin().getEconomyManager().getBalance(uuid);
    }

    /**
     * @param uuid   The uuid of the player's balance you wish to modify.
     * @param amount The amount of money you wish to set the player's balance to.
     * @return Amount of money after modification.
     */
    public static int setPlayerBalance(UUID uuid, int amount) {
        return HCF.getPlugin().getEconomyManager().setBalance(uuid, amount);
    }

    /**
     * @param uuid   The uuid of the player's balance you wish to add x amount of money to.
     * @param amount The amount of money you wish to add to the player's balance
     * @return Amount of money after addition.
     */
    public static int addPlayerBalance(UUID uuid, int amount) {
        return HCF.getPlugin().getEconomyManager().addBalance(uuid, amount);
    }

    /**
     * @param uuid   The uuid of the player's balance you wish to subtract x amount of money from.
     * @param amount The amount of money you wish to subtract from the player's balance
     * @return Amount of money after subtraction.
     */
    public static int subtractPlayerBalance(UUID uuid, int amount) {
        return HCF.getPlugin().getEconomyManager().subtractBalance(uuid, amount);
    }

    /**
     * @param player   The player whose staff mode you wish to check.
     * @return Whether or not the player is in staff mode.
     */
    public static boolean isStaffMode(Player player) {
        return isStaffMode(player.getUniqueId());
    }
    /**
     * @param uuid   The uuid of the player whose staff mode you wish to check.
     * @return Whether or not the player is in staff mode.
     */
    public static boolean isStaffMode(UUID uuid) {
        return HCF.getPlugin().getUserManager().getUser(uuid).isStaffMode();
    }
    /**
     * @param player   The player whose staffmode you wish to set.
     * @param staffMode   The value you wish to set the player's staff mode to.
     * */
    public static void setStaffMode(Player player, boolean staffMode) {
        setStaffMode(player.getUniqueId(), staffMode);
    }

    /**
     * @param uuid   The uuid of the player whose staffmode you wish to set.
     * @param staffMode   The value you wish to set the player's staff mode to.
     */
    public static void setStaffMode(UUID uuid, boolean staffMode) {
        HCF.getPlugin().getUserManager().getUser(uuid).setStaffMode(staffMode);
    }
    /**
     * @param player   The player whose staffmode you wish to check.
     * @return Whether or not the player is in staffmode.
     */
    public static boolean toggleStaffMode(Player player, boolean staffMode) {
        return toggleStaffMode(player, staffMode);
    }
    /**
     * @param uuid   The uuid of the player whose staffmode you wish to toggle.
     * @return Whether or not the player is in staffmode.
     */
    public static boolean toggleStaffMode(UUID uuid, boolean staffMode) {
        FactionUser user = HCF.getPlugin().getUserManager().getUser(uuid);
        user.setStaffMode(!isStaffMode(uuid));
        return user.isStaffMode();
    }

    public static Configuration getConfig() {
        return HCF.getPlugin().getConfiguration();
    }

    public static void tryAlert(Player player, String string) {
        ProtectionListener.tryAlert(player, string);
    }
}
