package me.arnoldsk.pepsidog.Magnet;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MagnetItemData {
    PepsiDog plugin;

    String itemKey = "magnet";
    String name = ChatColor.AQUA + "Magnet";
    Material material = Material.HEART_OF_THE_SEA;

    ItemStack item;

    public MagnetItemData(PepsiDog plugin) {
        this.plugin = plugin;

        // Create a new custom item
        item = new ItemStack(material);

        // Adjust metadata
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            // Name
            meta.setDisplayName(name);

            // Lore
            meta.setLore(getLore());

            // Flags - Hide everything
            meta.addItemFlags(
                    ItemFlag.HIDE_ENCHANTS,
                    ItemFlag.HIDE_ATTRIBUTES,
                    ItemFlag.HIDE_UNBREAKABLE,
                    ItemFlag.HIDE_DESTROYS,
                    ItemFlag.HIDE_PLACED_ON,
                    ItemFlag.HIDE_POTION_EFFECTS
            );

            // Glint
            meta.addEnchant(Enchantment.VANISHING_CURSE, 1, true);

            item.setItemMeta(meta);
        }
    }

    public static ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();

        lore.add("Imagine walking to pick up items...");
        lore.add(ChatColor.DARK_GRAY + "Right click to toggle state");

        return lore;
    }

    public static boolean equalsLore(List<String> lore) {
        ArrayList<String> magnetLore = getLore();

        // Simple check first
        if (magnetLore.size() != lore.size()) return false;

        // Compare each line
        // Using ArrayList.equals fails for some reason, most likely due to custom color
        for (int i = 0; i < magnetLore.size(); i++) {
            String a = magnetLore.get(i);
            String b = lore.get(i);

            if (!a.equals(b)) {
                return false;
            }
        }

        return true;
    }

    public ItemStack getItem() {
        return item;
    }

    public Recipe getRecipe() {
        NamespacedKey key = new NamespacedKey(plugin, itemKey);
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        // Set the recipe shape
        recipe.shape(
                "RRR",
                "LDR",
                "LLL"
        );

        // Map the shape items
        recipe.setIngredient('L', Material.LAPIS_BLOCK);
        recipe.setIngredient('R', Material.REDSTONE_BLOCK);
        recipe.setIngredient('D', Material.DIAMOND);

        return recipe;
    }
}
