package me.cahrypt.kitpvpcore.gui.listeners;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.gui.GUI;
import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.listener.AListener;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Optional;

public class GUIFlipListener extends AListener {
    private final KitPlayerManager kitPlayerManager;
    private final ItemStack fwdArrow;
    private final ItemStack bkArrow;

    public GUIFlipListener() {
        super();
        this.kitPlayerManager = JavaPlugin.getPlugin(KitPvPCore.class).getKitPlayerManager();
        this.fwdArrow = KitCoreItems.GUIStandardItems.FORWARD_ARROW.getItem();
        this.bkArrow = KitCoreItems.GUIStandardItems.BACKWARD_ARROW.getItem();
    }

    private boolean openNextGUIIfPresent(List<GUI> guiSystem, KitPlayer kitPlayer, InventoryView inventoryView, int incrementFromCurrentInv) {
        String invName = inventoryView.getTitle();

        Optional<GUI> optionalGUI = guiSystem
                .stream()
                .filter(gui -> gui.getUiName().equals(invName))
                .findFirst();

        if (optionalGUI.isEmpty()) {
            return false;
        }

        int index = guiSystem.indexOf(optionalGUI.get()) + incrementFromCurrentInv;

        if (index > guiSystem.size()) {
            return false;
        }

        guiSystem.get(index).open(kitPlayer);
        return true;
    }

    @EventHandler
    public void onGUIFlip(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) {
            return;
        }

        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer((Player) event.getWhoClicked());
        List<GUI> guiSystem = kitPlayer.getActiveGUISystem();

        if (guiSystem == null) {
            return;
        }

        event.setCancelled(true);
        InventoryView inventoryView = event.getView();

        if (clickedItem.isSimilar(fwdArrow)) {
            if (openNextGUIIfPresent(guiSystem, kitPlayer, inventoryView, 1)) {
                return;
            }
        }

        if (clickedItem.isSimilar(bkArrow)) {
            openNextGUIIfPresent(guiSystem, kitPlayer, event.getView(), -1);
        }
    }
}
