package me.cahrypt.kitpvpcore.effects;

import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KitPotionEffect {
    private final PotionEffect potionEffect;

    public KitPotionEffect(PotionEffectType type, int durationSeconds, int amplifier) {
        this.potionEffect = new PotionEffect(type, durationSeconds*20, amplifier-1, false, false);
    }

    public KitPotionEffect(PotionEffectType type, int amplifier) {
        this.potionEffect = new PotionEffect(type, Integer.MAX_VALUE, amplifier-1, false, false);
    }

    public KitPotionEffect(PotionEffectType type) {
        this.potionEffect = new PotionEffect(type, Integer.MAX_VALUE, 0, false, false);
    }

    public void applyEffect(Player player) {
        player.addPotionEffect(potionEffect);
    }

    public void removeEffect(Player player) {
        player.removePotionEffect(potionEffect.getType());
    }

    public void applyEffect(KitPlayer kitPlayer) {
        kitPlayer.getPlayer().addPotionEffect(potionEffect);
    }
}
