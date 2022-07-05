package me.cahrypt.kitpvpcore.kit.kits;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.effects.KitPotionEffect;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.kit.Kit;
import me.cahrypt.kitpvpcore.kit.anotation.TriggerAbility;
import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.kit.loadout.Loadout;
import me.cahrypt.kitpvpcore.particle.KitParticle;
import me.cahrypt.kitpvpcore.particle.SphereParticleSystem;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Mage extends Kit {
    private final KitPlayerManager kitPlayerManager;
    private final Set<KitPlayer> invincibleSet;
    private final SphereParticleSystem sphereParticleSystem;

    public Mage() {
        super(
                "Mage Kit",
                new ItemBuilder<>(Material.SHIELD)
                        .setDisplayName(ChatColor.DARK_GREEN + "Mage Kit")
                        .build(),
                new Loadout(
                        new ItemBuilder<>(Material.DIAMOND_SWORD)
                                .setDisplayName(ChatColor.GRAY + "Mage Sword")
                                .build(),
                        new ItemStack(Material.DIAMOND_HELMET),
                        new ItemStack(Material.GOLDEN_CHESTPLATE),
                        new ItemStack(Material.GOLDEN_LEGGINGS),
                        new ItemStack(Material.DIAMOND_BOOTS),
                        new KitPotionEffect[] {
                                new KitPotionEffect(PotionEffectType.INCREASE_DAMAGE),
                        },
                        KitCoreItems.AbilityItems.MAGE_TOOL.getItem()
                )
        );
        this.kitPlayerManager = JavaPlugin.getPlugin(KitPvPCore.class).getKitPlayerManager();
        this.invincibleSet = new HashSet<>();
        this.sphereParticleSystem = new SphereParticleSystem(new KitParticle(Particle.REDSTONE, new Particle.DustOptions(Color.ORANGE, 2)), 2);
    }

    @TriggerAbility(listenItem = KitCoreItems.AbilityItems.MAGE_TOOL)
    public void doAbility(KitPlayer kitPlayer, PlayerInteractEvent event) {
        Player player = kitPlayer.getPlayer();
        kitPlayer.setActiveKitCooldown(30, (target, cooldown) -> target.sendMessage(ChatMessageType.ACTION_BAR, ChatColor.RED + "Cooldown: " + cooldown));
        invincibleSet.add(kitPlayer);
        sphereParticleSystem.spawnParticle(player.getWorld(), player.getLocation().add(0, 1, 0));
        new BukkitRunnable() {
            @Override
            public void run() {
                invincibleSet.remove(kitPlayer);
            }
        }.runTaskLater(JavaPlugin.getPlugin(KitPvPCore.class), 300);
    }

    @Override
    public void onKitPlayerClean(KitPlayer kitPlayer) {
        invincibleSet.remove(kitPlayer);
    }

    @EventHandler
    public void onDamageEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player target)) {
            return;
        }

        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        KitPlayer kitTarget = kitPlayerManager.getKitPlayer(target);

        if (isInARegion(kitTarget)) {
            return;
        }

        if (!invincibleSet.contains(kitTarget)) {
            return;
        }

        event.setCancelled(true);
        sphereParticleSystem.spawnParticle(target.getWorld(), target.getLocation().add(0, 1, 0));
        player.setVelocity(player.getLocation().getDirection().multiply(-1));
    }
}
