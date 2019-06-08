package us.lemin.hcf.listener;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.misc.BukkitUtils;
import us.lemin.hcf.HCF;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class DeathMessageListener implements Listener {

    private final HCF plugin;
    private final Map<UUID, DeathInformation> lastAttackerMap;

    public DeathMessageListener(HCF plugin) {
        this.plugin = plugin;
        this.lastAttackerMap = new HashMap<>();
    }

    @Getter
    class DeathInformation {
        private final WeakReference<Player> attacker;
        private final EntityDamageEvent.DamageCause damageCause;
        private final ItemStack attackedItem;

        public DeathInformation(WeakReference<Player> attacker, EntityDamageEvent.DamageCause damageCause, ItemStack attackedItem) {
            this.attacker = attacker;
            this.damageCause = damageCause;
            this.attackedItem = attackedItem;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastAttackerMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        lastAttackerMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player attacker = BukkitUtils.getFinalAttacker(event, true);
            if (attacker != null) {
                lastAttackerMap.put(event.getEntity().getUniqueId(), new DeathInformation(new WeakReference<>(attacker), event.getCause(), attacker.getItemInHand()));
            } else {
                lastAttackerMap.put(event.getEntity().getUniqueId(), new DeathInformation(null, event.getCause(), null));
            }
        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        DeathInformation information = lastAttackerMap.get(event.getEntity().getUniqueId());
        String deathMessage;
        if (information.getAttacker().get() != null) {
            switch (information.getDamageCause()) {
                default:
                case ENTITY_ATTACK:
                    deathMessage = "%VICTIM% was struck down by %KILLER%";
                    break;
                case PROJECTILE:
                    deathMessage = "%VICTIM% was shot by %KILLER%";
                    break;
                case FALL:
                    deathMessage = "%VICTIM% was knocked off a ledge by %KILLER%";
                    break;
            }
        } else {
            switch (information.getDamageCause()) {
                default:
                case ENTITY_ATTACK:
                    deathMessage = "%VICTIM% was struck down by a monster";
                    break;
                case PROJECTILE:
                    deathMessage = "%VICTIM% was shot by a monster";
                    break;
                case FALL:
                    deathMessage = "%VICTIM% has fallen to their death";
                    break;
            }
        }
    }

}
