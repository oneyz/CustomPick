package com.me.custompick;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PickReloadCommand implements CommandExecutor {
    private final CustomPick plugin;
    private final ConfigManager configManager;

    public PickReloadCommand(CustomPick plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player) || sender.hasPermission("custompick.reload")) {
            configManager.reload();
            sender.sendMessage("§aKonfiguracja przeładowana pomyślnie");
            return true;
        } else {
            sender.sendMessage("§cNie masz dostępu aby użyć tej komendy.");
            return true;
        }
    }
}
