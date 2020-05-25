package me.arnoldsk.pepsidog;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandBase implements CommandInterface {
    PepsiDog plugin;

    public CommandBase(PepsiDog plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean run(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}