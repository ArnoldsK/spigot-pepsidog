package me.arnoldsk.pepsidog.CustomDrops;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomDropsListener implements org.bukkit.event.Listener {
    PepsiDog plugin;

    public CustomDropsListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        // Has to be a dragon
        if (!(entity instanceof EnderDragon)) return;

        EnderDragon dragon = (EnderDragon) entity;

        // Has to be killed by a player
        Player player = dragon.getKiller();

        if (player == null) return;

        playerAwardElytra(player);
    }

        void playerAwardElytra(Player player) {
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
