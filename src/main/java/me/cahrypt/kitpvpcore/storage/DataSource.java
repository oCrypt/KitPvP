package me.cahrypt.kitpvpcore.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    static {
        config.setJdbcUrl("jdbc:mysql://u2_PTydzVrkqD:GAFiKJpC.IA1CkiU4.GZpPP5@db.fumaz.dev:3306/s2_KITPVP");
        config.setUsername("u2_PTydzVrkqD");
        config.setPassword("GAFiKJpC.IA1CkiU4.GZpPP5");
        config.addDataSourceProperty("cachePrepStmts" , "true");
        config.addDataSourceProperty("prepStmtCacheSize" , "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        ds = new HikariDataSource(config);
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}