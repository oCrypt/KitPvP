package me.cahrypt.kitpvpcore.scoreboard;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class KitScoreboardHandler {
    private final KitPvPCore main;
    private final KitPlayerManager kitPlayerManager;
    private BukkitTask scoreboardTask;

    public KitScoreboardHandler() {
        this.main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
        scoreboardTask = null;
    }

    public void startScoreboard() {
        scoreboardTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> kitPlayerManager.getKitPlayer(player).getKitScoreboard().updateStats());
            }
        }.runTaskTimer(main, 0, 100);
    }

    public void stopScoreboard() {
        scoreboardTask.cancel();
    }
}
