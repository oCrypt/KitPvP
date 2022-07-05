package me.cahrypt.kitpvpcore.item;

import me.cahrypt.kitpvpcore.KitPvPCore;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ItemEvent {
    private final ItemStack listenItem;

    public ItemEvent(ItemStack listenItem) {
        this.listenItem = listenItem;
        JavaPlugin.getPlugin(KitPvPCore.class).getItemManager().addItemEvent(this);
    }

    public boolean isItem(ItemStack item) {
        return listenItem.isSimilar(item);
    }

    public abstract void onPlayerUseItem(PlayerInteractEvent event);
}
