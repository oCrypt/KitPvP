package me.cahrypt.kitpvpcore.stats;

public class StatsSQLStatements {

    protected static final String CREATE_STATS_TABLE = "CREATE TABLE IF NOT EXISTS PlayerStats (" +
            "playerUUID CHAR(36) NOT NULL, " +
            "kills INTEGER NOT NULL, " +
            "deaths INTEGER NOT NULL, " +
            "bestKS SMALLINT NOT NULL, " +
            "PRIMARY KEY (playerUUID)" +
            ");";

    protected static final String FETCH_STATS_FROM_PLAYER = "SELECT * FROM PlayerStats WHERE (playerUUID = ?)";

    protected static final String INSERT_NEW_STATS_FOR_PLAYER = "INSERT INTO PlayerStats (playerUUID, kills, deaths, bestKS) VALUES (?, 0, 0, 0);";

    protected static final String UPDATE_STATS_FOR_PLAYER = "UPDATE PlayerStats SET kills = ?, deaths = ?, bestKS = ? WHERE playerUUID = ?;";
}
