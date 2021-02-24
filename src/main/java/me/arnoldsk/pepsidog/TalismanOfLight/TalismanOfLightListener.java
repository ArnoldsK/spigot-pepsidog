package me.arnoldsk.pepsidog.TalismanOfLight;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TalismanOfLightListener implements Listener {
    PepsiDog plugin;

    public TalismanOfLightListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void craftItem(PrepareItemCraftEvent event) {
        AtomicBoolean canCraft = new AtomicBoolean(true);

        event.getInventory().forEach(item -> {
            // Do not check blank spots
            if (item == null || item.getItemMeta() == null) return;

            // Do not check the result
            // This will prevent the item to be created
            if (event.getRecipe().getResult().equals(item)) return;

            // Check for lore
            if (!item.getItemMeta().hasLore()) return;

            // Require the specific lore
            List<String> lore = item.getItemMeta().getLore();

            // Compare lore
            if (TalismanOfLightItemData.getLore().equals(lore)) return;

            // Item is not allowed
            canCraft.set(false);
        });

        // Disallow crafting
        if (!canCraft.get()) {
            event.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onServerListPing(ServerLoadEvent event) {
        // 20 ticks ~ 1 second
        long delayMs = 60;

        this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() {
                checkPlayerInventories();
            }
        }, delayMs, delayMs);
    }

    private void checkPlayerInventories() {
        Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();

        players.forEach(player -> {
            player.getInventory().forEach(itemStack -> {
                // Check if player has the item
                if (itemStack == null) return;
                if (!itemStack.getType().equals(Material.NETHER_STAR)) return;

                // Apply night vision
                playerAddNightVision(player);
            });
        });
    }

    public void playerAddNightVision(Player player) {
        // 20 ticks ~ 1 second
        int duration = 300;

        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, duration, 0));
    }
}
