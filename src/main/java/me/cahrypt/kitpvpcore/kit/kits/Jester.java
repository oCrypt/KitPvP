package me.cahrypt.kitpvpcore.kit.kits;

import me.cahrypt.kitpvpcore.effects.KitPotionEffect;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.kit.Kit;
import me.cahrypt.kitpvpcore.kit.anotation.TriggerAbility;
import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.kit.loadout.Loadout;
import me.cahrypt.kitpvpcore.particle.CircleParticleSystem;
import me.cahrypt.kitpvpcore.particle.KitParticle;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Jester extends Kit {
    private final CircleParticleSystem circleParticleSystem;

    public Jester() {
        super(
                "Jester Kit",
                new ItemBuilder<>(Material.CARVED_PUMPKIN)
                        .setDisplayName(ChatColor.YELLOW + "Jester Kit")
                        .build(),
                new Loadout(
                        new ItemBuilder<>(Material.DIAMOND_SWORD)
                                .addEnchantment(Enchantment.DAMAGE_ALL, 1)
                                .setDisplayName(ChatColor.GRAY + "PvP Sword")
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_HELMET)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .addEnchantment(Enchantment.DURABILITY, 5)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.RED))
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_CHESTPLATE)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .addEnchantment(Enchantment.DURABILITY, 5)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.BLUE))
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_LEGGINGS)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .addEnchantment(Enchantment.DURABILITY, 5)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.GREEN))
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_BOOTS)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .addEnchantment(Enchantment.DURABILITY, 5)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.RED))
                                .build(),
                        new KitPotionEffect[] {
                                new KitPotionEffect(PotionEffectType.INCREASE_DAMAGE)
                        },
                        KitCoreItems.AbilityItems.JESTER_STAFF.getItem()
                )
        );
        this.circleParticleSystem = new CircleParticleSystem(new KitParticle(Particle.SNOWBALL), 1);
    }



    @TriggerAbility(listenItem = KitCoreItems.AbilityItems.JESTER_STAFF)
    public void doAbility(KitPlayer kitPlayer, PlayerInteractEvent event) {
        Player player = kitPlayer.getPlayer();
        Action action = event.getAction();

        if (player.isSneaking() && action == Action.LEFT_CLICK_AIR) {
            player.launchProjectile(Snowball.class);
            circleParticleSystem.spawnParticle(player.getWorld(), player.getLocation());
            return;
        }

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            forNearbyPlayers(player, 6, target -> target.openInventory(target.getInventory()));
            kitPlayer.setActiveKitCooldown(15, (target, cooldown) -> target.sendMessage(ChatMessageType.ACTION_BAR, ChatColor.RED + "Cooldown: " + cooldown));
            return;
        }

        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            forNearbyPlayers(player, 5, target -> {
                Random random = new Random();
                Location location = target.getLocation();
                target.teleport(new Location(
                        location.getWorld(),
                        location.getX(),
                        location.getY(),
                        location.getZ(),
                        random.nextFloat(-360, 360),
                        random.nextFloat(-360, 360)));
            });
            kitPlayer.setActiveKitCooldown(15, (target, cooldown) -> target.sendMessage(ChatMessageType.ACTION_BAR, ChatColor.RED + "Cooldown: " + cooldown));
        }
    }

    @Override
    public void onKitPlayerClean(KitPlayer kitPlayer) {

    }
}
