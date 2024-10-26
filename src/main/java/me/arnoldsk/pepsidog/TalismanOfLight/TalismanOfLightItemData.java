package me.arnoldsk.pepsidog.TalismanOfLight;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TalismanOfLightItemData {
    PepsiDog plugin;

    String itemKey = "talisman_of_light";
    String name = ChatColor.AQUA + "Talisman of Light";
    Material material = Material.NETHER_STAR;

    ItemStack item;

    public TalismanOfLightItemData(PepsiDog plugin) {
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

            item.setItemMeta(meta);
        }
    }

    public static ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();

        lore.add("There are darknesses in life and there are lights,");
        lore.add("and you are one of the lights, the light of all lights");
        lore.add(ChatColor.DARK_GRAY + "Right click to toggle state");

        return lore;
    }

    public static boolean equalsLore(List<String> lore) {
        ArrayList<String> talismanLore = getLore();

        // Simple check first
        if (talismanLore.size() != lore.size()) return false;

        // Compare each line
        // Using ArrayList.equals fails for some reason, most likely due to custom color
        for (int i = 0; i < talismanLore.size(); i++) {
            String a = talismanLore.get(i);
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
                "IGI",
                "GDG",
                "IGI"
        );

        // Map the shape items
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('G', Material.GOLDEN_CARROT);
        recipe.setIngredient('D', Material.DIAMOND);

        return recipe;
    }
}
