package me.cahrypt.kitpvpcore.storage;

import me.cahrypt.kitpvpcore.KitPvPCore;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;

public class DataManager {
    private final KitPvPCore main;

    public DataManager() {
        this.main = JavaPlugin.getPlugin(KitPvPCore.class);
    }

    public void executeQuery(String preparedStatement) throws SQLException {
        Connection connection = DataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(preparedStatement);

        statement.execute();
        connection.close();
        statement.close();
    }

    public void executeQuery(String preparedStatement, Consumer<PreparedStatement> preparedStatementConsumer) throws SQLException {
        Connection connection = DataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(preparedStatement);

        preparedStatementConsumer.accept(statement);

        statement.execute();
        connection.close();
        statement.close();
    }

    public void useQueryResults(String preparedStatement, Consumer<ResultSet> resultSetConsumer) throws SQLException {
        Connection connection = DataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(preparedStatement);

        statement.execute();

        resultSetConsumer.accept(statement.getResultSet());

        connection.close();
        statement.close();
    }

    public void useQueryResults(String preparedStatement, Consumer<PreparedStatement> preparedStatementConsumer, Consumer<ResultSet> resultSetConsumer) throws SQLException {
        Connection connection = DataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(preparedStatement);

        preparedStatementConsumer.accept(statement);

        statement.execute();

        resultSetConsumer.accept(statement.getResultSet());

        connection.close();
        statement.close();
    }

    public void executeQueryASync(String preparedStatement, Consumer<PreparedStatement> preparedStatementConsumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    executeQuery(preparedStatement, preparedStatementConsumer);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(main);
    }

    public void useQueryResultsASync(String preparedStatement, Consumer<ResultSet> resultSetConsumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    useQueryResults(preparedStatement, resultSetConsumer);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(main);
    }

    public void useQueryResultsASync(String preparedStatement, Consumer<PreparedStatement> preparedStatementConsumer, Consumer<ResultSet> resultSetConsumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    useQueryResults(preparedStatement, preparedStatementConsumer, resultSetConsumer);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(main);
    }
}