package com.abilkaland.core.managers;

import com.abilkaland.core.AbilkaLandCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Level;

public class PluginManager {
    private final AbilkaLandCore plugin;
    
    public PluginManager(AbilkaLandCore plugin) {
        this.plugin = plugin;
    }
    
    public void autoConfigurePlugins() {
        plugin.getLogger().info("§6Начинаю автонастройку плагинов...");
        
        // Проверка зависимостей
        checkDependencies();
        
        // Настройка EssentialsX
        if (Bukkit.getPluginManager().getPlugin("Essentials") != null) {
            configureEssentials();
        }
        
        // Настройка LuckPerms
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") != null) {
            configureLuckPerms();
        }
        
        // Настройка WorldGuard
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            configureWorldGuard();
        }
        
        // Настройка GriefPrevention
        if (Bukkit.getPluginManager().getPlugin("GriefPrevention") != null) {
            configureGriefPrevention();
        }
        
        plugin.getLogger().info("§aАвтонастройка завершена!");
    }
    
    private void checkDependencies() {
        String[] required = {"Vault", "PlaceholderAPI"};
        String[] optional = {"DiscordSRV", "WorldEdit", "CoreProtect"};
        
        for (String pluginName : required) {
            if (Bukkit.getPluginManager().getPlugin(pluginName) == null) {
                plugin.getLogger().warning("§cТребуется плагин: " + pluginName);
            }
        }
    }
    
    private void configureEssentials() {
        try {
            FileConfiguration essentialsConfig = getPluginConfig("Essentials", "config.yml");
            if (essentialsConfig != null) {
                essentialsConfig.set("nickname-prefix", "&6[Abilka] ");
                essentialsConfig.set("change-displayname", true);
                essentialsConfig.set("teleport-cooldown", 5);
                essentialsConfig.set("economy.enabled", true);
                savePluginConfig("Essentials", "config.yml", essentialsConfig);
                plugin.getLogger().info("§aEssentials настроен!");
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Ошибка настройки Essentials", e);
        }
    }
    
    private void configureLuckPerms() {
        // LuckPerms обычно настраивается через команды
        plugin.getLogger().info("§eДля LuckPerms выполните команды вручную:");
        plugin.getLogger().info("§7/lp creategroup member");
        plugin.getLogger().info("§7/lp creategroup moderator");
        plugin.getLogger().info("§7/lp creategroup admin");
    }
    
    private void configureWorldGuard() {
        try {
            FileConfiguration wgConfig = getPluginConfig("WorldGuard", "config.yml");
            if (wgConfig != null) {
                wgConfig.set("region.claim.auto-claim-on-first-join", true);
                wgConfig.set("region.claim.auto-claim-size", 32);
                savePluginConfig("WorldGuard", "config.yml", wgConfig);
                plugin.getLogger().info("§aWorldGuard настроен!");
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Ошибка настройки WorldGuard", e);
        }
    }
    
    private void configureGriefPrevention() {
        try {
            FileConfiguration gpConfig = getPluginConfig("GriefPrevention", "config.yml");
            if (gpConfig != null) {
                gpConfig.set("claim.initial-blocks", 100);
                gpConfig.set("claim.min-size", 10);
                gpConfig.set("claim.accrued-per-hour", 10);
                savePluginConfig("GriefPrevention", "config.yml", gpConfig);
                plugin.getLogger().info("§aGriefPrevention настроен!");
            }
        } catch (Exception e) {
            plugin.getLogger().log(Level.WARNING, "Ошибка настройки GriefPrevention", e);
        }
    }
    
    private FileConfiguration getPluginConfig(String pluginName, String configFile) {
        org.bukkit.plugin.Plugin targetPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (targetPlugin != null) {
            File file = new File(targetPlugin.getDataFolder(), configFile);
            if (file.exists()) {
                return YamlConfiguration.loadConfiguration(file);
            }
        }
        return null;
    }
    
    private void savePluginConfig(String pluginName, String configFile, FileConfiguration config) {
        org.bukkit.plugin.Plugin targetPlugin = Bukkit.getPluginManager().getPlugin(pluginName);
        if (targetPlugin != null) {
            try {
                config.save(new File(targetPlugin.getDataFolder(), configFile));
            } catch (Exception e) {
                plugin.getLogger().warning("Не удалось сохранить конфиг для " + pluginName);
            }
        }
    }
}
