package me.cahrypt.kitpvpcore.player.listeners;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.listener.AListener;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class LeaveListener extends AListener {
    private final KitPlayerManager kitPlayerManager;

    public LeaveListener() {
        super();
        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        kitPlayerManager.quitPlayer(event.getPlayer());
    }
}