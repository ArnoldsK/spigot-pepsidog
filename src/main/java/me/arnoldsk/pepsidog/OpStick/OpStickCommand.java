package me.arnoldsk.pepsidog.OpStick;

import me.arnoldsk.pepsidog.CommandBase;
import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class OpStickCommand extends CommandBase {
    public OpStickCommand(PepsiDog plugin) {
        super(plugin);
    }

    @Override
    public boolean run(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "This is a server operator command");
        } else {
            // Create a custom stick
            ItemStack stick = new ItemStack(Material.STICK);
            ItemMeta meta = stick.getItemMeta();

            // Rename the item
            if (meta != null) {
                meta.setDisplayName("Op Stick");
                stick.setItemMeta(meta);
            }

            // Add enchantments
            stick.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9000);
            stick.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
            stick.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 3);

            player.getInventory().addItem(stick);
        }

        return true;
    }
}
