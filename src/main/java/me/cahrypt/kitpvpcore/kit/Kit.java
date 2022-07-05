package me.cahrypt.kitpvpcore.kit;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.kit.annotation.InteractAtPlayerAbility;
import me.cahrypt.kitpvpcore.kit.annotation.PassiveAbility;
import me.cahrypt.kitpvpcore.kit.annotation.SpecificInteractAtPlayerAbility;
import me.cahrypt.kitpvpcore.kit.annotation.TriggerAbility;
import me.cahrypt.kitpvpcore.kit.loadout.Loadout;
import me.cahrypt.kitpvpcore.game.region.RegionManager;
import me.cahrypt.kitpvpcore.listener.AListener;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class Kit extends AListener {
    private final KitPlayerManager kitPlayerManager;
    private final RegionManager regionManager;

    private final String name;
    private final ItemStack displayItem;
    private final Loadout loadout;

    private final Map<InteractAtPlayerAbility, Method> interactAtPlayerAnnotationMap;
    private final Map<PassiveAbility, Method> passiveAnnotationMap;
    private final Map<SpecificInteractAtPlayerAbility, Method> specificInteractAtPlayerAnnotationMap;
    private final Map<TriggerAbility, Method> triggerAnnotationMap;

    public Kit(String name, ItemStack displayItem, Loadout loadout) {
        super();

        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
        this.regionManager = main.getRegionManager();

        this.name = name;
        this.displayItem = displayItem;
        this.loadout = loadout;

        this.interactAtPlayerAnnotationMap = getAbilityAnnotationMap(InteractAtPlayerAbility.class);
        this.passiveAnnotationMap = getAbilityAnnotationMap(PassiveAbility.class);
        this.specificInteractAtPlayerAnnotationMap = getAbilityAnnotationMap(SpecificInteractAtPlayerAbility.class);
        this.triggerAnnotationMap = getAbilityAnnotationMap(TriggerAbility.class);

        main.getKitManager().addKit(this);
    }

    private <T extends Annotation> Map<T, Method> getAbilityAnnotationMap(Class<T> annotation) {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotation))
                .collect(Collectors.toMap(method -> method.getAnnotation(annotation), method -> method));
    }

    protected boolean isInARegion(KitPlayer kitPlayer) {
        return regionManager.getSpawnRegion().isInRegion(kitPlayer) || regionManager.isInOtherRegion(kitPlayer);
    }

    protected ItemStack getKitItem() {
        return displayItem;
    }

    protected void forNearbyPlayers(Player causePlayer, double radius, Consumer<Player> playerInRadiusAction) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.getUniqueId().equals(causePlayer.getUniqueId())) {
                continue;
            }

            KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(target);

            if (isInARegion(kitPlayer)) {
                continue;
            }

            if (target.getLocation().distance(causePlayer.getLocation()) <= radius) {
                playerInRadiusAction.accept(target);
            }
        }
    }

    public Map<InteractAtPlayerAbility, Method> getInteractAtPlayerAnnotationMap() {
        return interactAtPlayerAnnotationMap;
    }

    public Map<PassiveAbility, Method> getPassiveAnnotationMap() {
        return passiveAnnotationMap;
    }

    public Map<SpecificInteractAtPlayerAbility, Method> getSpecificInteractAtPlayerAnnotationMap() {
        return specificInteractAtPlayerAnnotationMap;
    }

    public Map<TriggerAbility, Method> getTriggerAnnotationMap() {
        return triggerAnnotationMap;
    }

    public boolean isKit(String name) {
        return this.name.equalsIgnoreCase(name);
    }

    public boolean isKit(Kit kit) {
        return kit.isKit(name);
    }

    public boolean canAbility(KitPlayer kitPlayer) {
        if (isInARegion(kitPlayer)) {
            return false;
        }

        if (kitPlayer.getActiveKit() == null) {
            return false;
        }

        if (!isKit(kitPlayer.getActiveKit())) {
            return false;
        }

        return !kitPlayer.hasKitCooldown();
    }

    public void equipKit(KitPlayer kitPlayer) {
        kitPlayer.setActiveKit(this);
        loadout.applyLoadout(kitPlayer);
        kitPlayer.sendMessage(ChatColor.GREEN + "Applied " + name + "!");
    }

    public void repairKit(KitPlayer kitPlayer) {
        loadout.applyLoadout(kitPlayer);
        kitPlayer.sendMessage(ChatColor.GREEN + "Repaired " + name + "!");
    }

    public abstract void onKitPlayerClean(KitPlayer kitPlayer);
}
