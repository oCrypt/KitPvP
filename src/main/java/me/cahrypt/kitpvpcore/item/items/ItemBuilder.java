package me.cahrypt.kitpvpcore.item.items;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

public class ItemBuilder<T extends ItemMeta> {
    private final ItemStack itemStack;
    private final T itemMeta;

    @SuppressWarnings("unchecked cast")
    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        this.itemMeta = (T) itemStack.getItemMeta();
    }

    public ItemBuilder<T> setDisplayName(String name) {
        itemMeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder<T> addEnchantment(Enchantment enchantment, int power) {
        itemMeta.addEnchant(enchantment, power, true);
        return this;
    }

    public ItemBuilder<T> setLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder<T> setUnbreakable() {
        itemMeta.setUnbreakable(true);
        return this;
    }

    public ItemBuilder<T> setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder<T> useMeta(Consumer<T> actionConsumer) {
        actionConsumer.accept(itemMeta);
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
