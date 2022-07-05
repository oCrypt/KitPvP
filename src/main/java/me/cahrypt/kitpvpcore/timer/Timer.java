package me.cahrypt.kitpvpcore.timer;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Timer {
    private int cooldown;
    private final BukkitTask cooldownTask;

    public Timer(KitPlayer kitPlayer, int timerSeconds, Consumer<KitPlayer> onFinishConsumer, BiConsumer<KitPlayer, Integer> actionPerSecondConsumer) {
        this.cooldown = timerSeconds;
        this.cooldownTask = new BukkitRunnable() {
            @Override
            public void run() {
                actionPerSecondConsumer.accept(kitPlayer, cooldown);

                if (cooldown == 0) {
                    onFinishConsumer.accept(kitPlayer);
                    cancel();
                    return;
                }

                cooldown -= 1;
            }
        }.runTaskTimer(JavaPlugin.getPlugin(KitPvPCore.class), 0, 20);
    }

    public boolean isCancelled() {
        return cooldownTask.isCancelled();
    }

    public int getCooldownTime() {
        return cooldown;
    }

    public void cancel() {
        cooldownTask.cancel();
    }
}
