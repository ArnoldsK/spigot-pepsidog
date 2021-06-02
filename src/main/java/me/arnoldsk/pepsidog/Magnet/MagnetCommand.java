package me.arnoldsk.pepsidog.Magnet;

import me.arnoldsk.pepsidog.CommandBase;
import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MagnetCommand extends CommandBase {
    PepsiDog plugin;

    public MagnetCommand(PepsiDog plugin) {
        super(plugin);

        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (player.isOp() && args.length > 0 && args[0].equalsIgnoreCase("give")) {
            ItemStack item = new MagnetItemData(plugin).getItem();

            player.getInventory().addItem(item);
            return true;
        }

        player.sendMessage("Get items from afar. Craft it with one diamond in the middle, 4 redstone blocks at the top and to the right, and 4 lapis blocks at the bottom and to the left.");

        return true;
    }
}
