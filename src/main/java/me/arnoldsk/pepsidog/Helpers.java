package me.arnoldsk.pepsidog;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;

public class Helpers {
    public static boolean isLocationInSquareRadius(Location location, int radius, Location sourceLocation) {
        int x = location.getBlockX();
        int z = location.getBlockZ();
        int x1 = sourceLocation.getBlockX() - radius;
        int z1 = sourceLocation.getBlockZ() - radius;
        int x2 = sourceLocation.getBlockX() + radius;
        int z2 = sourceLocation.getBlockZ() + radius;

        return x >= x1 && x <= x2 && z >= z1 && z <= z2;
    }

    public static Block getBedOtherBlock(Location location, Bed blockData) {
        boolean isPartHead = blockData.getPart() == Bed.Part.HEAD;

        int blockX = location.getBlockX();
        int blockZ = location.getBlockZ();

        switch (blockData.getFacing()) {
            case EAST:
                blockX += isPartHead ? -1 : 1;
                break;

            case WEST:
                blockX += isPartHead ? 1 : -1;
                break;

            case SOUTH:
                blockZ += isPartHead ? -1 : 1;
                break;

            case NORTH:
                blockZ += isPartHead ? 1 : -1;
                break;
        }

        return location.getWorld().getBlockAt(blockX, location.getBlockY(), blockZ);
    }
}
