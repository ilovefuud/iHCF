package us.lemin.hcf.util;

import net.minecraft.server.v1_8_R3.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class LangUtil {

    private Map<Enchantment, String> enchantmentNames = new HashMap<>();
    private Map<PotionEffectType, String> potionNames = new HashMap<>();


    public LangUtil() {
        enchantmentNames.put(Enchantment.ARROW_DAMAGE, "Power");
        enchantmentNames.put(Enchantment.ARROW_FIRE, "Flame");
        enchantmentNames.put(Enchantment.ARROW_INFINITE, "Infinity");
        enchantmentNames.put(Enchantment.ARROW_KNOCKBACK, "Punch");
        enchantmentNames.put(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection");
        enchantmentNames.put(Enchantment.PROTECTION_EXPLOSIONS, "Blast Protection");
        enchantmentNames.put(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection");
        enchantmentNames.put(Enchantment.PROTECTION_FIRE, "Fire Protection");
        enchantmentNames.put(Enchantment.PROTECTION_FALL, "Feather Falling");
        enchantmentNames.put(Enchantment.DAMAGE_ALL, "Sharpness");
        enchantmentNames.put(Enchantment.DAMAGE_ARTHROPODS, "Bane of Arthropods");
        enchantmentNames.put(Enchantment.DAMAGE_UNDEAD, "Smite");
        enchantmentNames.put(Enchantment.DEPTH_STRIDER, "Depth Strider");
        enchantmentNames.put(Enchantment.DIG_SPEED, "Efficiency");
        enchantmentNames.put(Enchantment.DURABILITY, "Unbreaking");
        enchantmentNames.put(Enchantment.FIRE_ASPECT, "Fire Aspect");
        enchantmentNames.put(Enchantment.KNOCKBACK, "Knockback");
        enchantmentNames.put(Enchantment.LOOT_BONUS_BLOCKS, "Fortune");
        enchantmentNames.put(Enchantment.LOOT_BONUS_MOBS, "Looting");
        potionNames.put(PotionEffectType.ABSORPTION, "Absorption");
        potionNames.put(PotionEffectType.BLINDNESS, "Blindness");
        potionNames.put(PotionEffectType.CONFUSION, "Nausea");
        potionNames.put(PotionEffectType.DAMAGE_RESISTANCE, "Resistance");
        potionNames.put(PotionEffectType.FAST_DIGGING, "Haste");
        potionNames.put(PotionEffectType.FIRE_RESISTANCE, "Fire Resistance");
        potionNames.put(PotionEffectType.HARM, "Harming");
        potionNames.put(PotionEffectType.HEAL, "Healing");
        potionNames.put(PotionEffectType.REGENERATION, "Regeneration");
        potionNames.put(PotionEffectType.HEALTH_BOOST, "Health Boost");
        potionNames.put(PotionEffectType.HUNGER, "Hunger");
        potionNames.put(PotionEffectType.INCREASE_DAMAGE, "Strength");
        potionNames.put(PotionEffectType.INVISIBILITY, "Invisibility");
        potionNames.put(PotionEffectType.JUMP, "Jump Boost");
        potionNames.put(PotionEffectType.SLOW, "Slowness");
        potionNames.put(PotionEffectType.SLOW_DIGGING, "Mining Fatigue");
        potionNames.put(PotionEffectType.SPEED, "Speed");
        potionNames.put(PotionEffectType.NIGHT_VISION, "Night Vision");
        potionNames.put(PotionEffectType.POISON, "Poison");
        potionNames.put(PotionEffectType.WATER_BREATHING, "Water Breathing");
        potionNames.put(PotionEffectType.WEAKNESS, "Weakness");
        potionNames.put(PotionEffectType.WITHER, "Wither");
    }

    public String getByEnchantment(Enchantment enchantment) {
        String returned = enchantmentNames.get(enchantment);
        if (returned == null) returned = enchantment.toString();
        return returned;
    }

    public String getByPotion(PotionEffectType potionEffectType) {
        String returned = potionNames.get(potionEffectType);
        if (returned == null) returned = potionEffectType.getName();
        return returned;
    }
}
