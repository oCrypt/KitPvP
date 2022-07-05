package me.cahrypt.kitpvpcore.stats;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.storage.DataManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class StatsManager {
    private final DataManager dataManager;

    public StatsManager() {
        this.dataManager = JavaPlugin.getPlugin(KitPvPCore.class).getDataManager();

        createStatsTable();
    }

    public boolean hasRegisteredStats(UUID playerUUID) {
        AtomicReference<Boolean> hasRegisteredStats = new AtomicReference<>(false);
        try {
            dataManager.useQueryResults(
                    StatsSQLStatements.FETCH_STATS_FROM_PLAYER,
                    preparedStatement -> {
                        try {
                            preparedStatement.setString(1, playerUUID.toString());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    },
                    resultSet -> {
                        try {
                            hasRegisteredStats.set(resultSet.next());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
            );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return hasRegisteredStats.get();
    }

    private void createStatsTable() {
        try {
            dataManager.executeQuery(StatsSQLStatements.CREATE_STATS_TABLE);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateStatsASync(UUID playerUUID, int kills, int deaths, int bestKS) {
        String playerUUIDString = playerUUID.toString();

        dataManager.executeQueryASync(
                StatsSQLStatements.UPDATE_STATS_FOR_PLAYER,
                preparedStatement -> {
                    try {
                        preparedStatement.setInt(1, kills);
                        preparedStatement.setInt(2, deaths);
                        preparedStatement.setInt(3, bestKS);
                        preparedStatement.setString(4, playerUUIDString);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void createStatsASync(UUID playerUUID) {
        String playerUUIDString = playerUUID.toString();

        dataManager.executeQueryASync(
                StatsSQLStatements.INSERT_NEW_STATS_FOR_PLAYER,
                preparedStatement -> {
                    try {
                        preparedStatement.setString(1, playerUUIDString);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    public void withStatsPerformASync(UUID playerUUID, Consumer<ResultSet> statsAction) {
        String playerUUIDString = playerUUID.toString();

        dataManager.useQueryResultsASync(
                StatsSQLStatements.FETCH_STATS_FROM_PLAYER,
                preparedStatement -> {
                    try {
                        preparedStatement.setString(1, playerUUIDString);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                },
                statsAction
        );
    }
}
