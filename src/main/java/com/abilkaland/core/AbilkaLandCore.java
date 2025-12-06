package com.abilkaland.core;

import com.abilkaland.core.managers.*;
import com.abilkaland.core.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class AbilkaLandCore extends JavaPlugin {
    
    private static AbilkaLandCore instance;
    private ConfigManager configManager;
    private MilkEconomy milkEconomy;
    private PluginManager pluginManager;
    private ClanManager clanManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Инициализация менеджеров
        configManager = new ConfigManager(this);
        milkEconomy = new MilkEconomy(this);
        pluginManager = new PluginManager(this);
        clanManager = new ClanManager(this);
        
        // Загрузка конфигов
        configManager.loadConfigs();
        
        // Автонастройка плагинов
        if (getConfig().getBoolean("settings.auto-configure.enabled", true)) {
            pluginManager.autoConfigurePlugins();
        }
        
        // Регистрация команд
        getCommand("abilka").setExecutor(new AbilkaCommand(this));
        getCommand("milk").setExecutor(new MilkCommand(this));
        
        // Регистрация ивентов
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new ClanEventsListener(this), this);
        
        // Интеграции
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderExpansion(this).register();
        }
        
        getLogger().info("§6AbilkaLand Core включен! Клан готов к приключениям!");
    }
    
    @Override
    public void onDisable() {
        // Сохранение данных
        milkEconomy.saveBalances();
        clanManager.saveData();
        
        getLogger().info("§6AbilkaLand Core выключен. До новых встреч!");
    }
    
    public static AbilkaLandCore getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public MilkEconomy getMilkEconomy() {
        return milkEconomy;
    }
    
    public ClanManager getClanManager() {
        return clanManager;
    }
}
