package me.arnoldsk.pepsidog.ImageMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;

public class ImageMapRenderer extends MapRenderer {
    private final SoftReference<BufferedImage> cacheImage;
    private boolean hasRendered = false;

    public ImageMapRenderer(String url) throws IOException {
        this.cacheImage = new SoftReference<>(this.getImage(url));
    }

    @Override
    public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {
        if (this.hasRendered) return;

        BufferedImage image = this.cacheImage.get();
        if (image != null) {
            canvas.drawImage(0, 0, image);
        } else {
            player.sendMessage(ChatColor.RED + "Attempted to render the image, but the cached image was null!");
        }

        this.hasRendered = true;
    }

    BufferedImage getImage(String url) throws IOException {
        boolean useCache = ImageIO.getUseCache();

        // Temporarily disable cache, if it isn't already,
        // so we can get the latest image.
        ImageIO.setUseCache(false);

        InputStream stream = new URL(url).openStream();
        BufferedImage image = ImageIO.read(stream);
        stream.close();

        // Re-enable it with the old value.
        ImageIO.setUseCache(useCache);

        return MapPalette.resizeImage(image);
    }
}
