package me.cahrypt.kitpvpcore.kit.listeners;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.kit.Kit;
import me.cahrypt.kitpvpcore.listener.AListener;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class KitAbilityHandler extends AListener {
    private final KitPlayerManager kitPlayerManager;
    private final Set<Kit> kitSet;

    public KitAbilityHandler() {
        super();
        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
        this.kitSet = main.getKitManager().getKits();
    }

    private void handleAbility(KitPlayer kitPlayer, Consumer<Kit> actionIfPresentConsumer) {
        kitSet.stream()
                .filter(kit -> kit.canAbility(kitPlayer))
                .findFirst()
                .ifPresent(actionIfPresentConsumer);
    }

    private <T extends Annotation> void iterateAnnotationMap(Map<T, Method> annotationMap, BiConsumer<T, Method> actionBiConsumer) {
        annotationMap.forEach(actionBiConsumer);
    }

    public void addKitAbility(Kit kit) {
        kitSet.add(kit);
    }

    @EventHandler
    public void onPlayerInteractAtPlayer(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(player);
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!(event.getRightClicked() instanceof Player target)) {
            return;
        }

        KitPlayer kitTarget = kitPlayerManager.getKitPlayer(target);

        handleAbility(kitPlayer, kit -> {
            iterateAnnotationMap(kit.getSpecificInteractAtPlayerAnnotationMap(), ((annotation, method) -> {
                if (annotation.listenItem().getItem().isSimilar(item)) {
                    try {
                        method.invoke(kit, kitPlayer, kitTarget, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    event.setCancelled(true);
                }
            }));

            iterateAnnotationMap(kit.getInteractAtPlayerAnnotationMap(), (annotation, method) -> {
                try {
                    method.invoke(kit, kitPlayer, kitTarget, event);
                    event.setCancelled(true);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(player);

        handleAbility(kitPlayer, kit -> iterateAnnotationMap(kit.getPassiveAnnotationMap(), ((annotation, method) -> {
            try {
                method.invoke(kit, kitPlayer, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        })));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(player);
        ItemStack item = player.getInventory().getItemInMainHand();

        handleAbility(kitPlayer, kit -> iterateAnnotationMap(kit.getTriggerAnnotationMap(), ((annotation, method) -> {
            if (annotation.listenItem().getItem().isSimilar(item)) {
                try {
                    method.invoke(kit, kitPlayer, event);
                    event.setCancelled(true);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        })));
    }
}
