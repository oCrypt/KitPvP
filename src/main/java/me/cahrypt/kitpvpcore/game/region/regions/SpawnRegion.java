package me.cahrypt.kitpvpcore.game.region.regions;

import me.cahrypt.kitpvpcore.game.region.GameRegion;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class SpawnRegion extends GameRegion {

    public SpawnRegion() {
        super("spawn",
                new Location(Bukkit.getWorld("world"), 0, 63, 0),
                new Location(Bukkit.getWorld("world"), 11, 100, 11),
                new Location(Bukkit.getWorld("world"), -10, 0, -10)
        );
    }

    @Override
    public void onEnter(KitPlayer kitPlayer) {
        kitPlayer.clean();
    }

    @Override
    public void onLeave(KitPlayer kitPlayer) {
        if (kitPlayer.getActiveKit() == null) {
            sendToSpawnPoint(kitPlayer);
            kitPlayer.sendMessage(ChatColor.RED + "Select a kit and leave spawn to fight!");
        }
    }
}
