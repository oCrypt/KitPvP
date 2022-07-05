package me.cahrypt.kitpvpcore.kit.listeners;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.kit.Kit;
import me.cahrypt.kitpvpcore.kit.KitManager;
import me.cahrypt.kitpvpcore.listener.AListener;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class KitSelectListener extends AListener {
    private final KitManager kitManager;
    private final KitPlayerManager kitPlayerManager;

    public KitSelectListener() {
        super();
        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitManager = main.getKitManager();
        this.kitPlayerManager = main.getKitPlayerManager();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            return;
        }

        Kit kit = kitManager.getSelectedKit(clickedItem);

        if (kit == null) {
            return;
        }

        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer((Player) event.getWhoClicked());
        kit.equipKit(kitPlayer);
        event.getView().close();
    }
}
