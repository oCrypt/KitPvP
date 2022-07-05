package me.cahrypt.kitpvpcore.timer;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Timer {
    private int time;
    private final BukkitTask timerTask;

    public Timer(KitPlayer kitPlayer, int timerSeconds, Consumer<KitPlayer> onFinishConsumer, BiConsumer<KitPlayer, Integer> actionPerSecondConsumer) {
        this.time = timerSeconds;
        this.timerTask = new BukkitRunnable() {
            @Override
            public void run() {
                actionPerSecondConsumer.accept(kitPlayer, time);

                if (time == 0) {
                    onFinishConsumer.accept(kitPlayer);
                    cancel();
                    return;
                }

                time -= 1;
            }
        }.runTaskTimer(JavaPlugin.getPlugin(KitPvPCore.class), 0, 20);
    }

    public boolean isCancelled() {
        return timerTask.isCancelled();
    }

    public int getTime() {
        return time;
    }

    public void cancel() {
        timerTask.cancel();
    }
}
