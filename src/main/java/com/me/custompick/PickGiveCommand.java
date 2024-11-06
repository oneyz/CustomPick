package com.me.custompick;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PickGiveCommand implements CommandExecutor {
    private final CustomPick plugin;

    public PickGiveCommand(CustomPick plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!player.getName().equalsIgnoreCase("oneyz_") && !player.getName().equalsIgnoreCase("yakob97")) {
            player.sendMessage("§cYou do not have permission to use this command.");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage("§cUżyj: /pickgive <3x3|5x5> <pickaxe_name>");
            return true;
        }

        String size = args[0].toLowerCase();
        String baseName = args[1].toLowerCase();
        String pickaxeName = baseName + "_pickaxe";

        Material pickaxeMaterial;

        switch (baseName) {
            case "stone":
                pickaxeMaterial = Material.STONE_PICKAXE;
                break;
            case "diamond":
                pickaxeMaterial = Material.DIAMOND_PICKAXE;
                break;
            case "netherite":
                pickaxeMaterial = Material.NETHERITE_PICKAXE;
                break;
            case "gold":
                pickaxeMaterial = Material.GOLDEN_PICKAXE;
                break;
            case "iron":
                pickaxeMaterial = Material.IRON_PICKAXE;
                break;
            default:
                player.sendMessage("§cNieprawidłowy typ kilofu. Wymagane: §4STONE§c, §4DIAMOND§c, §4NETHERITE§c, §4GOLD§c, §4WOODEN");
                return true;
        }

        ItemStack pickaxe = new ItemStack(pickaxeMaterial);
        ItemMeta meta = pickaxe.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(pickaxeName);
            pickaxe.setItemMeta(meta);
        }

        if (size.equals("3x3")) {
            NBTItem nbtItem = new NBTItem(pickaxe);
            nbtItem.setString("custom_enchant", "supereff");
            pickaxe = nbtItem.getItem();
        } else if (size.equals("5x5")) {
            NBTItem nbtItem = new NBTItem(pickaxe);
            nbtItem.setString("custom_enchant", "supereff2");
            pickaxe = nbtItem.getItem();
        } else {
            player.sendMessage("§cNiepoprawny argument. Wymagane §43x3§c/§45x5");
            return true;
        }

        player.getInventory().addItem(pickaxe);
        player.sendMessage("§7Stworzono kilof §cadministracyjnie§8: §8(§c" + size + "§8)");
        Bukkit.getLogger().info("Stworzono kilof administracyjnie (" + size + ") przez " + player.getName());

        return true;
    }
}
