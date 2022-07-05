package me.cahrypt.kitpvpcore.leaderboard;

public class LeaderboardSQLStatements {

    public static final String GET_TOP_10_KILLERS = "SELECT playerUUID, kills FROM PlayerStats ORDER BY kills DESC LIMIT 10;";

    public static final String GET_TOP_10_BESTKS = "SELECT playerUUID, bestKS FROM PlayerStats ORDER BY bestKS DESC LIMIT 10;";
}
