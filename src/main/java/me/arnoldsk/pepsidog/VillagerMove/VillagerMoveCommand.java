package me.arnoldsk.pepsidog.VillagerMove;

import me.arnoldsk.pepsidog.CommandBase;
import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VillagerMoveCommand extends CommandBase {
    PepsiDog plugin;

    public VillagerMoveCommand(PepsiDog plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player player = (Player) sender;

        player.sendMessage("Sneak right click a villager with a signed book containing exact move coordinates. For example \"-181 / 102 / -173\".");

        return true;
    }
}
