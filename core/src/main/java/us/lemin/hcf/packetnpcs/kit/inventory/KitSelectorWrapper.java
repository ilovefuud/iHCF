package us.lemin.hcf.packetnpcs.kit.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.api.inventoryapi.InventoryWrapper;
import us.lemin.core.api.inventoryapi.PlayerAction;
import us.lemin.core.api.inventoryapi.PlayerInventoryWrapper;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.player.PlayerUtil;
import us.lemin.hcf.HCF;

public class KitSelectorWrapper extends PlayerInventoryWrapper {

    private final int sharpLevel;
    private final int protLevel;
    private final int powerLevel;
    private final ItemStack swiftnessPotion;
    private final ItemStack food;


    public KitSelectorWrapper(HCF plugin) {
        super("Kit Selector", 3);
        this.sharpLevel = plugin.getConfiguration().getEnchantmentLimit(Enchantment.DAMAGE_ALL);
        this.protLevel = plugin.getConfiguration().getEnchantmentLimit(Enchantment.PROTECTION_ENVIRONMENTAL);
        this.powerLevel = plugin.getConfiguration().getEnchantmentLimit(Enchantment.ARROW_DAMAGE);
        this.swiftnessPotion = new ItemBuilder(Material.POTION).durability(8226).build();
        this.food = new ItemStack(Material.COOKED_BEEF, 64);
    }

    @Override
    public void init(Player player, InventoryWrapper inventoryWrapper) {
        format(player, inventoryWrapper);
    }

    @Override
    public void update(Player player, InventoryWrapper inventoryWrapper) {
        format(player, inventoryWrapper);
    }

    private void format(Player player, InventoryWrapper inventoryWrapper) {
        inventoryWrapper.fillBorder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1));

        inventoryWrapper.setItem(2, 2, new ItemBuilder(Material.DIAMOND_SWORD).name(CC.GOLD + "PvP").loreLine(CC.YELLOW + "Left click to reset your kit").loreLine(CC.YELLOW + "Right click to keep your sword and receive a new kit.").build(), new PlayerAction((actionPlayer, clickType) -> {
            ItemStack savedItem;
            if (clickType == ClickType.RIGHT) {
                savedItem = actionPlayer.getInventory().getItem(0);
                if (savedItem == null || savedItem.getType() != Material.DIAMOND_SWORD) {
                    savedItem = new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, sharpLevel).build();
                }
            } else {
                savedItem = new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, sharpLevel).build();
            }
            clear(actionPlayer);
            actionPlayer.getInventory().setItem(0, savedItem);
            applyDiamond(actionPlayer);
        }));
        inventoryWrapper.setItem(2, 4, new ItemBuilder(Material.BOW).name(CC.GOLD + "Archer").loreLine(CC.YELLOW + "Left click to reset your kit").loreLine(CC.YELLOW + "Right click to keep your sword and receive a new kit.").build(), new PlayerAction((actionPlayer, clickType) -> {
            ItemStack savedItem;
            if (clickType == ClickType.RIGHT) {
                savedItem = actionPlayer.getInventory().getItem(0);
                if (savedItem == null || savedItem.getType() != Material.DIAMOND_SWORD) {
                    savedItem = new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, sharpLevel).build();
                }
            } else {
                savedItem = new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, sharpLevel).build();
            }
            clear(actionPlayer);
            actionPlayer.getInventory().setItem(0, savedItem);
            applyArcher(actionPlayer);
        }));
        inventoryWrapper.setItem(2, 6, new ItemBuilder(Material.GHAST_TEAR).name(CC.GOLD + "Bard").loreLine(CC.YELLOW + "Left click to reset your kit").loreLine(CC.YELLOW + "Right click to keep your sword and receive a new kit.").build(), new PlayerAction((actionPlayer, clickType) -> {
            ItemStack savedItem;
            if (clickType == ClickType.RIGHT) {
                savedItem = actionPlayer.getInventory().getItem(0);
                if (savedItem == null || savedItem.getType() != Material.DIAMOND_SWORD) {
                    savedItem = new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, sharpLevel).build();
                }
            } else {
                savedItem = new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, sharpLevel).build();
            }
            clear(actionPlayer);
            actionPlayer.getInventory().setItem(0, savedItem);
            applyBard(actionPlayer);
        }));
    }

    private void clear(Player player) {
        player.closeInventory();
        PlayerUtil.clearPlayer(player);
    }

    private void fill(Player player) {
        ItemStack healthPotion = new ItemBuilder(Material.POTION).durability(16421).build();
        for (int i=0; i < 36; i++){
            player.getInventory().addItem(healthPotion);
        }
    }

    private void applyDiamond(Player player) {
        ItemStack helmet = new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().setItem(1, new ItemBuilder(Material.ENDER_PEARL).amount(16).build());
        player.getInventory().setItem(2, swiftnessPotion);
        player.getInventory().setItem(8, food);
        player.getInventory().setItem(17, swiftnessPotion);
        player.getInventory().setItem(26, swiftnessPotion);
        player.getInventory().setItem(35, swiftnessPotion);
        fill(player);
    }

    private void applyArcher(Player player) {
        ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().setItem(1, new ItemBuilder(Material.ENDER_PEARL).amount(16).build());
        player.getInventory().setItem(2, new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, powerLevel).enchant(Enchantment.ARROW_FIRE, 1).enchant(Enchantment.ARROW_INFINITE, 1).enchant(Enchantment.DURABILITY, 3).build());
        player.getInventory().setItem(8, food);
        player.getInventory().setItem(17, new ItemStack(Material.SUGAR,64));
        player.getInventory().setItem(26, new ItemStack(Material.ARROW,64));
        player.getInventory().setItem(35, new ItemStack(Material.FEATHER,64));
        fill(player);
    }

    private void applyBard(Player player) {
        ItemStack helmet = new ItemBuilder(Material.GOLD_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack chestplate = new ItemBuilder(Material.GOLD_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack leggings = new ItemBuilder(Material.GOLD_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        ItemStack boots = new ItemBuilder(Material.GOLD_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, protLevel).enchant(Enchantment.DURABILITY, 3).build();
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.getInventory().setItem(1, new ItemBuilder(Material.ENDER_PEARL).amount(16).build());
        player.getInventory().setItem(2, new ItemStack(Material.SUGAR, 32));
        player.getInventory().setItem(3, new ItemStack(Material.IRON_INGOT, 32));
        player.getInventory().setItem(8, food);
        player.getInventory().setItem(16, new ItemStack(Material.SPIDER_EYE, 32));
        player.getInventory().setItem(17, new ItemStack(Material.BLAZE_POWDER, 32));
        player.getInventory().setItem(25, new ItemStack(Material.FEATHER, 32));
        player.getInventory().setItem(26, new ItemStack(Material.GHAST_TEAR, 10));
        player.getInventory().setItem(35, new ItemStack(Material.MAGMA_CREAM, 32));
        fill(player);
    }
}
