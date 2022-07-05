package me.cahrypt.kitpvpcore.leaderboard;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import me.cahrypt.kitpvpcore.storage.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Leaderboard {
    private final String statFormat;
    private final World world;
    private final Location location;
    private final String statGatheringQuery;

    private final LeaderboardLine title;
    private final List<LeaderboardLine> lines;

    protected final KitPvPCore main;
    protected final KitPlayerManager kitPlayerManager;
    protected final DataManager dataManager;

    public Leaderboard(String title, World world, String statGatheringQuery, Location location) {
        this.statFormat = ChatColor.DARK_AQUA + "%1$d. " + ChatColor.AQUA + "%2$s " + ChatColor.DARK_AQUA + "[%3$d]";
        this.world = world;
        this.location = location;
        this.statGatheringQuery = statGatheringQuery;

        this.title = new LeaderboardLine(title, world, location);
        this.lines = new ArrayList<>();

        this.main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
        this.dataManager = main.getDataManager();

        spawn();
        main.getLeaderboardHandler().addLeaderboard(this);
    }

    private void fillEmptyLines() {
        for (int i = lines.size() + 1; i <= 10; i++) {
            lines.add(new LeaderboardLine(ChatColor.GRAY + "None", world, new Location(
                    world,
                    location.getX(),
                    location.getY() - (0.3 * i),
                    location.getZ()
            )));
        }
    }

    protected void spawn() {
        withTopPlayers((topPlayerMap -> {
            AtomicInteger pos = new AtomicInteger(1);
            topPlayerMap.forEach((name, stat) -> {
                lines.add(new LeaderboardLine(statFormat.formatted(pos.get(), name, stat), world, new Location(
                        world,
                        location.getX(),
                        location.getY() - (0.3 * pos.get()),
                        location.getZ()
                )));
                pos.getAndIncrement();
            });

            fillEmptyLines();
        }));
    }

    public void withTopPlayers(Consumer<Map<String, Integer>> action) {
        dataManager.useQueryResultsASync(statGatheringQuery, resultSet -> {
            Map<String, Integer> topOnlinePlayerMap = new LinkedHashMap<>();

            try {
                while (resultSet.next()) {
                    topOnlinePlayerMap.put(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("playerUUID"))).getName(), resultSet.getInt(2));
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        action.accept(topOnlinePlayerMap);
                    }
                }.runTask(main);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void update() {
        withTopPlayers((topPlayerMap -> {
            AtomicInteger pos = new AtomicInteger(0);
            topPlayerMap.forEach((name, stat) -> {
                lines.get(pos.get()).update(statFormat.formatted(pos.get() + 1, name, stat));
                pos.getAndIncrement();
            });
        }));
    }

    public void remove() {
        title.remove();
        lines.forEach(LeaderboardLine::remove);
    }
}
