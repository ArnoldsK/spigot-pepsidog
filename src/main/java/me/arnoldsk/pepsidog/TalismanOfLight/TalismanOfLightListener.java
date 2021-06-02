package me.arnoldsk.pepsidog.TalismanOfLight;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;

public class TalismanOfLightListener implements Listener {
    PepsiDog plugin;

    String offStateLabel = " (off)";

    public TalismanOfLightListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    /**
     * Handle toggling enabled state
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Check for right click
        if (event.getAction() != Action.RIGHT_CLICK_AIR) return;

        // Get held item
        ItemStack item = event.getItem();

        // Check if the right clicked item is the talisman
        if (item == null || !isItemTalisman(item)) return;

        // Require player to be crouched
        Player player = event.getPlayer();

        if (!player.isSneaking()) return;

        // Get the current talisman state
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return;

        String displayName = itemMeta.getDisplayName();
        boolean isDisabled = isTalismanDisabled(item);

        // Adjust name based on the new state
        if (isDisabled) {
            displayName = displayName.replace(offStateLabel, "");
        } else {
            displayName += offStateLabel;
        }

        // Set the new name
        itemMeta.setDisplayName(displayName);
        item.setItemMeta(itemMeta);

        // Send the new state
        player.sendMessage("Talisman of Light is now " + (isDisabled ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
    }

    /**
     * Disallow crafting with the item
     */
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        ItemStack[] inventoryContents = event.getInventory().getContents();

        boolean canCraft = true;

        for (ItemStack item : inventoryContents) {
            // Do not check blank spots
            if (item == null || item.getItemMeta() == null) continue;

            // Do not check the result
            // This will prevent the item to be created
            if (event.getRecipe() == null || event.getRecipe().getResult().equals(item)) continue;

            // Check for lore
            if (!item.getItemMeta().hasLore()) continue;

            // Require the specific lore
            List<String> lore = item.getItemMeta().getLore();

            // Compare lore
            if (lore == null || !TalismanOfLightItemData.equalsLore(lore)) continue;

            // Disallow using the talisman for crafting
            canCraft = false;
        }

        if (canCraft) return;

        // Disallow crafting
        event.getInventory().setResult(new ItemStack(Material.AIR));
    }

    /**
     * Check inventories for the item
     */
    @EventHandler
    public void onServerListPing(ServerLoadEvent event) {
        // 20 ticks ~ 1 second
        long delayMs = 60;

        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::checkPlayerInventories, delayMs, delayMs);
    }

    boolean isItemTalisman(ItemStack item) {
        // Require a nether star
        if (item == null || !item.getType().equals(Material.NETHER_STAR)) return false;

        // Require lore
        if (item.getItemMeta() == null || item.getItemMeta().getLore() == null) return false;

        // Compare lore
        List<String> lore = item.getItemMeta().getLore();

        return TalismanOfLightItemData.getLore().equals(lore);
    }

    boolean isTalismanDisabled(ItemStack item) {
        // Get the current talisman state
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta == null) return true;

        String displayName = itemMeta.getDisplayName();

        return displayName.endsWith(offStateLabel);
    }

    ItemStack inventoryGetTalisman(PlayerInventory inventory) {
        for (ItemStack item : inventory) {
            if (isItemTalisman(item)) {
                return item;
            }
        }

        return null;
    }

    void checkPlayerInventories() {
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

        players.forEach(player -> {
            ItemStack item = inventoryGetTalisman(player.getInventory());

            // Item exists and not disabled
            if (item == null || isTalismanDisabled(item)) return;

            // Apply night vision
            playerAddNightVision(player);
        });
    }

    void playerAddNightVision(Player player) {
        // 20 ticks ~ 1 second
        int duration = 300;

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, duration, 0));
    }
}
