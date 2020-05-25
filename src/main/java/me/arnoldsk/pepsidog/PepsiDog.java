package me.arnoldsk.pepsidog;

import me.arnoldsk.pepsidog.CustomDrops.CustomDropsListener;
import me.arnoldsk.pepsidog.NameColors.NameColorsListener;
import me.arnoldsk.pepsidog.OpStick.OpStickCommand;
import me.arnoldsk.pepsidog.OpStick.OpStickListener;
import me.arnoldsk.pepsidog.PlayerBeds.PlayerBedsListener;
import me.arnoldsk.pepsidog.SoftSpawnProtection.SoftSpawnProtectionListener;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class PepsiDog extends JavaPlugin {
    FileConfiguration config = getConfig();

    File playerBedsConfigFile;
    FileConfiguration playerBedsConfig;

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Init configuration
        config.addDefault("enabled", true);
        config.addDefault("softSpawnProtection", 50);
        config.options().copyDefaults(true);
        saveConfig();

        // Custom config
        createPlayerBedsConfig();

        // Register events
        pluginManager.registerEvents(new PlayerBedsListener(this), this);
        pluginManager.registerEvents(new NameColorsListener(this), this);
        pluginManager.registerEvents(new SoftSpawnProtectionListener(this), this);
        pluginManager.registerEvents(new CustomDropsListener(this), this);
        pluginManager.registerEvents(new OpStickListener(this), this);

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
            }
        } catch (Exception e) {
            sender.sendMessage(ChatColor.DARK_RED + "Error: command failed");
            getLogger().log(Level.SEVERE, e.getMessage());
        }

        return false;
    }

    public FileConfiguration getPlayerBedsConfig() {
        return playerBedsConfig;
    }

    public void savePlayerBedsConfig() {
        try {
            playerBedsConfig.save(playerBedsConfigFile);
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save config to " + playerBedsConfigFile, e);
        }
    }

    void createPlayerBedsConfig() {
        String fileName = "player-beds.yml";
        playerBedsConfigFile = new File(getDataFolder(), fileName);

        if (!playerBedsConfigFile.exists()) {
            if (playerBedsConfigFile.getParentFile().mkdirs()) {
                saveResource(fileName, false);
            } else {
                System.out.println("Could not mkdirs");
            }
        } else {
            System.out.println("Config exists");
        }

        try {
            playerBedsConfig = new YamlConfiguration();
            playerBedsConfig.load(playerBedsConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            // This will always fail but the files will be loaded
            getLogger().log(Level.SEVERE, e.getMessage());
        }
    }
}
