package me.cahrypt.kitpvpcore.gui.listeners;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.listener.AListener;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GUICloseListener extends AListener {
    private final KitPlayerManager kitPlayerManager;

    public GUICloseListener() {
        super();
        this.kitPlayerManager = JavaPlugin.getPlugin(KitPvPCore.class).getKitPlayerManager();
    }

    @EventHandler
    public void onGUIClose(InventoryCloseEvent event) {
        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer((Player) event.getPlayer());

        if (kitPlayer == null) {
            return;
        }

        if (kitPlayer.getActiveGUISystem() != null) {
            kitPlayer.clearGUISystem();
        }
    }
}
