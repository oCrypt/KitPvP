package me.cahrypt.kitpvpcore.kit.kits;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.effects.KitPotionEffect;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.kit.Kit;
import me.cahrypt.kitpvpcore.kit.annotation.TriggerAbility;
import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.kit.loadout.Loadout;
import me.cahrypt.kitpvpcore.particle.CapeParticleSystem;
import me.cahrypt.kitpvpcore.particle.KitParticle;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Superman extends Kit {
    private final CapeParticleSystem capeParticleSystem;

    public Superman() {
        super(
                "Superman Kit",
                new ItemBuilder<>(Material.RAW_GOLD)
                        .setDisplayName(ChatColor.RED + "Superman Kit")
                        .build(),
                new Loadout(
                        new ItemBuilder<>(Material.NETHERITE_SWORD)
                                .setDisplayName(ChatColor.GRAY + "Superman Sword")
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_HELMET)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addEnchantment(Enchantment.DURABILITY, 5)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.BLUE))
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_CHESTPLATE)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .addEnchantment(Enchantment.DURABILITY, 5)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.RED))
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_LEGGINGS)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .addEnchantment(Enchantment.DURABILITY, 5)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.BLUE))
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_BOOTS)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                                .addEnchantment(Enchantment.DURABILITY, 5)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.BLUE))
                                .build(),
                        new KitPotionEffect[] {
                                new KitPotionEffect(PotionEffectType.INCREASE_DAMAGE),
                        },
                        KitCoreItems.AbilityItems.SUPERMAN_FEATHER.getItem()
                )
        );

        KitParticle bp = new KitParticle(Particle.REDSTONE, new Particle.DustOptions(Color.BLUE, 2));
        KitParticle rp = new KitParticle(Particle.REDSTONE, new Particle.DustOptions(Color.RED, 2));
        this.capeParticleSystem = new CapeParticleSystem(new KitParticle[][] {
                        {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                        {null, bp, bp, bp, bp, null, null, null, null, null, null, null, bp, bp, bp, bp, null, null},
                        {null, null, bp, bp, bp, bp, bp, null, null, null, bp, bp, bp, bp, bp, null, null, null},
                        {null, null, null, rp, rp, rp, rp, rp, rp, rp, rp, rp, rp, rp, null, null, null, null},
                        {null, null, null, null, rp, rp, rp, rp, rp, rp, rp, rp, rp, null, null, null, null, null},
                        {null, null, null, null, rp, rp, rp, rp, null, rp, rp, rp, rp, null, null, null, null, null},
                        {null, null, null, null, null, rp, rp, rp, null, rp, rp, rp, null, null, null, null, null, null},
                        {null, null, null, null, null, bp, bp, null, null, null, bp, bp, null, null, null, null, null, null},
                        {null, null, null, null, bp, bp, null, null, null, null, null, bp, bp, null, null, null, null, null}
        }, 0.2);
    }

    @TriggerAbility(listenItem = KitCoreItems.AbilityItems.SUPERMAN_FEATHER)
    public void doAbility(KitPlayer kitPlayer, PlayerInteractEvent event) {
        Player player = kitPlayer.getPlayer();
        kitPlayer.setActiveKitCooldown(30, (target, cooldown) -> {
            target.sendMessage(ChatMessageType.ACTION_BAR, ChatColor.RED + "Cooldown: " + cooldown);
            if (cooldown >= 10) {
                capeParticleSystem.spawnParticle(player.getWorld(), player.getLocation());

                if (cooldown % 5 == 0) {
                    target.sendMessage(ChatColor.AQUA + "You have " + (cooldown - 10) + " seconds of fly remaining!");
                }
            }
        });
        player.setAllowFlight(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                player.setAllowFlight(false);
            }
        }.runTaskLater(JavaPlugin.getPlugin(KitPvPCore.class), 400);
    }

    @Override
    public void onKitPlayerClean(KitPlayer kitPlayer) {
        Player player = kitPlayer.getPlayer();
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
