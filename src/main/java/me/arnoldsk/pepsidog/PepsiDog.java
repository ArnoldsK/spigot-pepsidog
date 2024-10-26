package me.arnoldsk.pepsidog;

import me.arnoldsk.pepsidog.CustomDrops.CustomDropsListener;
import me.arnoldsk.pepsidog.CustomSkulls.CustomSkullsListener;
import me.arnoldsk.pepsidog.ImageMap.ImageMapCommand;
import me.arnoldsk.pepsidog.ImageMap.ImageMapListener;
import me.arnoldsk.pepsidog.Magnet.MagnetCommand;
import me.arnoldsk.pepsidog.Magnet.MagnetItemData;
import me.arnoldsk.pepsidog.Magnet.MagnetListener;
import me.arnoldsk.pepsidog.NameColors.NameColorsListener;
import me.arnoldsk.pepsidog.OpStick.OpStickCommand;
import me.arnoldsk.pepsidog.OpStick.OpStickListener;
import me.arnoldsk.pepsidog.SoftSpawnProtection.SoftSpawnProtectionListener;
import me.arnoldsk.pepsidog.TalismanOfLight.TalismanOfLightCommand;
import me.arnoldsk.pepsidog.TalismanOfLight.TalismanOfLightItemData;
import me.arnoldsk.pepsidog.TalismanOfLight.TalismanOfLightListener;
import me.arnoldsk.pepsidog.WhereIs.WhereIsCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class PepsiDog extends JavaPlugin {
    private FileConfiguration config = getConfig();

    private File mapImagesConfigFile;
    private FileConfiguration mapImagesConfig;

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Init configuration
        config.addDefault("enabled", true);
        config.addDefault("softSpawnProtection", 50);
        config.options().copyDefaults(true);
        saveConfig();

        // Custom config
        createMapImagesConfig();

        // Register events
        pluginManager.registerEvents(new NameColorsListener(this), this);
        pluginManager.registerEvents(new SoftSpawnProtectionListener(this), this);
        pluginManager.registerEvents(new CustomDropsListener(this), this);
        pluginManager.registerEvents(new OpStickListener(this), this);
        pluginManager.registerEvents(new TalismanOfLightListener(this), this);
        pluginManager.registerEvents(new CustomSkullsListener(this), this);
        pluginManager.registerEvents(new MagnetListener(this), this);
        pluginManager.registerEvents(new ImageMapListener(this), this);

        // Add recipes
        getServer().addRecipe(new TalismanOfLightItemData(this).getRecipe());
        getServer().addRecipe(new MagnetItemData(this).getRecipe());

        // Alert
        System.out.println("Pepsi Dog plugin is enabled");
    }

    @Override
    public void onDisable() {
        // Alert
        System.out.println("Pepsi Dog plugin is disabled");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String[] args) {
        try {
            switch (command.getName().toLowerCase()) {
                case "opstick":
                    return new OpStickCommand(this).run(sender, command, label, args);

                case "whereis":
                    return new WhereIsCommand(this).run(sender, command, label, args);

                case "talismanoflight":
                    return new TalismanOfLightCommand(this).run(sender, command, label, args);

                case "magnet":
                    return new MagnetCommand(this).run(sender, command, label, args);

                case "imagemap":
                    return new ImageMapCommand(this).run(sender, command, label, args);
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.DARK_RED + "Error: command failed");
            getLogger().log(Level.SEVERE, e.getMessage());
        }

        return false;
    }

    public FileConfiguration getMapImagesConfig() {
        return mapImagesConfig;
    }

    public void saveMapImagesConfig() {
        try {
            getMapImagesConfig().save(mapImagesConfigFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save config to " + mapImagesConfigFile, e);
        }
    }

    private void createMapImagesConfig() {
        String fileName = "map-images.yml";
        mapImagesConfigFile = new File(getDataFolder(), fileName);

        if (!mapImagesConfigFile.exists()) {
            if (mapImagesConfigFile.getParentFile().mkdirs()) {
                saveResource(fileName, false);
            } else {
                System.out.println("Could not mkdirs");
            }
        } else {
            System.out.println("Config exists");
        }

        try {
            mapImagesConfig = new YamlConfiguration();
            mapImagesConfig.load(mapImagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            // This will always fail but the files will be loaded
            getLogger().log(Level.SEVERE, e.getMessage());
        }
    }
}
