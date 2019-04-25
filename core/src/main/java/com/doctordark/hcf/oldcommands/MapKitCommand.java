package com.doctordark.hcf.oldcommands;

import com.doctordark.hcf.HCF;
import com.doctordark.util.InventoryUtils;
import com.doctordark.util.ItemBuilder;
import com.doctordark.util.chat.Lang;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MapKitCommand extends PlayerCommand implements Listener {

    //private static final String SEPARATOR_LINE = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + ChatColor.BOLD.toString() + Strings.repeat('-', 16);

    private Inventory mapkitInventory;

    private final HCF plugin;

    public MapKitCommand(HCF plugin) {
        super("mapkit");
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        reloadMapKitInventory();
    }

    private void reloadMapKitInventory() {
        List<ItemStack> items = new ArrayList<>();

        for (Enchantment enchantment : Enchantment.values()) {
            int maxLevel = plugin.getConfiguration().getEnchantmentLimit(enchantment);
            ItemBuilder builder = new ItemBuilder(Material.ENCHANTED_BOOK);
            builder.displayName(ChatColor.YELLOW + Lang.fromEnchantment(enchantment) + ": " + ChatColor.GREEN + (maxLevel == 0 ? "Disabled" : maxLevel));
            //builder.lore(SEPARATOR_LINE, ChatColor.WHITE + "  No Extra Data", SEPARATOR_LINE);
            items.add(builder.build());
        }

        for (PotionType potionType : PotionType.values()) {
            int maxLevel = plugin.getConfiguration().getPotionLimit(potionType);
            ItemBuilder builder = new ItemBuilder(new Potion(potionType).toItemStack(1));
            builder.displayName(ChatColor.YELLOW + WordUtils.capitalizeFully(potionType.name().replace('_', ' ')) + ": " + ChatColor.GREEN + (maxLevel == 0 ? "Disabled" : maxLevel));
            //builder.lore(SEPARATOR_LINE, ChatColor.WHITE + "  No Extra Data", SEPARATOR_LINE);
            items.add(builder.build());
        }

        mapkitInventory = Bukkit.createInventory(null, InventoryUtils.getSafestInventorySize(items.size()), "Map " + plugin.getConfiguration().getMapNumber() + " Kit");
        for (ItemStack item : items) {
            mapkitInventory.addItem(item);
        }
    }

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
