package com.me.custompick;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryTracker {
    private final Map<UUID, ItemStack> pendingResults = new HashMap<>();

    public void setPendingResult(UUID playerId, ItemStack item) {
        pendingResults.put(playerId, item);
    }

    public ItemStack removePendingResult(UUID playerId) {
        return pendingResults.remove(playerId);
    }

    public ItemStack getPendingResult(UUID playerId) {
        return pendingResults.get(playerId);
    }
}
