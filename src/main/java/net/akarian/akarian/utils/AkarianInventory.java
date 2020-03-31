package net.akarian.akarian.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public interface AkarianInventory extends InventoryHolder {

    void onGUIClick(Player p, int slot, ItemStack item, Inventory inv);

}
