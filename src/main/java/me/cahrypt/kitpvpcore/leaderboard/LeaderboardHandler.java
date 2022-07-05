package me.cahrypt.kitpvpcore.leaderboard;

import me.cahrypt.kitpvpcore.KitPvPCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.Set;

public class LeaderboardHandler {
    private final Set<Leaderboard> leaderboards;
    private BukkitTask updateTask;

    public LeaderboardHandler() {
        this.leaderboards = new HashSet<>();
    }

    public void initializeLeaderboards() {
        new Leaderboard(ChatColor.GOLD + "" + ChatColor.BOLD + "Kills Leaderboard",
                Bukkit.getWorld("world"),
                LeaderboardSQLStatements.GET_TOP_10_KILLERS,
                new Location(Bukkit.getWorld("world"), 5, 65, 6)
        );

        new Leaderboard(ChatColor.GOLD + "" + ChatColor.BOLD + "KillStreak Leaderboard",
                Bukkit.getWorld("world"),
                LeaderboardSQLStatements.GET_TOP_10_BESTKS,
                new Location(Bukkit.getWorld("world"), -5, 65, 6)
        );
    }

    protected void addLeaderboard(Leaderboard leaderboard) {
        leaderboards.add(leaderboard);
    }

    public void startUpdateTask() {
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                leaderboards.forEach(Leaderboard::update);
            }
        }.runTaskTimer(JavaPlugin.getPlugin(KitPvPCore.class), 100, 15*20);
    }

    public void stopScoreboard() {
        updateTask.cancel();
        leaderboards.forEach(Leaderboard::remove);
    }
}
