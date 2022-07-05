package me.cahrypt.kitpvpcore.player;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.game.combat.tag.CombatTagManager;
import me.cahrypt.kitpvpcore.game.combat.tag.CombatTag;
import me.cahrypt.kitpvpcore.game.region.RegionManager;
import me.cahrypt.kitpvpcore.stats.StatsManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class KitPlayerManager {
    private final CombatTagManager combatTagManager;
    private final RegionManager regionManager;
    private final StatsManager statsManager;

    private final Map<Player, KitPlayer> kitPlayerMap;

    public KitPlayerManager() {
        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.combatTagManager = main.getCombatTagManager();
        this.regionManager = main.getRegionManager();
        this.statsManager = main.getStatsManager();

        this.kitPlayerMap = new HashMap<>();
    }

    public void broadcast(String message) {
        kitPlayerMap.forEach((player, kitPlayer) -> kitPlayer.sendMessage(message));
    }

    public void joinPlayer(Player player) {
        kitPlayerMap.put(player, new KitPlayer(player));
        KitPlayer kitPlayer = kitPlayerMap.get(player);
        regionManager.getSpawnRegion().sendToSpawnPoint(kitPlayer);
        kitPlayer.clean();
    }

    public void quitPlayer(Player player) {
        KitPlayer kitPlayer = kitPlayerMap.get(player);

        if (combatTagManager.hasCombatTag(kitPlayer)) {
            CombatTag combatTag = combatTagManager.getCombatTag(kitPlayer);
            combatTag.getInflicter().kill(kitPlayer);
        }

        statsManager.updateStatsASync(player.getUniqueId(), kitPlayer.getKills(), kitPlayer.getDeaths(), kitPlayer.getBestKS());
        kitPlayer.clean();
        kitPlayerMap.remove(player);
    }

    public KitPlayer getKitPlayer(Player player) {
        return kitPlayerMap.get(player);
    }

    public Collection<KitPlayer> getKitPlayers() {
        return kitPlayerMap.values();
    }
}
