package me.cahrypt.kitpvpcore.gui;

import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUI {
    private String uiName;
    private final int size;
    private final Inventory inventory;

    public GUI(String uiName, int rows) {
        this.uiName = uiName;
        this.size = (rows*9);
        this.inventory = Bukkit.createInventory(null, size, uiName);
        addBorder();
    }

    private void addBorder() {
        ItemStack item = KitCoreItems.GUIStandardItems.BORDER.getItem();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (i % 9 == 0 || i % 9 == 8) {
                inventory.setItem(i, item);
            }
        }
    }

    protected void setUiName(String uiName) {
        this.uiName = uiName;
    }

    public void open(KitPlayer kitPlayer) {
        kitPlayer.openGUI(inventory, uiName);
    }

    public ItemStack[] addItems(ItemStack... itemStacks) {
        return inventory.addItem(itemStacks).values().toArray(new ItemStack[0]);
    }

    public void setItem(int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }

    public String getUiName() {
        return uiName;
    }

    public int getSize() {
        return size;
    }
}
