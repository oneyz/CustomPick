package com.me.custompick;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class EfficiencyCombiner implements Listener {

    @EventHandler
    public void onPrepareEfficiency6(PrepareAnvilEvent event) {
        ItemStack slot1 = event.getInventory().getItem(0);
        ItemStack slot2 = event.getInventory().getItem(1);

        if (slot1 != null && slot2 != null
                && slot1.getType() == Material.ENCHANTED_BOOK
                && slot2.getType() == Material.ENCHANTED_BOOK) {

            EnchantmentStorageMeta meta1 = (EnchantmentStorageMeta) slot1.getItemMeta();
            EnchantmentStorageMeta meta2 = (EnchantmentStorageMeta) slot2.getItemMeta();

            if (meta1 != null && meta2 != null
                    && meta1.hasStoredEnchant(Enchantment.EFFICIENCY) && meta2.hasStoredEnchant(Enchantment.EFFICIENCY)
                    && meta1.getStoredEnchantLevel(Enchantment.EFFICIENCY) == 5
                    && meta2.getStoredEnchantLevel(Enchantment.EFFICIENCY) == 5) {


                ItemStack resultBook = new ItemStack(Material.ENCHANTED_BOOK);
                EnchantmentStorageMeta resultMeta = (EnchantmentStorageMeta) resultBook.getItemMeta();

                assert resultMeta != null;
                resultMeta.addStoredEnchant(Enchantment.EFFICIENCY, 6, true);
                resultBook.setItemMeta(resultMeta);

                event.setResult(resultBook);

            }
        }
    }
}
