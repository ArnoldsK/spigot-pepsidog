package me.arnoldsk.pepsidog.OpStick;

import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpStickListener implements Listener {
    PepsiDog plugin;

    public OpStickListener(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        // TODO: destroy the OP stick
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (!(damager instanceof Player)) {
            return;
        }

        // Cast to player
        Player player = (Player) damager;

        // Require OP
        if (!player.isOp()) {
            return;
        }

        // Get main hand item
        ItemStack item = player.getInventory().getItemInMainHand();

        // Only if it's Op Stick
        if (isNotOpStick(item)) {
            return;
        }

        // Obliterate the damn thing
        if (!event.getEntity().isDead()) {
            event.getEntity().remove();
        }
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

        // Only if it's Op Stick
        if (isNotOpStick(item)) {
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

    boolean isNotOpStick(ItemStack item) {
        ItemMeta meta = item.getItemMeta();

        return item.getType() != Material.DEBUG_STICK || meta == null || !meta.getDisplayName().equals("Op Stick");
    }
}
