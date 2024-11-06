package com.me.custompick;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private List<String> allowedPickaxes;
    private Map<String, Map<String, Integer>> durabilityLossConfig;
    private Map<String, Map<Integer, Integer>> unbreakingConfig;
    private final CustomPick plugin;

    public ConfigManager(CustomPick plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();
        allowedPickaxes = config.getStringList("allowed");
        durabilityLossConfig = new HashMap<>();
        unbreakingConfig = new HashMap<>();

        for (String pickaxe : allowedPickaxes) {
            if (config.contains("durability_loss." + pickaxe)) {
                Map<String, Integer> lossConfig = new HashMap<>();
                lossConfig.put("supereff", config.getInt("durability_loss." + pickaxe + ".supereff"));
                lossConfig.put("supereff2", config.getInt("durability_loss." + pickaxe + ".supereff2"));
                durabilityLossConfig.put(pickaxe, lossConfig);
            }
            if (config.contains("unbreaking." + pickaxe)) {
                Map<Integer, Integer> unbreakingLevels = new HashMap<>();
                for (String level : config.getConfigurationSection("unbreaking." + pickaxe).getKeys(false)) {
                    unbreakingLevels.put(Integer.parseInt(level), config.getInt("unbreaking." + pickaxe + "." + level));
                }
                unbreakingConfig.put(pickaxe, unbreakingLevels);
            }
        }
    }

    public void reload() {
        plugin.reloadConfig();
        loadConfig();
    }

    public List<String> getAllowedPickaxes() {
        return allowedPickaxes;
    }

    public boolean isAllowedPickaxe(Material material) {
        return allowedPickaxes.contains(material.name());
    }

    public int getBaseDurabilityLoss(String pickaxe, String enchantTag) {
        if (durabilityLossConfig.containsKey(pickaxe)) {
            return durabilityLossConfig.get(pickaxe).getOrDefault(enchantTag, 0);
        }
        return 0;
    }

    public int getUnbreakingReduction(String pickaxe, int level) {
        if (unbreakingConfig.containsKey(pickaxe)) {
            return unbreakingConfig.get(pickaxe).getOrDefault(level, 0);
        }
        return 0;
    }
}
