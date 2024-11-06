package com.me.custompick;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class PickaxeDescriptionUpdater implements Listener {
    private final ConfigManager configManager;

    public PickaxeDescriptionUpdater(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerHoldItem(PlayerItemHeldEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItem(event.getNewSlot());

        if (item != null && item.getType() != Material.AIR && configManager.isAllowedPickaxe(item.getType())) {
            NBTItem nbtItem = new NBTItem(item);
            String customEnchant = nbtItem.getString("custom_enchant");

            if ("supereff".equals(customEnchant) || "supereff2".equals(customEnchant)) {
                ItemMeta itemMeta = item.getItemMeta();
                if (itemMeta != null) {
                    if (!hasCorrectLore(itemMeta, customEnchant)) {
                        String customLore = getLoreForEnchant(customEnchant);
                        itemMeta.setLore(Arrays.asList(customLore.split("\n")));
                        item.setItemMeta(itemMeta);
                    }
                }
            }
        }
    }

    private boolean hasCorrectLore(ItemMeta meta, String customEnchant) {
        if (meta.hasLore()) {
            String correctLore = getLoreForEnchant(customEnchant);
            return meta.getLore().containsAll(Arrays.asList(correctLore.split("\n")));
        }
        return false;
    }

    private String getLoreForEnchant(String customEnchant) {
        if ("supereff".equals(customEnchant)) {
            return ChatColor.translateAlternateColorCodes('&', "\n&7Dodatki:\n&c| &7Kopanie 3X3");
        } else if ("supereff2".equals(customEnchant)) {
            return ChatColor.translateAlternateColorCodes('&', "\n&7Dodatki:\n&c| &7Kopanie 5X5");
        }
        return "";
    }
}
