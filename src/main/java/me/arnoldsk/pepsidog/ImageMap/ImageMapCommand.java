package me.arnoldsk.pepsidog.ImageMap;

import me.arnoldsk.pepsidog.CommandBase;
import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import java.util.logging.Level;

public class ImageMapCommand extends CommandBase {
    PepsiDog plugin;

    public ImageMapCommand(PepsiDog plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length > 0) {
            this.handle(player, args[0]);

            return true;
        }

        player.sendMessage("Provide the image URL to get a map of the image. Preferably a square.");

        return true;
    }

    void handle(Player player, String url) {
        Inventory inventory = player.getInventory();

        boolean isCreative = player.getGameMode() == GameMode.CREATIVE;
        boolean hasEmptyMap = this.getHasEmptyMap(inventory);

        // Require an empty map
        if (!isCreative && !hasEmptyMap) {
            player.sendMessage(ChatColor.RED + "You need an empty map in your inventory to get an image map.");
            return;
        }

        try {
            // Create the image map
            ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
            MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
            assert mapMeta != null;

            MapView mapView = Bukkit.createMap(player.getWorld());

            ImageMapRenderer mapRenderer = new ImageMapRenderer(url);
            mapView.getRenderers().clear();
            mapView.addRenderer(mapRenderer);

            mapMeta.setMapView(mapView);
            mapItem.setItemMeta(mapMeta);

            player.getInventory().addItem(mapItem);

            // Save the map and url
            plugin.getMapImagesConfig().set(String.valueOf(mapView.getId()), url);
            plugin.saveMapImagesConfig();

            // Remove one empty map
            if (!isCreative) {
                this.removeEmptyMap(inventory);
                player.updateInventory();
            }
        } catch (Exception ex) {
            player.sendMessage(ChatColor.RED + "Unable to get the image URL");
            plugin.getLogger().log(Level.WARNING, ex.getMessage());
        }
    }

    boolean getHasEmptyMap(Inventory inventory) {
        try {
            return inventory.contains(Material.MAP);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    void removeEmptyMap(Inventory inventory) {
        try {
            int slot = inventory.first(Material.MAP);
            ItemStack item = inventory.getItem(slot);
            assert item != null;

            item.setAmount(item.getAmount() - 1);
            inventory.setItem(slot, item);
        } catch (IllegalArgumentException ex) {
            // Do nothing
        }
    }
}
