package me.cahrypt.kitpvpcore.game.combat.killstreak;

import me.cahrypt.kitpvpcore.player.KitPlayer;

import java.util.HashMap;
import java.util.Map;

public class KillStreakManager {
    private final Map<KitPlayer, Integer> killStreakMap;

    public KillStreakManager() {
        this.killStreakMap = new HashMap<>();
    }

    public void setKillStreak(KitPlayer kitPlayer, int amount) {
        killStreakMap.put(kitPlayer, amount);
    }

    public void incrementKillStreak(KitPlayer kitPlayer) {
        if (!killStreakMap.containsKey(kitPlayer)) {
            killStreakMap.put(kitPlayer, 1);
            return;
        }

        setKillStreak(kitPlayer, killStreakMap.get(kitPlayer) + 1);
    }

    public void removeKillStreak(KitPlayer kitPlayer) {
        killStreakMap.remove(kitPlayer);
    }

    public boolean hasKillStreak(KitPlayer kitPlayer) {
        return killStreakMap.containsKey(kitPlayer);
    }

    public Integer getKillStreak(KitPlayer kitPlayer) {
        return killStreakMap.get(kitPlayer);
    }

}
