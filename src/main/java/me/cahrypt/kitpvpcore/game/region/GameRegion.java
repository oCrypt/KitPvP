package me.cahrypt.kitpvpcore.game.region;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.game.combat.tag.CombatTagManager;
import me.cahrypt.kitpvpcore.listener.AListener;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;

public abstract class GameRegion extends AListener {
    private final KitPvPCore main;
    private final KitPlayerManager kitPlayerManager;
    private final CombatTagManager combatTagManager;
    private final String name;
    private final Location spawnPoint;
    private final BoundingBox region;
    private final World world;

    public GameRegion(String name, Location spawnPoint, Location loc1, Location loc2) {
        super();
        this.main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
        this.combatTagManager = main.getCombatTagManager();
        this.name = name;
        this.region = new BoundingBox(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
        this.world = loc1.getWorld();
        this.spawnPoint = spawnPoint;

        startAutoRegionClean();
    }

    private void startAutoRegionClean() {
        new BukkitRunnable() {
            @Override
            public void run() {
                world.getNearbyEntities(region).forEach(entity -> {
                    if (!(entity instanceof Player || entity instanceof ArmorStand)) {
                        entity.remove();
                    }
                });
            }
        }.runTaskTimer(main, 0, 20);
    }

    protected String getName() {
        return name;
    }

    public boolean isInRegion(KitPlayer kitPlayer) {
        return isInRegion(kitPlayer.getPlayer().getLocation());
    }

    public boolean isInRegion(Location loc) {
        return region.contains(loc.toVector()) && loc.getWorld().getName().equals(world.getName());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location atLocation = event.getTo();

        if (atLocation == null) {
            return;
        }

        Location fromLocation = event.getFrom();
        Player player = event.getPlayer();
        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(player);

        if (!isInRegion(atLocation) && isInRegion(fromLocation)) {
            onLeave(kitPlayer);
            return;
        }

        if (isInRegion(atLocation) && !isInRegion(fromLocation)) {

            if (combatTagManager.hasCombatTag(kitPlayer)) {
                kitPlayer.sendMessage(ChatColor.RED + "You are still combat logged");
                kitPlayer.getPlayer().setVelocity(event.getTo().toVector().subtract(event.getFrom().toVector()).multiply(-5));
                return;
            }

            kitPlayer.sendMessage(ChatColor.GREEN + "You have entered " + name + " region");
            onEnter(kitPlayer);
        }
    }

    public void sendToSpawnPoint(KitPlayer kitPlayer) {
        kitPlayer.teleport(spawnPoint);
    }

    public abstract void onEnter(KitPlayer kitPlayer);

    public abstract void onLeave(KitPlayer kitPlayer);
}
