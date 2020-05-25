package me.arnoldsk.pepsidog.NameColors;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class NameColorsListener implements org.bukkit.event.Listener {
    PepsiDog plugin;

    public NameColorsListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        event.setFormat(ChatColor.GOLD + "%1$s" + ChatColor.WHITE + ": %2$s");
    }
}
