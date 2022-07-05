package me.cahrypt.kitpvpcore.kit.kits;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.effects.KitPotionEffect;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.kit.Kit;
import me.cahrypt.kitpvpcore.kit.annotation.PassiveAbility;
import me.cahrypt.kitpvpcore.kit.loadout.Loadout;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.HashSet;
import java.util.Set;

public class Abomination extends Kit {
    private final KitPlayerManager kitPlayerManager;
    private final Set<Player> blinded;

    public Abomination() {
        super(
                "Abomination",
                new ItemBuilder<>(Material.BROWN_WOOL)
                        .setDisplayName(ChatColor.DARK_GRAY + "Abomination Kit")
                        .build(),
                new Loadout(
                        new ItemBuilder<>(Material.IRON_SWORD)
                                .addEnchantment(Enchantment.DAMAGE_ALL, 3)
                                .setDisplayName(ChatColor.GRAY + "Abomination Sword")
                                .build(),
                        new ItemStack(Material.BROWN_WOOL),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_CHESTPLATE)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.BLACK))
                                .build(),
                        new ItemBuilder<LeatherArmorMeta>(Material.LEATHER_LEGGINGS)
                                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                                .useMeta(leatherArmorMeta -> leatherArmorMeta.setColor(Color.BLACK))
                                .build(),
                        new ItemStack(Material.CHAINMAIL_BOOTS),
                        new KitPotionEffect[] {}
                )
        );
        this.kitPlayerManager = JavaPlugin.getPlugin(KitPvPCore.class).getKitPlayerManager();
        this.blinded = new HashSet<>();
    }

    @PassiveAbility
    public void doAbility(KitPlayer kitPlayer, PlayerMoveEvent event) {
        Player player = event.getPlayer();
        forNearbyPlayers(player, 4, target -> {

            PlayerInventory targetInventory = target.getInventory();
            ItemStack targetHelmet = targetInventory.getHelmet();

            target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 40, 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 0));

            if (blinded.contains(player)) {
                return;
            }

            blinded.add(player);
            targetInventory.setHelmet(new ItemStack(Material.BROWN_WOOL));
            KitPlayer kitTarget = kitPlayerManager.getKitPlayer(target);
            Kit targetKit = kitTarget.getActiveKit();

            new BukkitRunnable() {
                @Override
                public void run() {
                    blinded.remove(player);
                    if (kitTarget.getActiveKit() != null && kitTarget.getActiveKit().isKit(targetKit)) {
                        targetInventory.setHelmet(targetHelmet);
                    }
                }
            }.runTaskLater(JavaPlugin.getPlugin(KitPvPCore.class), 40);
        });
    }

    @Override
    public void onKitPlayerClean(KitPlayer kitPlayer) {

    }
}
