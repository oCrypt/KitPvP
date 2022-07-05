package me.cahrypt.kitpvpcore.item;

import me.cahrypt.kitpvpcore.item.events.KitSelector;
import me.cahrypt.kitpvpcore.item.events.Soup;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ItemManager {
    private final Set<ItemEvent> itemEventSet;

    public ItemManager() {
        this.itemEventSet = new HashSet<>();
    }

    public void initializeItemEvents() {
        new Soup();
        new KitSelector();
        Bukkit.getLogger().info("Items Initialized");
    }

    protected ItemEvent getItemEvent(ItemStack item) {
        return itemEventSet
                .stream()
                .filter(itemEvent -> itemEvent.isItem(item))
                .findFirst()
                .orElse(null);
    }

    protected void addItemEvent(ItemEvent itemEvent) {
        itemEventSet.add(itemEvent);
    }
}
