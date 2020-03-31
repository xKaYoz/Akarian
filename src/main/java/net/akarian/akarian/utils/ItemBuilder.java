package net.akarian.akarian.utils;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by KaYoz on 8/7/2017.
 * Subscribe to me on Youtube:
 * http://www.youtube.com/c/KaYozMC/
 */

public class ItemBuilder {

    ItemStack item;
    @Getter
    ItemMeta meta;

    public ItemBuilder(Material mat, int amount) {
        item = new ItemStack(mat, amount);
        meta = item.getItemMeta();
    }

    public ItemBuilder(Material mat, int id, int amount) {
        item = new ItemStack(mat, amount, (byte) id);
        meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(Chat.format(name));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(Chat.formatList(lore));
        return this;
    }

    public ItemBuilder setEnchant(Enchantment enchant, int level) {
        item.addUnsafeEnchantment(enchant, level);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
