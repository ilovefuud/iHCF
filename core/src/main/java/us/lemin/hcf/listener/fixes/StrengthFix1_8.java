package us.lemin.hcf.listener.fixes;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class StrengthFix1_8 implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageStrengthNerf(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();

        for (final PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                final ItemStack heldItem = player.getItemInHand() != null ? player.getItemInHand() : new ItemStack(Material.AIR);

                final int sharpnessLevel = heldItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
                final int strengthLevel = effect.getAmplifier() + 1;

                final double totalDamage = event.getDamage();
                final double weaponDamage = (totalDamage - 1.25 * sharpnessLevel) / (1.0 + 1.3 * strengthLevel) - 1.0;
                final double finalDamage = 1.0 + weaponDamage + 1.25 * sharpnessLevel + (1 * 2) * strengthLevel;

                event.setDamage(finalDamage);
                break;
            }
        }
    }
}
