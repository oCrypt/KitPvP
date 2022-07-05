package me.cahrypt.kitpvpcore.item;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.listener.AListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemEventHandler extends AListener {
    private final ItemManager itemManager;

    public ItemEventHandler() {
        super();
        this.itemManager = JavaPlugin.getPlugin(KitPvPCore.class).getItemManager();
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        ItemEvent usageEvent = itemManager.getItemEvent(item);

        if (usageEvent == null) {
            return;
        }

        usageEvent.onPlayerUseItem(event);
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        if (item.getType() != Material.BOWL) {
            event.setCancelled(true);
        }
    }
}
