package com.me.custompick;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class AnvilClickListener implements Listener {
    private final CustomPick plugin;

    public AnvilClickListener(CustomPick plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.ANVIL) {
            Player player = (Player) event.getWhoClicked();
            ItemStack resultItem = plugin.getInventoryTracker().getPendingResult(player.getUniqueId());

            if (event.getSlot() == 2 && resultItem != null) {
                NBTItem nbtItem = new NBTItem(resultItem);
                String customEnchant = nbtItem.getString("custom_enchant");

                plugin.getInventoryTracker().removePendingResult(player.getUniqueId());
                plugin.getLogManager().logPickaxeCreation(player, customEnchant, resultItem);
            }
        }
    }
}
