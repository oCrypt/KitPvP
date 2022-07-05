package me.cahrypt.kitpvpcore.game.combat.listeners;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.game.combat.killstreak.KillStreakManager;
import me.cahrypt.kitpvpcore.game.combat.tag.CombatTag;
import me.cahrypt.kitpvpcore.game.combat.tag.CombatTagManager;
import me.cahrypt.kitpvpcore.game.region.RegionManager;
import me.cahrypt.kitpvpcore.listener.AListener;
import me.cahrypt.kitpvpcore.particle.KitParticle;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class CombatListener extends AListener {
    private final KitPlayerManager kitPlayerManager;
    private final CombatTagManager combatTagManager;
    private final RegionManager regionManager;
    private final KillStreakManager killStreakManager;

    private final KitParticle bloodParticle;

    public CombatListener() {
        super();
        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
        this.combatTagManager = main.getCombatTagManager();
        this.regionManager = main.getRegionManager();
        this.killStreakManager = main.getKillStreakManager();

        this.bloodParticle = new KitParticle(Particle.CRIMSON_SPORE);
    }

    private boolean isInRegion(KitPlayer kitPlayer) {
        return regionManager.getSpawnRegion().isInRegion(kitPlayer) || regionManager.isInOtherRegion(kitPlayer);
    }

    private KitPlayer obtainProcessedPlayer(EntityDamageEvent event, Entity entity) {
        if (!(entity instanceof Player player)) {
            return null;
        }

        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(player);

        if (isInRegion(kitPlayer)) {
            event.setCancelled(true);
            return null;
        }

        return kitPlayer;
    }

    private void playHitEffect(Entity victim) {
        World world = victim.getWorld();
        world.playSound(victim, Sound.BLOCK_COPPER_BREAK, 3, 1);
        bloodParticle.spawnParticle(world, victim.getLocation());
    }

    private void handleDeath(KitPlayer victim) {
        CombatTag combatTag = combatTagManager.getCombatTag(victim);

        if (combatTag == null) {
            victim.die();
            return;
        }

        KitPlayer lastInflicter = combatTag.getInflicter();

        if (lastInflicter == null) {
            victim.die();
        } else {
            victim.dieTo(lastInflicter);
            lastInflicter.kill(victim);
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
            return;
        }

        Entity entityVictim = event.getEntity();

        KitPlayer kitVictim = obtainProcessedPlayer(event, entityVictim);

        if (kitVictim == null) {
            return;
        }

        if (event.getDamage() >= kitVictim.getPlayer().getHealth()) {
            event.setCancelled(true);
            handleDeath(kitVictim);
        }

    }

    @EventHandler
    public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
        Entity entityDamager = event.getDamager();
        Entity entityVictim = event.getEntity();

        KitPlayer kitVictim = obtainProcessedPlayer(event, entityVictim);

        if (kitVictim == null) {
            return;
        }

        KitPlayer kitDamager = obtainProcessedPlayer(event, entityDamager);
        playHitEffect(entityVictim);

        if (event.getDamage() >= kitVictim.getPlayer().getHealth()) {

            if (kitDamager == null) {
                handleDeath(kitVictim);
                return;
            }

            if (killStreakManager.hasKillStreak(kitVictim) && killStreakManager.getKillStreak(kitVictim) >= 5) {
                kitPlayerManager.broadcast(
                        ChatColor.AQUA + "" + ChatColor.BOLD +
                        kitVictim.getPlayer().getName() +
                        " has died with a kill streak of " +
                        killStreakManager.getKillStreak(kitVictim) +
                        " to " +
                        kitDamager.getPlayer().getName());
            }

            kitDamager.kill(kitVictim);
            killStreakManager.incrementKillStreak(kitDamager);
            return;
        }

        if (combatTagManager.hasCombatTag(kitVictim)) {
            combatTagManager.clearTag(kitVictim);
        }

        combatTagManager.combatTag(kitVictim, kitDamager);
    }
}
