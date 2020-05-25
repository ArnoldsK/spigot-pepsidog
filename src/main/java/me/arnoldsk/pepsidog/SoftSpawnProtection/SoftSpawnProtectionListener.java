package me.arnoldsk.pepsidog.SoftSpawnProtection;

import me.arnoldsk.pepsidog.Helpers;
import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class SoftSpawnProtectionListener implements org.bukkit.event.Listener {
    PepsiDog plugin;

    int softSpawnProtection = 0;

    public SoftSpawnProtectionListener(PepsiDog plugin) {
        this.plugin = plugin;

        // Get config that will be reused
        softSpawnProtection = plugin.getConfig().getInt("softSpawnProtection");
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Block block = event.getBlock();
        Location location = block.getLocation();
        World world = location.getWorld();

        // Ignore if there is no soft spawn protection
        if (softSpawnProtection <= 0) {
            return;
        }

        // Ignore if not normal World
        assert world != null;
        if (world.getEnvironment() != World.Environment.NORMAL) {
            return;
        }

        Location spawnLocation = world.getSpawnLocation();
        boolean isBurnInSoftSpawn = Helpers.isLocationInSquareRadius(location, softSpawnProtection, spawnLocation);

        // Ignore if not in soft spawn protection
        if (!isBurnInSoftSpawn) {
            return;
        }

        // Cancel the event
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Location location = event.getLocation();
        World world = location.getWorld();

        // Ignore if there is no soft spawn protection
        if (softSpawnProtection <= 0) {
            return;
        }

        // Ignore if not normal World
        assert world != null;
        if (world.getEnvironment() != World.Environment.NORMAL) {
            return;
        }

        Location spawnLocation = world.getSpawnLocation();
        boolean isBedInSoftSpawn = Helpers.isLocationInSquareRadius(location, softSpawnProtection, spawnLocation);

        // Ignore if not in soft spawn protection
        if (!isBedInSoftSpawn) {
            return;
        }

        // Cancel the event
        event.setCancelled(true);
    }
}
