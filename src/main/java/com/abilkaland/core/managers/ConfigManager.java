package com.abilkaland.core.managers;

import com.abilkaland.core.AbilkaLandCore;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final AbilkaLandCore plugin;
    private final Map<String, FileConfiguration> configs = new HashMap<>();
    
    public ConfigManager(AbilkaLandCore plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfigs() {
        // Основной конфиг
        plugin.saveDefaultConfig();
        
        // Дополнительные конфиги
        saveAndLoad("messages.yml");
        saveAndLoad("milk-shop.yml");
        saveAndLoad("clan-settings.yml");
        saveAndLoad("plugin-dependencies.yml");
        
        plugin.getLogger().info("§aКонфигурации загружены!");
    }
    
    private void saveAndLoad(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }
        configs.put(fileName, YamlConfiguration.loadConfiguration(configFile));
    }
    
    public FileConfiguration getConfig(String name) {
        if (name.equals("config")) {
            return plugin.getConfig();
        }
        return configs.getOrDefault(name + ".yml", plugin.getConfig());
    }
    
    public void saveConfig(String name) {
        try {
            File configFile = new File(plugin.getDataFolder(), name + ".yml");
            configs.get(name + ".yml").save(configFile);
        } catch (Exception e) {
            plugin.getLogger().severe("Ошибка сохранения конфига: " + name);
        }
    }
    
    public void reloadConfigs() {
        plugin.reloadConfig();
        for (String configName : configs.keySet()) {
            File configFile = new File(plugin.getDataFolder(), configName);
            configs.put(configName, YamlConfiguration.loadConfiguration(configFile));
        }
    }
}
