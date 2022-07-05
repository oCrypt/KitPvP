package me.cahrypt.kitpvpcore.kit.loadout;

import me.cahrypt.kitpvpcore.effects.KitPotionEffect;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;

public class Loadout {
    private final ItemStack weapon;
    private final ItemStack healingItem;
    private final ItemStack[] armorContents;
    private final KitPotionEffect[] effects;
    private final ItemStack[] extraItems;

    public Loadout(ItemStack weapon, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, KitPotionEffect[] effects, ItemStack... extraItems) {
        this.weapon = weapon;
        this.healingItem = KitCoreItems.ComplexItems.SOUP.getItem();
        this.armorContents = new ItemStack[] { boots, leggings, chestplate, helmet };
        this.effects = effects;
        this.extraItems = extraItems;
    }

    public void applyLoadout(KitPlayer kitPlayer) {
        Player player = kitPlayer.getPlayer();
        PlayerInventory inventory = player.getInventory();

        Arrays.stream(effects).forEach(kitPotionEffect -> kitPotionEffect.applyEffect(player));

        inventory.setItemInMainHand(weapon);
        Arrays.stream(extraItems).forEach(extraItem -> inventory.addItem(extraItems));

        inventory.setArmorContents(armorContents);
        inventory.setItem(8, new ItemBuilder<>(Material.COOKED_BEEF).setAmount(64).build());

        for(int i = 0; i < 36; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, healingItem);
            }
        }
    }
}
