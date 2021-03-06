package us.lemin.hcf.timer.type;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import us.lemin.hcf.HCF;
import us.lemin.hcf.timer.PlayerTimer;
import us.lemin.hcf.util.DurationFormatter;
import us.lemin.hcf.util.imagemessage.ImageChar;
import us.lemin.hcf.util.imagemessage.ImageMessage;

import javax.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * Timer used to prevent {@link Player}s from using Notch Apples too often.
 */
public class GappleTimer extends PlayerTimer implements Listener {

    private final HCF plugin;

    private ImageMessage goppleArtMessage;

    public GappleTimer(HCF plugin) {
        super("Gapple", plugin.getConfiguration().isKitmap() ? TimeUnit.MINUTES.toMillis(10) : TimeUnit.HOURS.toMillis(6L));

        this.plugin = plugin;

        if (plugin.getImageFolder().getGopple() != null) {
            goppleArtMessage = ImageMessage.newInstance(plugin.getImageFolder().getGopple(), 8, ImageChar.BLOCK.getChar()).appendText("", "",
                    ChatColor.GOLD.toString() + ChatColor.BOLD + ' ' + name + ':',
                    ChatColor.GRAY + "  Consumed",
                    ChatColor.GOLD + " Cooldown Remaining:",
                    ChatColor.GRAY + "  " + DurationFormatUtils.formatDurationWords(defaultCooldown, true, true)
            );
        }
    }

    @Override
    public String getScoreboardPrefix() {
        return plugin.getConfiguration().getScoreboardSidebarTimerGapplePrefix();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        ItemStack stack = event.getItem();
        if (stack != null && stack.getType() == Material.GOLDEN_APPLE && stack.getDurability() == 1) {
            Player player = event.getPlayer();
            if (setCooldown(player, player.getUniqueId(), defaultCooldown, false, new Predicate<Long>() {
                @Override
                public boolean test(@Nullable Long value) {
                    return false;
                }
            })) {

                if (goppleArtMessage != null) {
                    goppleArtMessage.sendToPlayer(player);
                } else {
                    player.sendMessage(ChatColor.YELLOW + "Consumed " + ChatColor.GOLD + "Golden Apple" + ChatColor.YELLOW + ", now on a cooldown for " +
                            DurationFormatUtils.formatDurationWords(defaultCooldown, true, true));
                }
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You still have a " + getDisplayName() + ChatColor.RED + " cooldown for another " +
                        ChatColor.BOLD + DurationFormatter.getRemaining(getRemaining(player), true, false) + ChatColor.RED + '.');
            }
        }
    }
}
