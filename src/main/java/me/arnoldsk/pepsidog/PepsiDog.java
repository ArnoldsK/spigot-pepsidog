package me.arnoldsk.pepsidog;

import me.arnoldsk.pepsidog.CustomDrops.CustomDropsListener;
import me.arnoldsk.pepsidog.NameColors.NameColorsListener;
import me.arnoldsk.pepsidog.OpStick.OpStickCommand;
import me.arnoldsk.pepsidog.OpStick.OpStickListener;
import me.arnoldsk.pepsidog.SoftSpawnProtection.SoftSpawnProtectionListener;
import me.arnoldsk.pepsidog.TalismanOfLight.TalismanOfLightCommand;
import me.arnoldsk.pepsidog.TalismanOfLight.TalismanOfLightItemData;
import me.arnoldsk.pepsidog.TalismanOfLight.TalismanOfLightListener;
import me.arnoldsk.pepsidog.WhereIs.WhereIsCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class PepsiDog extends JavaPlugin {
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Init configuration
        config.addDefault("enabled", true);
        config.addDefault("softSpawnProtection", 50);
        config.options().copyDefaults(true);
        saveConfig();

        // Register events
        pluginManager.registerEvents(new NameColorsListener(this), this);
        pluginManager.registerEvents(new SoftSpawnProtectionListener(this), this);
        pluginManager.registerEvents(new CustomDropsListener(this), this);
        pluginManager.registerEvents(new OpStickListener(this), this);
        pluginManager.registerEvents(new TalismanOfLightListener(this), this);

        // Add recipes
        getServer().addRecipe(new TalismanOfLightItemData(this).getRecipe());

        // Alert
        System.out.println("Pepsi Dog plugin is enabled");
    }

    @Override
    public void onDisable() {
        // Alert
        System.out.println("Pepsi Dog plugin is disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        try {
            switch (command.getName().toLowerCase()) {
                case "opstick":
                    return new OpStickCommand(this).run(sender, command, label, args);

                case "whereis":
                    return new WhereIsCommand(this).run(sender, command, label, args);

                case "talismanoflight":
                    return new TalismanOfLightCommand(this).run(sender, command, label, args);
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.DARK_RED + "Error: command failed");
            getLogger().log(Level.SEVERE, e.getMessage());
        }

        return false;
    }
}
