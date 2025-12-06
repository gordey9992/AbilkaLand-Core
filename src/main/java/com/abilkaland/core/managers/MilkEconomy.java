package com.abilkaland.core.managers;

import com.abilkaland.core.AbilkaLandCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MilkEconomy {
    private final AbilkaLandCore plugin;
    private final Map<UUID, Double> balances = new HashMap<>();
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    
    public MilkEconomy(AbilkaLandCore plugin) {
        this.plugin = plugin;
        loadBalances();
    }
    
    public double getBalance(Player player) {
        return balances.getOrDefault(player.getUniqueId(), 0.0);
    }
    
    public void setBalance(Player player, double amount) {
        balances.put(player.getUniqueId(), amount);
        saveBalances();
    }
    
    public void addBalance(Player player, double amount, String reason) {
        double current = getBalance(player);
        balances.put(player.getUniqueId(), current + amount);
        
        // Сообщение игроку
        String message = plugin.getConfigManager().getConfig("messages").getString("milk.earned")
                .replace("{amount}", String.valueOf(amount))
                .replace("{reason}", reason);
        player.sendMessage(message);
        
        // Логирование
        plugin.getLogger().info(player.getName() + " получил " + amount + " Молочка за: " + reason);
        
        // Discord интеграция
        if (plugin.getConfig().getBoolean("milk-economy.integrations.discord", false)) {
            sendDiscordLog(player, amount, reason, "earn");
        }
    }
    
    public boolean removeBalance(Player player, double amount, String reason) {
        double current = getBalance(player);
        if (current >= amount) {
            balances.put(player.getUniqueId(), current - amount);
            saveBalances();
            
            String message = plugin.getConfigManager().getConfig("messages").getString("milk.spent")
                    .replace("{amount}", String.valueOf(amount))
                    .replace("{reason}", reason);
            player.sendMessage(message);
            
            return true;
        } else {
            String message = plugin.getConfigManager().getConfig("messages").getString("milk.not-enough")
                    .replace("{amount}", String.valueOf(amount));
            player.sendMessage(message);
            return false;
        }
    }
    
    public void addHourlyPlay(Player player) {
        if (checkCooldown(player, "hourly")) {
            double amount = plugin.getConfig().getDouble("milk-economy.earn-rules.hourly-play", 10);
            addBalance(player, amount, "Ежечасная активность");
            setCooldown(player, "hourly", 3600000); // 1 час
        }
    }
    
    public void addDailyLogin(Player player) {
        if (checkCooldown(player, "daily")) {
            double amount = plugin.getConfig().getDouble("milk-economy.earn-rules.daily-login", 25);
            addBalance(player, amount, "Ежедневный вход");
            setCooldown(player, "daily", 86400000); // 24 часа
        }
    }
    
    public void addMobKill(Player player) {
        double amount = plugin.getConfig().getDouble("milk-economy.earn-rules.mob-kill", 2);
        addBalance(player, amount, "Убийство моба");
    }
    
    private boolean checkCooldown(Player player, String type) {
        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) {
            cooldowns.put(uuid, new HashMap<>());
            return true;
        }
        
        Map<String, Long> playerCooldowns = cooldowns.get(uuid);
        if (!playerCooldowns.containsKey(type)) {
            return true;
        }
        
        long lastTime = playerCooldowns.get(type);
        return System.currentTimeMillis() - lastTime > 0;
    }
    
    private void setCooldown(Player player, String type, long cooldown) {
        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) {
            cooldowns.put(uuid, new HashMap<>());
        }
        cooldowns.get(uuid).put(type, System.currentTimeMillis() + cooldown);
    }
    
    public void saveBalances() {
        FileConfiguration config = plugin.getConfigManager().getConfig("clan-settings");
        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            config.set("milk-balances." + entry.getKey().toString(), entry.getValue());
        }
        plugin.getConfigManager().saveConfig("clan-settings");
    }
    
    private void loadBalances() {
        FileConfiguration config = plugin.getConfigManager().getConfig("clan-settings");
        if (config.contains("milk-balances")) {
            for (String key : config.getConfigurationSection("milk-balances").getKeys(false)) {
                try {
                    UUID uuid = UUID.fromString(key);
                    double balance = config.getDouble("milk-balances." + key);
                    balances.put(uuid, balance);
                } catch (IllegalArgumentException e) {
                    plugin.getLogger().warning("Неверный UUID в балансах: " + key);
                }
            }
        }
    }
    
    private void sendDiscordLog(Player player, double amount, String reason, String type) {
        // Интеграция с DiscordSRV
        if (Bukkit.getPluginManager().getPlugin("DiscordSRV") != null) {
            String channelId = plugin.getConfig().getString("discord.milk-transactions-channel");
            if (channelId != null) {
                String message = "**" + player.getName() + "** " + 
                    (type.equals("earn") ? "получил" : "потратил") + 
                    " **" + amount + "** 🥛 за: " + reason;
                
                // Отправка через DiscordSRV API
                Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                    try {
                        Class.forName("github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel");
                        // Код интеграции с DiscordSRV
                    } catch (Exception e) {
                        // Игнорируем если DiscordSRV не найден
                    }
                });
            }
        }
    }
}
