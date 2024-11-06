package com.me.custompick;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PrepareAnvilListener implements Listener {
    private final ConfigManager configManager;
    private final CustomPick plugin;

    public PrepareAnvilListener(ConfigManager configManager, CustomPick plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        ItemStack slot1 = event.getInventory().getItem(0);
        ItemStack slot2 = event.getInventory().getItem(1);

        if (slot1 != null && configManager.isAllowedPickaxe(slot1.getType())) {
            if (slot2 != null && slot2.getType() == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) slot2.getItemMeta();

                if (meta != null && meta.hasStoredEnchant(Enchantment.EFFICIENCY)) {
                    int enchantLevel = meta.getStoredEnchantLevel(Enchantment.EFFICIENCY);
                    int maxLevel = Enchantment.EFFICIENCY.getMaxLevel();

                    if (enchantLevel == 6) {
                        NBTItem nbtItem = new NBTItem(slot1);
                        String currentEnchantValue = nbtItem.getString("custom_enchant");

                        ItemStack resultItem = nbtItem.getItem();
                        Player player = (Player) event.getView().getPlayer();

                        String customLore;
                        if ("supereff".equals(currentEnchantValue)) {
                            nbtItem.setString("custom_enchant", "supereff2");
                            customLore = ChatColor.translateAlternateColorCodes('&', "\n&7Dodatki:\n&c| &7Kopanie 5x5");
                        } else {
                            nbtItem.setString("custom_enchant", "supereff");
                            customLore = ChatColor.translateAlternateColorCodes('&', "\n&7Dodatki:\n&c| &7Kopanie 3x3");
                        }

                        if (!resultItem.containsEnchantment(Enchantment.EFFICIENCY)) {
                            resultItem.addEnchantment(Enchantment.EFFICIENCY, Math.min(enchantLevel, maxLevel));
                        }

                        ItemMeta resultMeta = resultItem.getItemMeta();
                        if (resultMeta != null) {
                            resultMeta.setLore(Arrays.asList(customLore.split("\n")));
                            resultItem.setItemMeta(resultMeta);
                        }

                        event.setResult(resultItem);
                        plugin.getInventoryTracker().setPendingResult(player.getUniqueId(), resultItem);
                    }
                }
            }
        }
    }
}
