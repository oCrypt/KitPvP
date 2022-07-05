package me.cahrypt.kitpvpcore.gui;

import me.cahrypt.kitpvpcore.item.items.KitCoreItems;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.function.Supplier;

public class GUISystem {
    private final String systemName;
    private final int rows;
    private final Supplier<ItemStack[]> itemStackArraySupplier;

    public GUISystem(String systemName, int rowsPerGUI, Supplier<ItemStack[]> itemStackArraySupplier) {
        this.systemName = systemName;
        this.rows = rowsPerGUI;
        this.itemStackArraySupplier = itemStackArraySupplier;
    }

    private void assembleGUI(List<GUI> guiList) {
        GUI gui = new GUI(systemName, rows);
        ItemStack[] extras = gui.addItems(itemStackArraySupplier.get());
        guiList.add(gui);

        if (extras.length != 0) {
            assembleGUI(guiList);
        }
    }

    private void setupUISystemNames(List<GUI> guiList) {
        for (int i = 0; i < guiList.size(); i++) {
            GUI ui = guiList.get(0);
            ui.setUiName(ui.getUiName() + ChatColor.GREEN + " - Page " + (i + 1));
        }
    }

    private void setupUISystemArrows(List<GUI> guiList) {
        ItemStack fwdItem = KitCoreItems.GUIStandardItems.FORWARD_ARROW.getItem();
        ItemStack bkItem = KitCoreItems.GUIStandardItems.BACKWARD_ARROW.getItem();
        int finalUIPos = guiList.size();

        if (finalUIPos == 1) {
            return;
        }

        GUI entryUI = guiList.get(0);
        int entryUISize = entryUI.getSize();

        entryUI.setItem(entryUISize - 1, fwdItem);

        GUI finalUI = guiList.get(finalUIPos);
        int finalUISize = finalUI.getSize();

        finalUI.setItem(finalUISize - 10, bkItem);

        for (int i = 1; i < finalUIPos; i++) {
            GUI middleUI = guiList.get(i);
            middleUI.setItem(i - 1, fwdItem);
            middleUI.setItem(i - 9, bkItem);
        }
    }

    public void openGUISystem(KitPlayer kitPlayer) {
        List<GUI> guiList = new ArrayList<>();

        assembleGUI(guiList);
        setupUISystemNames(guiList);
        setupUISystemArrows(guiList);

        guiList.get(0).open(kitPlayer);
        kitPlayer.setActiveGUISystem(guiList);
    }
}
