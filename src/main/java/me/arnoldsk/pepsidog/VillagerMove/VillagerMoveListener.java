package me.arnoldsk.pepsidog.VillagerMove;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.jetbrains.annotations.NotNull;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VillagerMoveListener implements Listener {
    PepsiDog plugin;

    public VillagerMoveListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        @NotNull Player player = event.getPlayer();

        // Must be sneaking
        if (!player.isSneaking()) return;

        // Get villager
        @NotNull Entity villagerEntity = event.getRightClicked();
        if (villagerEntity.getType() != EntityType.VILLAGER) return;

        // Get the book
        @NotNull ItemStack itemInHand = player.getInventory().getItemInMainHand();
        if (itemInHand.getType() != Material.WRITTEN_BOOK) return;

        // Get book contents
        BookMeta bookMeta = (BookMeta) itemInHand.getItemMeta();
        assert bookMeta != null;
        String firstLine = bookMeta.getPage(1).split("\\r?\\n")[0];

        // Get coordinates
        Pattern pattern = Pattern.compile("^(-?\\d+)\\s*?/\\sv?(-?\\d+)\\s*?/\\s*?(-?\\d+)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(firstLine);
        if (!matcher.matches()) return;

        int x = Integer.parseInt(matcher.group(1));
        int y = Integer.parseInt(matcher.group(2));
        int z = Integer.parseInt(matcher.group(3));

        // Prevent default
        event.setCancelled(true);

        // Teleport the villager
        villagerEntity.teleport(new Location(player.getWorld(), x, y + 1, z));

        // Remove the book
        itemInHand.setAmount(0);
        player.updateInventory();
    }
}
