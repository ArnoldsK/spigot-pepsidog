package me.arnoldsk.pepsidog.ImageMap;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.map.MapView;

import java.io.IOException;
import java.util.Map;

public class ImageMapListener implements Listener {
    PepsiDog plugin;

    public ImageMapListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        FileConfiguration mapImagesConfig = plugin.getMapImagesConfig();
        Map<String, Object> mapIdUrls = mapImagesConfig.getValues(false);

        mapIdUrls.forEach((mapId, url) -> {
            MapView mapView = plugin.getServer().getMap(Integer.parseInt(mapId));
            if (mapView == null) return;

            try {
                ImageMapRenderer mapRenderer = new ImageMapRenderer((String) url);
                mapView.getRenderers().clear();
                mapView.addRenderer(mapRenderer);
            } catch (IOException exception) {
                // Do nothing
            }
        });
    }
}
