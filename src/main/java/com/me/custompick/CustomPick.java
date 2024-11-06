package com.me.custompick;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomPick extends JavaPlugin {
    private ConfigManager configManager;
    private LogManager logManager;
    private InventoryTracker inventoryTracker;

    private boolean isCoreProtectInstalled;
    private boolean isGriefPreventionInstalled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        configManager = new ConfigManager(this);
        logManager = new LogManager(this);
        inventoryTracker = new InventoryTracker();

        checkDependencies();

        getServer().getPluginManager().registerEvents(new PrepareAnvilListener(configManager, this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(configManager, this), this);
        getServer().getPluginManager().registerEvents(new PickaxeDescriptionUpdater(configManager), this);
        getServer().getPluginManager().registerEvents(new EfficiencyCombiner(), this);
        getServer().getPluginManager().registerEvents(new AnvilClickListener(this), this);

        getCommand("pickreload").setExecutor(new PickReloadCommand(this, configManager));
        getCommand("pickdebug").setExecutor(new PickDebugCommand());
        getCommand("pickgive").setExecutor(new PickGiveCommand(this));
    }


    private void checkDependencies() {
        Plugin coreProtect = Bukkit.getPluginManager().getPlugin("CoreProtect");
        Plugin griefPrevention = Bukkit.getPluginManager().getPlugin("GriefPrevention");

        if (coreProtect != null) {
            isCoreProtectInstalled = true;
            getLogger().info("CoreProtect is installed and enabled.");
        } else {
            isCoreProtectInstalled = false;
            getLogger().warning("CoreProtect is not installed or not enabled.");
        }

        if (griefPrevention != null) {
            isGriefPreventionInstalled = true;
            getLogger().info("GriefPrevention is installed and enabled.");
        } else {
            isGriefPreventionInstalled = false;
            getLogger().warning("GriefPrevention is not installed or not enabled.");
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LogManager getLogManager() {
        return logManager;
    }

    public InventoryTracker getInventoryTracker() {
        return inventoryTracker;
    }

    public boolean isCoreProtectInstalled() {
        return isCoreProtectInstalled;
    }

    public boolean isGriefPreventionInstalled() {
        return isGriefPreventionInstalled;
    }
}