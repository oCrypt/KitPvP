package me.cahrypt.kitpvpcore;

import me.cahrypt.kitpvpcore.commandsys.command.SimpleCommand;
import me.cahrypt.kitpvpcore.commandsys.commands.SpawnCmd;
import me.cahrypt.kitpvpcore.commandsys.commands.StatsCmd;
import me.cahrypt.kitpvpcore.game.combat.killstreak.KillStreakManager;
import me.cahrypt.kitpvpcore.item.events.KitSelector;
import me.cahrypt.kitpvpcore.item.events.Soup;
import me.cahrypt.kitpvpcore.kit.kits.*;
import me.cahrypt.kitpvpcore.kit.listeners.KitAbilityHandler;
import me.cahrypt.kitpvpcore.game.combat.tag.CombatTagManager;
import me.cahrypt.kitpvpcore.game.combat.listeners.CombatListener;
import me.cahrypt.kitpvpcore.game.region.RegionManager;
import me.cahrypt.kitpvpcore.gui.listeners.GUICloseListener;
import me.cahrypt.kitpvpcore.gui.listeners.GUIFlipListener;
import me.cahrypt.kitpvpcore.item.ItemEventHandler;
import me.cahrypt.kitpvpcore.item.ItemManager;
import me.cahrypt.kitpvpcore.kit.KitManager;
import me.cahrypt.kitpvpcore.kit.listeners.KitSelectListener;
import me.cahrypt.kitpvpcore.leaderboard.Leaderboard;
import me.cahrypt.kitpvpcore.leaderboard.LeaderboardHandler;
import me.cahrypt.kitpvpcore.leaderboard.LeaderboardSQLStatements;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import me.cahrypt.kitpvpcore.player.listeners.JoinListener;
import me.cahrypt.kitpvpcore.player.listeners.LeaveListener;
import me.cahrypt.kitpvpcore.scoreboard.KitScoreboardHandler;
import me.cahrypt.kitpvpcore.stats.StatsManager;
import me.cahrypt.kitpvpcore.storage.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class KitPvPCore extends JavaPlugin {
    private ItemManager itemManager;
    private KitManager kitManager;
    private CombatTagManager combatTagManager;
    private DataManager dataManager;
    private RegionManager regionManager;
    private StatsManager statsManager;
    private KitPlayerManager kitPlayerManager;
    private KillStreakManager killStreakManager;
    private KitScoreboardHandler kitScoreboardHandler;
    private LeaderboardHandler leaderboardHandler;

    private void initializeCommands() {
        new SpawnCmd();
        new StatsCmd();
        Bukkit.getLogger().info("Commands Initialized");
    }

    private void initializeListeners() {
        new JoinListener();
        new LeaveListener();

        new KitAbilityHandler();
        new KitSelectListener();

        new ItemEventHandler();

        new CombatListener();

        new GUICloseListener();
        new GUIFlipListener();
        Bukkit.getLogger().info("Listeners Initialized");
    }

    @Override
    public void onEnable() {
        this.itemManager = new ItemManager();
        this.kitManager = new KitManager();
        this.combatTagManager = new CombatTagManager();
        this.dataManager = new DataManager();
        this.regionManager = new RegionManager();
        this.statsManager = new StatsManager();
        this.kitPlayerManager = new KitPlayerManager();
        this.killStreakManager = new KillStreakManager();
        this.kitScoreboardHandler = new KitScoreboardHandler();
        this.leaderboardHandler = new LeaderboardHandler();

        regionManager.initializeRegions();
        itemManager.initializeItemEvents();
        kitManager.initializeKits();
        leaderboardHandler.initializeLeaderboards();

        initializeListeners();
        initializeCommands();


        kitScoreboardHandler.startScoreboard();
        leaderboardHandler.startUpdateTask();
    }

    @Override
    public void onDisable() {
        leaderboardHandler.stopScoreboard();
    }

    public void registerCommand(String string, SimpleCommand command) {
        getCommand(string).setExecutor(command);
    }

    public KitPlayerManager getKitPlayerManager() {
        return kitPlayerManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public CombatTagManager getCombatTagManager() {
        return combatTagManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public KillStreakManager getKillStreakManager() {
        return killStreakManager;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public KitScoreboardHandler getKitScoreboardHandler() {
        return kitScoreboardHandler;
    }

    public LeaderboardHandler getLeaderboardHandler() {
        return leaderboardHandler;
    }
}
