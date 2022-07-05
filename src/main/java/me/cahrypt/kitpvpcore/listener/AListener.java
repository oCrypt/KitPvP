package me.cahrypt.kitpvpcore.listener;

import me.cahrypt.kitpvpcore.KitPvPCore;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class AListener implements Listener {
    public AListener() {
        Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getPlugin(KitPvPCore.class));
    }
}
