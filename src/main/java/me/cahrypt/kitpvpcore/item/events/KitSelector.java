package me.cahrypt.kitpvpcore.item.events;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.item.ItemEvent;
import me.cahrypt.kitpvpcore.kit.KitManager;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class KitSelector extends ItemEvent {
    private final KitPlayerManager kitPlayerManager;
    private final KitManager kitManager;

    public KitSelector() {
        super(
                new ItemBuilder<>(Material.CHEST)
                        .setDisplayName(ChatColor.LIGHT_PURPLE + "Kit Selector")
                        .build()
        );

        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
        this.kitManager = main.getKitManager();
    }

    @Override
    public void onPlayerUseItem(PlayerInteractEvent event) {
        kitManager.openKitSelection(kitPlayerManager.getKitPlayer(event.getPlayer()));
    }
}
