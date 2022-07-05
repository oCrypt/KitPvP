package me.cahrypt.kitpvpcore.scoreboard;

import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

public class  KitScoreboard {
    private final KitPlayer kitPlayer;
    private final Scoreboard scoreboard;
    private final Objective boardObjective;

    private final String killsPrefixFormat;
    private final String deathsPrefixFormat;
    private final String activeKSPrefixFormat;
    private final String bestKSPrefixFormat;
    private final String kdRatioPrefixFormat;
    private final String onlineCountPrefixFormat;

    public KitScoreboard(KitPlayer kitPlayer) {
        this.kitPlayer = kitPlayer;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.boardObjective = scoreboard.registerNewObjective("KitPvP", "dummy", ChatColor.AQUA + "☯" + ChatColor.DARK_AQUA + " KITPVP " + ChatColor.AQUA + "☯");

        this.killsPrefixFormat = ChatColor.AQUA + "Kills: " + ChatColor.WHITE + "%s";
        this.deathsPrefixFormat = ChatColor.AQUA + "Deaths: " + ChatColor.WHITE + "%s";
        this.activeKSPrefixFormat = ChatColor.AQUA + "Kill Streak: " + ChatColor.WHITE + "%s";
        this.bestKSPrefixFormat = ChatColor.AQUA + "Best KS: " + ChatColor.WHITE + "%s";
        this.kdRatioPrefixFormat = ChatColor.AQUA + "K/D Ratio: " + ChatColor.WHITE + "%s";
        this.onlineCountPrefixFormat = ChatColor.AQUA + "Online: " + ChatColor.GREEN + "%1$d / %2$d";

        boardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        createStatDisplay();
        kitPlayer.getPlayer().setScoreboard(scoreboard);
    }

    private void addLine(String name, String entry, String prefix, int score) {
        Team line = scoreboard.registerNewTeam(name);
        line.addEntry(entry);
        line.setPrefix(prefix);
        boardObjective.getScore(entry).setScore(score);
    }

    private void createStatDisplay() {
        addLine("kills", ChatColor.AQUA + "" + ChatColor.WHITE, killsPrefixFormat.formatted(kitPlayer.getKills()), 6);
        addLine("deaths", ChatColor.BLACK + "" + ChatColor.WHITE, deathsPrefixFormat.formatted(kitPlayer.getDeaths()), 5);
        addLine("activeKS", ChatColor.BLUE + "" + ChatColor.WHITE, activeKSPrefixFormat.formatted(kitPlayer.getActiveKS()), 4);
        addLine("bestKS", ChatColor.BOLD + "" + ChatColor.WHITE, bestKSPrefixFormat.formatted(kitPlayer.getBestKS()), 3);
        addLine("ratio", ChatColor.DARK_AQUA + "" + ChatColor.WHITE, kdRatioPrefixFormat.formatted(kitPlayer.getFormattedKDRatio()), 2);
        addLine("onlineCount", ChatColor.DARK_PURPLE + "" + ChatColor.WHITE, onlineCountPrefixFormat.formatted(Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()), 1);
    }

    public void updateStats() {
        scoreboard.getTeam("kills").setPrefix(killsPrefixFormat.formatted(kitPlayer.getKills()));
        scoreboard.getTeam("deaths").setPrefix(deathsPrefixFormat.formatted(kitPlayer.getDeaths()));
        scoreboard.getTeam("activeKS").setPrefix(activeKSPrefixFormat.formatted(kitPlayer.getActiveKS()));
        scoreboard.getTeam("bestKS").setPrefix(bestKSPrefixFormat.formatted(kitPlayer.getBestKS()));
        scoreboard.getTeam("ratio").setPrefix(kdRatioPrefixFormat.formatted(kitPlayer.getFormattedKDRatio()));
        scoreboard.getTeam("onlineCount").setPrefix(onlineCountPrefixFormat.formatted(Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()));
    }
}
