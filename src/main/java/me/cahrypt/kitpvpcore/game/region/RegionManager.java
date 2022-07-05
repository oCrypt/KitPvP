package me.cahrypt.kitpvpcore.game.region;

import me.cahrypt.kitpvpcore.game.region.regions.SpawnRegion;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;

public class RegionManager {
    private GameRegion spawn;
    private final Set<GameRegion> additionalRegionSet;

    public RegionManager() {
        this.additionalRegionSet = new HashSet<>();
    }

    public void initializeRegions() {
        this.spawn = new SpawnRegion();
        Bukkit.getLogger().info("Regions Initialized");
    }

    public boolean isInOtherRegion(KitPlayer kitPlayer) {
        return additionalRegionSet.stream().anyMatch(region -> region.isInRegion(kitPlayer));
    }

    public GameRegion getSpawnRegion() {
        return spawn;
    }
}
