package com.me.custompick;

import de.tr7zw.nbtapi.NBT;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.iface.ReadWriteNBT;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PickDebugCommand implements CommandExecutor {

    private final Set<String> allowedPlayers = new HashSet<>(Arrays.asList("oneyz_", "yakob97"));

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        if (!allowedPlayers.contains(player.getName())) {
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir()) {
            player.sendMessage(ChatColor.RED + "Nie trzymasz itemu");
            return true;
        }

        ReadWriteNBT nbt = NBT.itemStackToNBT(item);
        String json = nbt.toString();
        player.sendMessage("§7Dane NBT:");
        player.sendMessage(json);

        return true;
    }
}