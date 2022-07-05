package me.cahrypt.kitpvpcore.player;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.game.combat.killstreak.KillStreakManager;
import me.cahrypt.kitpvpcore.game.combat.tag.CombatTagManager;
import me.cahrypt.kitpvpcore.game.region.GameRegion;
import me.cahrypt.kitpvpcore.scoreboard.KitScoreboard;
import me.cahrypt.kitpvpcore.stats.StatsManager;
import me.cahrypt.kitpvpcore.timer.Timer;
import me.cahrypt.kitpvpcore.gui.GUI;
import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.kit.Kit;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class KitPlayer {
    private final CombatTagManager combatTagManager;
    private final KillStreakManager killStreakManager;
    private final StatsManager statsManager;

    private final DecimalFormat kdFormat;
    private final GameRegion spawn;
    private final Player player;
    private final KitScoreboard kitScoreboard;

    private Kit activeKit;
    private List<GUI> activeGUISystem;
    private Timer activeKitCooldown;

    private int kills;
    private int deaths;
    private int bestKS;

    public KitPlayer(Player player) {
        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.combatTagManager = main.getCombatTagManager();
        this.killStreakManager = main.getKillStreakManager();
        this.statsManager = main.getStatsManager();

        this.kdFormat = new DecimalFormat("#.00");
        this.spawn = main.getRegionManager().getSpawnRegion();
        this.player = player;
        this.kitScoreboard = new KitScoreboard(this);

        this.activeKit = null;
        this.activeGUISystem = null;
        this.activeKitCooldown = null;

        registerStats();
    }

    private void registerStats() {
        UUID playerUUID = player.getUniqueId();

        if (!statsManager.hasRegisteredStats(playerUUID)) {
            statsManager.createStatsASync(playerUUID);

            kills = 0;
            deaths = 0;
            bestKS = 0;

            return;
        }

        statsManager.withStatsPerformASync(player.getUniqueId(), resultSet -> {
            try {
                resultSet.next();
                kills = resultSet.getInt(2);
                deaths = resultSet.getInt(3);
                bestKS = resultSet.getInt(4);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public boolean isKitPlayer(KitPlayer kitPlayer) {
        return player.getName().equals(kitPlayer.getPlayer().getName());
    }

    public void setActiveKit(Kit kit) {
        activeKit = kit;
    }

    public void setActiveGUISystem(List<GUI> activeGUISystem) {
        this.activeGUISystem = activeGUISystem;
    }

    public void setActiveKitCooldown(int cooldownSeconds, BiConsumer<KitPlayer, Integer> actionPerSecondConsumer) {
        this.activeKitCooldown = new Timer(this, cooldownSeconds, target -> {
            activeKitCooldown = null;
            sendMessage(ChatColor.GREEN + "Cooldown expired!");
        }, actionPerSecondConsumer);
    }

    public void setInventory(Inventory inventory) {
        player.getInventory().setContents(inventory.getContents());
    }

    public void clean() {
        if (activeKitCooldown != null) {
            activeKitCooldown.cancel();
            this.activeKitCooldown = null;
        }

        if (activeKit != null) {
            this.activeKit = null;
        }

        if (killStreakManager.hasKillStreak(this)) {
            sendMessage(ChatColor.RED + "You have lost your kill streak of " + killStreakManager.getKillStreak(this) + "!");
            bestKS = Math.max(killStreakManager.getKillStreak(this), bestKS);
            killStreakManager.removeKillStreak(this);
        }

        if (combatTagManager.hasCombatTag(this)) {
            combatTagManager.clearTag(this);
        }

        player.setExp(0);
        player.setLevel(0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
        player.getInventory().clear();
        player.getInventory().addItem(KitCoreItems.ComplexItems.KIT_CHEST.getItem());
    }

    public void die() {
        sendMessage(ChatColor.RED + "You have died!");
        spawn.sendToSpawnPoint(this);
        clean();
        deaths++;
    }

    public void dieTo(KitPlayer killer) {
        sendMessage(ChatColor.RED + "You have died to " + killer.getPlayer().getName() + "!");
        spawn.sendToSpawnPoint(this);
        clean();
        deaths++;
    }

    public void kill(KitPlayer victim) {
        sendMessage(ChatColor.GOLD + "You have killed " + victim.getPlayer().getName() + "!" + ChatColor.GREEN + " +3 SOUP");
        ItemStack soup = KitCoreItems.ComplexItems.SOUP.getItem();
        player.getInventory().addItem(soup, soup, soup);
        kills++;
    }

    public void sendMessage(String message) {
        player.sendMessage(ChatColor.GOLD + "KITPVP -> " + message);
    }

    public void sendMessage(ChatMessageType chatMessageType, String message) {
        player.spigot().sendMessage(chatMessageType, new TextComponent(message));
    }

    public void teleport(Location location) {
        sendMessage(ChatColor.GREEN + "You have been teleported");
        player.teleport(location);
    }

    public void openGUI(Inventory inventory, String uiName) {
        sendMessage(ChatColor.GREEN + "You are now viewing menu " + uiName);
        player.openInventory(inventory);
    }

    public void clearGUISystem() {
        this.activeGUISystem = null;
    }

    public void addHealth(double desiredHealthIncrease) {
        player.setHealth((Math.min(desiredHealthIncrease + player.getHealth(), 20.0)));
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public int getActiveKS() {
        return (killStreakManager.hasKillStreak(this) ? killStreakManager.getKillStreak(this) : 0);
    }

    public int getBestKS() {
        return bestKS;
    }

    public float getKDRatio() {
        return deaths != 0 ? ((float) kills / (float) deaths) : kills;
    }

    public String getFormattedKDRatio() {
        return kdFormat.format(getKDRatio());
    }

    public boolean hasKitCooldown() {
        return activeKitCooldown != null;
    }

    public Kit getActiveKit() {
        return activeKit;
    }

    public List<GUI> getActiveGUISystem() {
        return activeGUISystem;
    }

    public KitScoreboard getKitScoreboard() {
        return kitScoreboard;
    }

    public Player getPlayer() {
        return player;
    }

}
