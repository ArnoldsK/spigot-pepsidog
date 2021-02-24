package me.arnoldsk.pepsidog.TalismanOfLight;

import me.arnoldsk.pepsidog.CommandBase;
import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TalismanOfLightCommand extends CommandBase {
    PepsiDog plugin;

    public TalismanOfLightCommand(PepsiDog plugin) {
        super(plugin);

        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player player = (Player) sender;

        player.sendMessage("See in the dark. Teleport home. Craft it with 4 iron ingots in corners, 4 golden carrots on each side and a diamond in the middle.");

        return true;
    }
}
