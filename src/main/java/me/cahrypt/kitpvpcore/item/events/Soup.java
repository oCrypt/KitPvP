package me.cahrypt.kitpvpcore.item.events;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.game.region.RegionManager;
import me.cahrypt.kitpvpcore.item.items.ItemBuilder;
import me.cahrypt.kitpvpcore.item.ItemEvent;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Soup extends ItemEvent {
    private final KitPlayerManager kitPlayerManager;
    private final RegionManager regionManager;

    public Soup() {
        super(
                new ItemBuilder<>(Material.MUSHROOM_STEW)
                .setDisplayName(ChatColor.GRAY + "Soup")
                .build()
        );

        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();
        this.regionManager = main.getRegionManager();
    }

    @Override
    public void onPlayerUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(player);

        if (player.getHealth() == 20.0) {
            return;
        }

        if (regionManager.getSpawnRegion().isInRegion(kitPlayer) || regionManager.isInOtherRegion(kitPlayer)) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        inventory.setItemInMainHand(new ItemStack(Material.BOWL));
        kitPlayer.addHealth(6.0);
    }
}
