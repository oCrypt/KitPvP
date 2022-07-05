package me.cahrypt.kitpvpcore.kit;

import me.cahrypt.kitpvpcore.gui.GUISystem;
import me.cahrypt.kitpvpcore.kit.kits.*;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KitManager {
    private final HashMap<ItemStack, Kit> kitMap;
    private final GUISystem kitSelectionGUISystem;

    public KitManager() {
        this.kitMap = new HashMap<>();
        this.kitSelectionGUISystem = new GUISystem(ChatColor.GRAY + "Kit Selection", 6, this::getKitItems);
    }

    public void initializeKits() {
        new Jester();
        new Screech();
        new Abomination();
        new Ninja();
        new Mage();
        new Superman();
        Bukkit.getLogger().info("Kits Initialized");
    }

    protected void addKit(Kit kit) {
        kitMap.put(kit.getKitItem(), kit);
    }

    public ItemStack[] getKitItems() {
        return kitMap.keySet().toArray(new ItemStack[0]);
    }

    public Set<Kit> getKits() {
        return new HashSet<>(kitMap.values());
    }

    public Kit getSelectedKit(ItemStack clickedItem) {
        return kitMap.get(clickedItem);
    }

    public void openKitSelection(KitPlayer kitPlayer) {
        kitSelectionGUISystem.openGUISystem(kitPlayer);
    }
}

