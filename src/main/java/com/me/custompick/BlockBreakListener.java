package com.me.custompick;

import de.tr7zw.nbtapi.NBTItem;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public class BlockBreakListener implements Listener {
    private final ConfigManager configManager;
    private final CoreProtectAPI coreProtectAPI;
    private final CustomPick plugin;

    private static final Set<Material> UNBREAKABLE_BLOCKS = EnumSet.of(
            Material.BEDROCK, Material.BARRIER, Material.END_PORTAL_FRAME,
            Material.END_PORTAL, Material.NETHER_PORTAL, Material.COMMAND_BLOCK,
            Material.STRUCTURE_BLOCK, Material.JIGSAW, Material.REINFORCED_DEEPSLATE,
            Material.MOVING_PISTON, Material.DRAGON_EGG
    );

    public BlockBreakListener(ConfigManager configManager, CustomPick plugin) {
        this.configManager = configManager;
        this.plugin = plugin;
        this.coreProtectAPI = plugin.isCoreProtectInstalled() ? ((CoreProtect) Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("CoreProtect"))).getAPI() : null;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (!configManager.isAllowedPickaxe(itemInHand.getType())) return;

        NBTItem nbtItem = new NBTItem(itemInHand);
        String customEnchantTag = nbtItem.getString("custom_enchant");

        if (!("supereff".equals(customEnchantTag) || "supereff2".equals(customEnchantTag))) return;

        if (plugin.isGriefPreventionInstalled()) {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(event.getBlock().getLocation(), true, null);

            if (claim != null && !claim.hasExplicitPermission(player, ClaimPermission.Access)) {
                event.setCancelled(true);
                return;
            }
        }

        int radius = "supereff2".equals(customEnchantTag) ? 2 : 1;
        destroyArea(event, radius);
    }

    private void destroyArea(BlockBreakEvent event, int radius) {
        Block centerBlock = event.getBlock();
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        NBTItem nbtItem = new NBTItem(tool);
        String customEnchantTag = nbtItem.getString("custom_enchant");

        int baseDurabilityLoss = configManager.getBaseDurabilityLoss(tool.getType().name(), customEnchantTag);
        int durabilityLoss = calculateDurabilityLoss(tool, baseDurabilityLoss);

        int startX = centerBlock.getX() - radius;
        int startY = centerBlock.getY() - radius;
        int startZ = centerBlock.getZ() - radius;
        int endX = centerBlock.getX() + radius;
        int endY = centerBlock.getY() + radius;
        int endZ = centerBlock.getZ() + radius;

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                for (int z = startZ; z <= endZ; z++) {
                    Block block = centerBlock.getWorld().getBlockAt(x, y, z);

                    if (!isBreakable(block, player)) continue;

                    if (plugin.isCoreProtectInstalled() && coreProtectAPI != null) {
                        BlockData blockData = block.getBlockData();
                        coreProtectAPI.logRemoval(player.getName(), block.getLocation(), block.getType(), blockData);
                    }

                    block.breakNaturally(tool);
                }
            }
        }

        applyDurabilityLoss(tool, durabilityLoss, player);
    }

    private boolean isBreakable(Block block, Player player) {
        return block.getType() != Material.AIR
                && block.getType().isBlock()
                && !UNBREAKABLE_BLOCKS.contains(block.getType())
                && isPlayerAuthorizedToBreak(block.getLocation(), player);
    }

    private boolean isPlayerAuthorizedToBreak(Location location, Player player) {
        if (!plugin.isGriefPreventionInstalled()) return true;
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
        return claim == null || claim.hasExplicitPermission(player, ClaimPermission.Access);
    }

    private int calculateDurabilityLoss(ItemStack tool, int baseLoss) {
        int unbreakingLevel = getUnbreakingLevel(tool);
        int unbreakingReduction = configManager.getUnbreakingReduction(tool.getType().name(), unbreakingLevel);

        return Math.max(baseLoss - unbreakingReduction, 0);
    }

    private int getUnbreakingLevel(ItemStack tool) {
        ItemMeta meta = tool.getItemMeta();
        int unbreakingLevel = 0;
        if (meta != null && meta.hasEnchant(Enchantment.UNBREAKING)) {
            unbreakingLevel = meta.getEnchantLevel(Enchantment.UNBREAKING);
        }

        return unbreakingLevel;
    }

    private void applyDurabilityLoss(ItemStack tool, int loss, Player player) {
        ItemMeta meta = tool.getItemMeta();
        if (meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            int currentDamage = damageable.getDamage();
            int newDurability = currentDamage + loss;

            if (newDurability >= tool.getType().getMaxDurability()) {
                player.getInventory().removeItem(tool);
                return;
            }

            damageable.setDamage(newDurability);
            tool.setItemMeta(meta);
        }
    }
}
