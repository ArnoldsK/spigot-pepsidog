package me.arnoldsk.pepsidog.PlayerBeds;

import me.arnoldsk.pepsidog.Helpers;
import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Bed;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;

public class PlayerBedsListener implements org.bukkit.event.Listener {
    PepsiDog plugin;

    public PlayerBedsListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    Block getBedHeadBlock(Block anyBedBlock) {
        Bed blockData = (Bed) anyBedBlock.getBlockData();

        if (blockData.getPart() != Bed.Part.HEAD) {
            // Set the other block as the returned block
            anyBedBlock = Helpers.getBedOtherBlock(anyBedBlock.getLocation(), blockData);
        }

        return anyBedBlock;
    }

    Block getBedFootBlock(Block anyBedBlock) {
        Bed blockData = (Bed) anyBedBlock.getBlockData();

        if (blockData.getPart() != Bed.Part.FOOT) {
            // Set the other block as the returned block
            anyBedBlock = Helpers.getBedOtherBlock(anyBedBlock.getLocation(), blockData);
        }

        return anyBedBlock;
    }

    String getBedOwner(Block bedHeadBlock) {
        return getBedOwner(bedHeadBlock, false);
    }

    String getBedOwner(Block bedHeadBlock, Boolean returnUID) {
        String blockHashCode = String.valueOf(bedHeadBlock.hashCode());

        FileConfiguration config = plugin.getPlayerBedsConfig();
        Set<String> playerUIDs = config.getKeys(true);

        for (String playerUID : playerUIDs) {
            String name = config.getString(playerUID + ".name");
            String hashCode = config.getString(playerUID + ".hash");

            if (blockHashCode.equals(hashCode)) {
                return returnUID ? playerUID : name;
            }
        }

        return "";
    }

    boolean canPlayerModifyBed(Player player, Block bedHeadBlock) {
        String bedOwner = getBedOwner(bedHeadBlock, true);

        return bedOwner.isEmpty() || bedOwner.equals(player.getUniqueId().toString());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Block block = event.getClickedBlock();

        // Throws null if block is destroyed
        if (block == null) {
            return;
        }

        // Require the block to be a bed
        if (!(block.getBlockData() instanceof Bed)) {
            return;
        }

        Block bedHeadBlock = getBedHeadBlock(block);
        String bedOwner = getBedOwner(bedHeadBlock);

        // Cancel the event by default
        event.setCancelled(true);

        // Disallow interacting with owned beds
        if (!canPlayerModifyBed(player, bedHeadBlock)) {
            // Allow ops to break beds
            if (player.isOp() && action == Action.LEFT_CLICK_BLOCK) {
                player.sendMessage(ChatColor.BLUE + "You broke " + bedOwner + "'s bed");
                event.setCancelled(false);
                return;
            }

            player.sendMessage(ChatColor.RED + "This is " + bedOwner + "'s bed");
            return;
        }

        // Handle left click on an owned bed
        if (action == Action.LEFT_CLICK_BLOCK) {
            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "You can't destroy beds");
            } else {
                // Allow ops to break beds
                event.setCancelled(false);
            }
            return;
        }

        // Handle only right click
        if (action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        // Check if the bed is outside of the soft spawn protection
        int softSpawnProtection = plugin.getConfig().getInt("softSpawnProtection");

        if (softSpawnProtection > 0) {
            Location spawnLocation = block.getWorld().getSpawnLocation();
            boolean isBedInSoftSpawn = Helpers.isLocationInSquareRadius(block.getLocation(), softSpawnProtection, spawnLocation);

            if (!isBedInSoftSpawn) {
                // Alert
                player.sendMessage(ChatColor.RED + "You can't set respawn point at this location");
                return;
            }
        }

        // Check for sign updates
        World world = block.getWorld();
        Block blockAboveBedHead = world.getBlockAt(bedHeadBlock.getX(), bedHeadBlock.getY() + 1, bedHeadBlock.getZ());
        Sign sign = null;

        if (blockAboveBedHead.getState() instanceof Sign) {
            sign = (Sign) blockAboveBedHead.getState();
        } else {
            Block bedFootBlock = getBedFootBlock(block);
            Block blockAboveBedFoot = world.getBlockAt(bedFootBlock.getX(), bedFootBlock.getY() + 1, bedFootBlock.getZ());

            if (blockAboveBedFoot.getState() instanceof Sign) {
                sign = (Sign) blockAboveBedFoot.getState();
            }
        }

        if (sign != null) {
            // Update the sign
            updateSignOwner(sign, player.getName());
        }

        // Assign the bed to the player
        String configKey = player.getUniqueId().toString();

        plugin.getPlayerBedsConfig().set(configKey + ".name", player.getName());
        plugin.getPlayerBedsConfig().set(configKey + ".hash", bedHeadBlock.hashCode());
        plugin.savePlayerBedsConfig();

        // Allow the event
        event.setCancelled(false);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        // Bypass ops
        if (player.isOp()) {
            return;
        }

        // Require the block to be a bed
        if (!(block.getBlockData() instanceof Bed)) {
            return;
        }

        // Disallow placing beds
        event.setCancelled(true);

        // Alert
        player.sendMessage(ChatColor.RED + "You can't place beds");
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Block block = event.getBlock();

        // Check if the sign is above a bed
        Block blockBelow = block.getWorld().getBlockAt(block.getX(), block.getY() - 1, block.getZ());

        if (!(blockBelow.getBlockData() instanceof Bed)) {
            return;
        }

        Block bedHeadBlock = getBedHeadBlock(blockBelow);
        String bedOwner = getBedOwner(bedHeadBlock);

        // Bed has no owner
        if (bedOwner.isEmpty()) {
            return;
        }

        updateSignOwner((Sign) event, bedOwner);
    }

    void updateSignOwner(Sign sign, String bedOwner) {
        for (int i = 0; i < 4; i++) {
            String line = "";

            switch (i) {
                case 1:
                    line = bedOwner;
                    break;

                case 2:
                    line = "sleeps here";
                    break;
            }

            sign.setLine(i, line);
        }

        sign.update();
    }
}
