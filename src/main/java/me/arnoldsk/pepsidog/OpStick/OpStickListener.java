package me.arnoldsk.pepsidog.OpStick;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpStickListener implements Listener {
    PepsiDog plugin;

    public OpStickListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        // Player is OP
        if (!player.isOp()) {
            return;
        }

        // Item is required
        if (item == null) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        // Only if there is metadata
        if (meta == null) {
            return;
        }

        // Only if it's Op Stick
        if (!meta.getDisplayName().equals("Op Stick")) {
            return;
        }

        // Handle only left clicks
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        // Require a block
        if (block == null) {
            return;
        }

        // Destroy the block with a made up looting pick-axe
        ItemStack breakWithItem = new ItemStack(Material.DIAMOND_PICKAXE);

        breakWithItem.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);

        block.breakNaturally(breakWithItem);
    }
}
