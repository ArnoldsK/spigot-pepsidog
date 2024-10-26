package me.arnoldsk.pepsidog.CustomDrops;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Material;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
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

        // Drop the Elytra
        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta meta = elytra.getItemMeta();
        assert meta != null;
        meta.setDisplayName("Ender Dragon Elytra");
        elytra.setItemMeta(meta);
        dragon.getWorld().dropItem(dragon.getLocation(), elytra);
    }
}
