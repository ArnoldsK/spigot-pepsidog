package me.arnoldsk.pepsidog.CustomSkulls;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;

public class CustomSkullsListener implements Listener {
    PepsiDog plugin;
    
    public CustomSkullsListener(PepsiDog plugin) {
        this.plugin = plugin;
    }
    
    public ArrayList<Material> getSkullMaterials() {
        ArrayList<Material> materials = new ArrayList<>();
        
        materials.add(Material.SKELETON_SKULL);
        materials.add(Material.WITHER_SKELETON_SKULL);
        materials.add(Material.CREEPER_HEAD);
        materials.add(Material.ZOMBIE_HEAD);
        materials.add(Material.PLAYER_HEAD);
        materials.add(Material.DRAGON_HEAD);
        
        return materials;
    }
    
    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvilInventory = event.getInventory();
        ArrayList<Material> materials = getSkullMaterials();
        
        // Get items
        ItemStack baseItem = anvilInventory.getItem(0);
        ItemStack resultItem = event.getResult();

        // Require an item and a valid result
        if (baseItem == null || resultItem == null) return;
        
        // The base item has to be a skull
        if (!materials.contains(baseItem.getType())) return;
        
        // Check for custom name
        String renameText = anvilInventory.getRenameText();

        // Technically 2 and 3 char names exist but it's not worth it
        if (renameText == null || renameText.length() < 4) return;

        // Find the player
        OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(renameText);

        // Create the player skull
        ItemStack playerSkull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta playerSkullMeta = (SkullMeta) playerSkull.getItemMeta();

        if (playerSkullMeta == null) return;

        // Set the skull owner
        boolean isSkullOwnerSet = playerSkullMeta.setOwningPlayer(offlinePlayer);

        if (!isSkullOwnerSet) return;

        playerSkull.setItemMeta(playerSkullMeta);

        // Set the skull as the result
        event.setResult(playerSkull);
    }
}
