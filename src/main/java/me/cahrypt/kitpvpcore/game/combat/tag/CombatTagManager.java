package me.cahrypt.kitpvpcore.game.combat.tag;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class CombatTagManager {
    private final Map<CombatTag, BukkitTask> combatMap;

    public CombatTagManager() {
        this.combatMap = new HashMap<>();
    }

    private Optional<Map.Entry<CombatTag, BukkitTask>> getOptionalCombatEntry(KitPlayer kitPlayer) {
        return combatMap.entrySet()
                .stream()
                .filter(entrySet -> {
                    CombatTag combatTag = entrySet.getKey();
                    return combatTag.getReceiver().isKitPlayer(kitPlayer) || combatTag.getInflicter().isKitPlayer(kitPlayer);
                })
                .findFirst();
    }

    public boolean hasCombatTag(KitPlayer kitPlayer) {
        return getOptionalCombatEntry(kitPlayer).isPresent();
    }

    public CombatTag getCombatTag(KitPlayer kitPlayer) {
        AtomicReference<CombatTag> combatTagAtomicReference = new AtomicReference<>(null);
        getOptionalCombatEntry(kitPlayer).ifPresent(action -> combatTagAtomicReference.set(action.getKey()));
        return combatTagAtomicReference.get();
    }

    public void combatTag(KitPlayer receiver, KitPlayer inflicter) {
        CombatTag combatTag = new CombatTag(receiver, inflicter);
        combatMap.put(combatTag, new BukkitRunnable() {
            @Override
            public void run() {
                combatMap.remove(combatTag);
            }
        }.runTaskLaterAsynchronously(JavaPlugin.getPlugin(KitPvPCore.class), 20*20));
    }

    public void clearTag(KitPlayer kitPlayer) {
        CombatTag combatTag = getCombatTag(kitPlayer);
        combatMap.get(combatTag).cancel();
        combatMap.remove(combatTag);
    }
}
