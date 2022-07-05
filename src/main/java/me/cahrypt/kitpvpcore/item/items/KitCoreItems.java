package me.cahrypt.kitpvpcore.item.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class KitCoreItems {

    public enum AbilityItems {

        JESTER_STAFF(new ItemBuilder<>(Material.DIAMOND_HOE)
                .setDisplayName(ChatColor.YELLOW + "Jester Staff")
                .build()),

        SCREECHER_TOOL(new ItemBuilder<>(Material.GHAST_TEAR)
                .setDisplayName(ChatColor.DARK_GREEN + "Screecher's Screech")
                .build()),

        NINJA_DUST(new ItemBuilder<>(Material.REDSTONE)
                .setDisplayName(ChatColor.RED + "Invisibility Dust")
                .build()),

        MAGE_TOOL(new ItemBuilder<>(Material.ORANGE_WOOL)
                .setDisplayName(ChatColor.DARK_GREEN + "Magic Shield")
                .build()),

        SUPERMAN_FEATHER(new ItemBuilder<>(Material.FEATHER)
                .setDisplayName(ChatColor.DARK_GREEN + "Flying Feather")
                .build());

        private final ItemStack item;

        AbilityItems(ItemStack item) {
            this.item =  item;
        }

        public ItemStack getItem() {
            return item;
        }
    }

    public enum ComplexItems {

        SOUP(new ItemBuilder<>(Material.MUSHROOM_STEW)
                .setDisplayName(ChatColor.GRAY + "Soup")
                .build()),

        KIT_CHEST(new ItemBuilder<>(Material.CHEST)
                .setDisplayName(ChatColor.LIGHT_PURPLE + "Kit Selector")
                .build());

        private final ItemStack item;

        ComplexItems(ItemStack item) {
            this.item = item;
        }

        public ItemStack getItem() {
            return item;
        }
    }

    public enum GUIStandardItems {

        FORWARD_ARROW(new ItemBuilder<>(Material.GREEN_STAINED_GLASS_PANE)
                .setDisplayName(ChatColor.GREEN + "NEXT PAGE")
                .build()),

        BACKWARD_ARROW(new ItemBuilder<>(Material.GREEN_STAINED_GLASS_PANE)
                .setDisplayName(ChatColor.RED + "PREVIOUS PAGE")
                .build()),

        BORDER(new ItemBuilder<>(Material.GREEN_STAINED_GLASS_PANE)
                .setDisplayName("")
                .build());

        private final ItemStack item;

        GUIStandardItems(ItemStack item) {
            this.item =  item;
        }

        public ItemStack getItem() {
            return item;
        }
    }
}
