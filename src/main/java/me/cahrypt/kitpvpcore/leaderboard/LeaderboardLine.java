package me.cahrypt.kitpvpcore.leaderboard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

public class LeaderboardLine {
    private final ArmorStand line;

    public LeaderboardLine(String text, World world, Location location) {
        this.line = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        line.setCustomName(text);

        line.setCustomNameVisible(true);
        line.setInvulnerable(true);
        line.setInvisible(true);
        line.setGravity(false);
        line.setCanPickupItems(false);
    }

    public void update(String text) {
        line.setCustomName(text);
    }

    public void remove() {
        line.remove();
    }
}
