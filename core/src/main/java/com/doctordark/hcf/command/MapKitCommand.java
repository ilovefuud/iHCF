package com.doctordark.hcf.command;

import com.doctordark.hcf.HCF;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.misc.InventoryUtils;

import java.util.*;

public class MapKitCommand extends PlayerCommand implements Listener {

    //private static final String SEPARATOR_LINE = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + ChatColor.BOLD.toString() + Strings.repeat('-', 16);
    private final ItemStack BLANK = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(DyeColor.GRAY.getData()).name("").build();
    private final ItemStack LIGHTER = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(DyeColor.WHITE.getData()).name("").build();

    private Inventory mapkitInventory;

    private final HCF plugin;

    public MapKitCommand(HCF plugin) {
        super("mapkit");
        setAliases("kitmap");
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        news();
    }

    private void news() {
        mapkitInventory = Bukkit.createInventory(null, InventoryUtils.getSafestInventorySize(64), !plugin.getConfiguration().isKitmap() ? "Map " + plugin.getConfiguration().getMapNumber() + " Kit" : "KitMap Limits");
        ItemStack[] contents = new ItemStack[mapkitInventory.getSize()];
        Arrays.fill(contents, BLANK);
        Arrays.fill(contents, (9 * 1) + 2, (9 * 1) + 7, LIGHTER);
        Arrays.fill(contents, (9 * 2) + 2, (9 * 2) + 7, LIGHTER);
        Arrays.fill(contents, (9 * 3) + 2, (9 * 3) + 7, LIGHTER);
        Arrays.fill(contents, (9 * 4) + 2, (9 * 4) + 7, LIGHTER);
        int protection = plugin.getConfiguration().getEnchantmentLimit(Enchantment.PROTECTION_ENVIRONMENTAL);
        int unbreaking = plugin.getConfiguration().getEnchantmentLimit(Enchantment.DURABILITY);
        int sharpness = plugin.getConfiguration().getEnchantmentLimit(Enchantment.DAMAGE_ALL);
        int looting = plugin.getConfiguration().getEnchantmentLimit(Enchantment.LOOT_BONUS_MOBS);
        int power = plugin.getConfiguration().getEnchantmentLimit(Enchantment.ARROW_DAMAGE);
        int flame = plugin.getConfiguration().getEnchantmentLimit(Enchantment.ARROW_FIRE);
        ItemStack HELMET = new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protection).enchant(Enchantment.DURABILITY, unbreaking).build();ItemStack CHESTPLATE = new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protection).enchant(Enchantment.DURABILITY, unbreaking).build();
        ItemStack LEGGINGS = new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protection).enchant(Enchantment.DURABILITY, unbreaking).build();
        ItemStack BOOTS = new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protection).enchant(Enchantment.DURABILITY, unbreaking).build();
        ItemStack SWORD = new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, sharpness).enchant(Enchantment.LOOT_BONUS_MOBS, looting).enchant(Enchantment.DURABILITY, unbreaking).build();
        ItemStack BOW = new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, power).enchant(Enchantment.ARROW_FIRE, flame).enchant(Enchantment.DURABILITY, unbreaking).build();
        int posion = plugin.getConfiguration().getPotionLimit(PotionType.POISON);
        ItemStack POSION = ItemBuilder.from(new Potion(PotionType.POISON, posion, true, false).toItemStack(1)).lore(CC.YELLOW + "Amplifier: " + posion, CC.YELLOW + "Unextended ").build();
        int invisibility = plugin.getConfiguration().getPotionLimit(PotionType.INVISIBILITY);
        ItemStack INVIS = ItemBuilder.from(new Potion(PotionType.INVISIBILITY, Math.max(invisibility, 1), false, true).toItemStack(1)).lore(CC.YELLOW + "Enabled").build();
        contents[(9 * 1) + 4] = HELMET.clone();
        contents[(9 * 2) + 3] = POSION.clone();
        contents[(9 * 2) + 4] = CHESTPLATE.clone();
        contents[(9 * 2) + 5] = SWORD.clone();
        contents[(9 * 3) + 3] = INVIS.clone();
        contents[(9 * 3) + 4] = LEGGINGS.clone();
        contents[(9 * 3) + 5] = BOW.clone();
        contents[(9 * 4) + 4] = BOOTS.clone();
        mapkitInventory.setContents(contents);
    }

    /*private void reloadMapKitInventory() {
        List<ItemStack> items = new ArrayList<>();

        for (Enchantment enchantment : Enchantment.values()) {
            int maxLevel = plugin.getConfiguration().getEnchantmentLimit(enchantment);
            ItemBuilder builder = new ItemBuilder(Material.ENCHANTED_BOOK);
            builder.name(ChatColor.YELLOW + enchantment.getName() + ": " + ChatColor.GREEN + (maxLevel == 0 ? "Disabled" : maxLevel));
            //builder.lore(SEPARATOR_LINE, ChatColor.WHITE + "  No Extra Data", SEPARATOR_LINE);
            items.add(builder.build());
        }

        for (PotionType potionType : PotionType.values()) {
            int maxLevel = plugin.getConfiguration().getPotionLimit(potionType);
            ItemBuilder builder = ItemBuilder.from(new Potion(potionType).toItemStack(1));
            builder.name(ChatColor.YELLOW + WordUtils.capitalizeFully(potionType.name().replace('_', ' ')) + ": " + ChatColor.GREEN + (maxLevel == 0 ? "Disabled" : maxLevel));
            //builder.lore(SEPARATOR_LINE, ChatColor.WHITE + "  No Extra Data", SEPARATOR_LINE);
            items.add(builder.build());
        }

        mapkitInventory = Bukkit.createInventory(null, InventoryUtils.getSafestInventorySize(items.size()), "Map " + plugin.getConfiguration().getMapNumber() + " Kit");
        for (ItemStack item : items) {
            mapkitInventory.addItem(item);
        }
    }*/

    @Override
    public void execute(Player player, String[] strings) {
        player.openInventory(mapkitInventory);
    }


    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (mapkitInventory.equals(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onPluginDisable(PluginDisableEvent event) {
        for (HumanEntity viewer : new HashSet<>(mapkitInventory.getViewers())) { // copy to prevent co-modification
            viewer.closeInventory();
        }
    }
}
