package me.arnoldsk.pepsidog.WhereIs;

import me.arnoldsk.pepsidog.CommandBase;
import me.arnoldsk.pepsidog.PepsiDog;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static org.bukkit.Bukkit.getServer;

public class WhereIsCommand extends CommandBase {
    public WhereIsCommand(PepsiDog plugin) {
        super(plugin);
    }

    @Override
    public boolean run(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Target player is required");
            return false;
        }

        // Find target player
        String targetPlayerName = args[0];
        AtomicReference<Player> targetPlayerRef = new AtomicReference<>();

        Collection<? extends Player> onlinePlayers = getServer().getOnlinePlayers();

        onlinePlayers.forEach((Player onlinePlayer) -> {
            if (!onlinePlayer.getDisplayName().equalsIgnoreCase(targetPlayerName)) return;

            targetPlayerRef.set(onlinePlayer);
        });

        // Validate player
        if (targetPlayerRef == null) {
            player.sendMessage(ChatColor.RED + "Target player is not found");
            return false;
        }

        Player targetPlayer = targetPlayerRef.get();
        Location location = targetPlayer.getLocation();

        player.sendMessage(
                ChatColor.GREEN + targetPlayer.getDisplayName() +
                        ChatColor.WHITE + " is in " +
                        ChatColor.GREEN + targetPlayer.getWorld().getName() +
                        ChatColor.WHITE + " at " +
                        ChatColor.GREEN + location.getBlockX() + " / " + location.getBlockY() + " / " + location.getBlockZ()
        );

        return true;
    }
}