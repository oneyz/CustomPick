package com.me.custompick;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class LogManager {
    private final CustomPick plugin;

    public LogManager(CustomPick plugin) {
        this.plugin = plugin;
        File logsFolder = new File(plugin.getDataFolder(), "logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }
    }

    public void logPickaxeCreation(Player player, String customEnchant, ItemStack item) {
        File logsFolder = new File(plugin.getDataFolder(), "logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = now.format(formatter);

        Map<String, Object> logEntry = new HashMap<>();
        logEntry.put("player", player.getName());
        logEntry.put("uuid", player.getUniqueId().toString());
        logEntry.put("custom_enchant", customEnchant);
        logEntry.put("timestamp", formattedDate);

        ReadWriteNBT nbt = NBT.itemStackToNBT(item);
        String json = nbt.toString();
        logEntry.put("nbt_data", json);

        File logFile = new File(logsFolder, player.getUniqueId() + "_log.yml");

        try (FileWriter writer = new FileWriter(logFile, true)) {
            Yaml yaml = new Yaml(new DumperOptions());
            yaml.dump(logEntry, writer);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Failed to write to log file: " + e.getMessage());
        }
    }
}