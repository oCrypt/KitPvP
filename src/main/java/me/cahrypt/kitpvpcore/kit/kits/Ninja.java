package me.cahrypt.kitpvpcore.kit.kits;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.effects.KitPotionEffect;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.kit.Kit;
import me.cahrypt.kitpvpcore.kit.annotation.TriggerAbility;
import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.kit.loadout.Loadout;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class Ninja extends Kit {
    private final KitPlayerManager kitPlayerManager;
    private final Set<KitPlayer> invisibleSet;
    private final KitPotionEffect abilityPotionEffect;

    public Ninja() {
        super(
                "Ninja Kit",
                new ItemBuilder<>(Material.REDSTONE)
                        .setDisplayName(ChatColor.RED + "Ninja Kit")
                        .build(),
                new Loadout(
                        new ItemBuilder<>(Material.DIAMOND_SWORD)
                                .addEnchantment(Enchantment.DAMAGE_ALL, 2)
                                .setDisplayName(ChatColor.GRAY + "Ninja Sword")
                                .build(),
                        null,
                        null,
                        null,
                        null,
                        new KitPotionEffect[] {
                                new KitPotionEffect(PotionEffectType.INCREASE_DAMAGE),
                                new KitPotionEffect(PotionEffectType.SPEED, 2)
                        },
                        KitCoreItems.AbilityItems.NINJA_DUST.getItem()
                )
        );
        this.kitPlayerManager = JavaPlugin.getPlugin(KitPvPCore.class).getKitPlayerManager();
        this.invisibleSet = new HashSet<>();
        this.abilityPotionEffect = new KitPotionEffect(PotionEffectType.INVISIBILITY, 5, 2);
    }

    @TriggerAbility(listenItem = KitCoreItems.AbilityItems.NINJA_DUST)
    public void doAbility(KitPlayer kitPlayer, PlayerInteractEvent event) {
        Player player = kitPlayer.getPlayer();
        PlayerInventory inventory = player.getInventory();
        player.getInventory().setItemInMainHand(null);
        abilityPotionEffect.applyEffect(player);
        kitPlayer.setActiveKitCooldown(10, (target, cooldown) -> target.sendMessage(ChatMessageType.ACTION_BAR, ChatColor.RED + "Cooldown: " + cooldown));
        invisibleSet.add(kitPlayer);
        new BukkitRunnable() {
            @Override
            public void run() {
                player.getInventory().setItem(1, KitCoreItems.AbilityItems.NINJA_DUST.getItem());
                invisibleSet.remove(kitPlayer);
            }
        }.runTaskLater(JavaPlugin.getPlugin(KitPvPCore.class), 100);
    }

    @Override
    public void onKitPlayerClean(KitPlayer kitPlayer) {
        invisibleSet.remove(kitPlayer);
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

        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(player);

        if (!invisibleSet.contains(kitPlayer)) {
            return;
        }

        abilityPotionEffect.removeEffect(player);
        kitPlayer.sendMessage(ChatColor.RED + "You have hit another player and lost your invisibility!");
        invisibleSet.remove(kitPlayer);

    }
}
