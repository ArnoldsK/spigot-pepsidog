package me.arnoldsk.pepsidog.CustomDrops;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomDropsListener implements org.bukkit.event.Listener {
    PepsiDog plugin;

    public CustomDropsListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        double finalDamage = event.getFinalDamage();

        // Require the damager to be a player
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();

        // Give an Elytra on dragon kill
        if (entity instanceof EnderDragon) {
            EnderDragon dragon = (EnderDragon) entity;
            double finalHealth = dragon.getHealth() - finalDamage;

            if (finalHealth <= 0) {
                ItemStack elytra = new ItemStack(Material.ELYTRA);
                ItemMeta meta = elytra.getItemMeta();

                // Give the Elytra a custom name
                if (meta != null) {
                    meta.setDisplayName("Ender Dragon Elytra");
                    elytra.setItemMeta(meta);
                }

                // Give the item
                player.getInventory().addItem(elytra);

                // Alert
                plugin.getServer().broadcastMessage(ChatColor.GREEN + player.getName() + " has received Elytra from the Ender Dragon");
            }
        }
    }
}
