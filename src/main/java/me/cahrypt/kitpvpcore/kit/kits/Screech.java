package me.cahrypt.kitpvpcore.kit.kits;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.effects.KitPotionEffect;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.kit.Kit;
import me.cahrypt.kitpvpcore.kit.anotation.TriggerAbility;
import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.kit.loadout.Loadout;
import me.cahrypt.kitpvpcore.particle.CircleParticleSystem;
import me.cahrypt.kitpvpcore.particle.KitParticle;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Screech extends Kit {
    private final CircleParticleSystem circleParticleSystem;
    private final KitPotionEffect abilityKitPotionEffect;

    public Screech() {
        super(
                "Screecher Kit",
                new ItemBuilder<>(Material.GHAST_TEAR)
                        .setDisplayName(ChatColor.DARK_GREEN + "Screecher Kit")
                        .build(),
                new Loadout(
                        new ItemBuilder<>(Material.IRON_SWORD)
                                .addEnchantment(Enchantment.DAMAGE_ALL, 3)
                                .setDisplayName(ChatColor.GRAY + "Screecher Sword")
                                .build(),
                        new ItemStack(Material.IRON_HELMET),
                        new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                        new ItemStack(Material.GOLDEN_LEGGINGS),
                        new ItemStack(Material.IRON_BOOTS),
                        new KitPotionEffect[] {
                                new KitPotionEffect(PotionEffectType.SPEED, 2)
                        },
                        KitCoreItems.AbilityItems.SCREECHER_TOOL.getItem()
                )
        );
        this.circleParticleSystem = new CircleParticleSystem(new KitParticle(Particle.REDSTONE, new Particle.DustOptions(Color.GREEN, 2)), 1);
        this.abilityKitPotionEffect = new KitPotionEffect(PotionEffectType.WITHER, 5, 5);
    }

    @TriggerAbility(listenItem = KitCoreItems.AbilityItems.SCREECHER_TOOL)
    public void doAbility(KitPlayer kitPlayer, PlayerInteractEvent event) {
        Player player = kitPlayer.getPlayer();
        Action action = event.getAction();

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            for (double i = 1.0; i <= 6; i += 0.5) {
                double finalI = i;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        circleParticleSystem.spawnParticle(player.getWorld(), player.getLocation(), finalI);
                    }
                }.runTaskLater(JavaPlugin.getPlugin(KitPvPCore.class), (int) finalI);
            }

            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GHAST_HURT, 1, 1);
            forNearbyPlayers(player, 6, abilityKitPotionEffect::applyEffect);
            kitPlayer.setActiveKitCooldown(10, (target, cooldown) -> target.getPlayer().setLevel(cooldown));
        }

    }

    @Override
    public void onKitPlayerClean(KitPlayer kitPlayer) {

    }
}
