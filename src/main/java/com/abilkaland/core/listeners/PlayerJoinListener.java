package com.abilkaland.core.listeners;

import com.abilkaland.core.AbilkaLandCore;
import com.abilkaland.core.managers.MilkEconomy;
import com.abilkaland.core.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final AbilkaLandCore plugin;
    
    public PlayerJoinListener(AbilkaLandCore plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MilkEconomy economy = plugin.getMilkEconomy();
        
        // Кастомное сообщение
        String joinMessage = plugin.getConfigManager().getConfig("messages").getString("welcome.join")
                .replace("{player}", player.getName())
                .replace("{prefix}", MessageUtils.getPrefix());
        
        event.setJoinMessage(null); // Отключаем стандартное сообщение
        
        // Отправляем приветствие в чат
        Bukkit.broadcastMessage(MessageUtils.color(joinMessage));
        
        // Персональное сообщение игроку
        if (!player.hasPlayedBefore()) {
            // Новый игрок
            String firstJoin = plugin.getConfigManager().getConfig("messages").getString("welcome.first-join");
            double starting = plugin.getConfig().getDouble("milk-economy.starting-balance", 50);
            
            economy.addBalance(player, starting, "Первое появление");
            player.sendMessage(MessageUtils.color(firstJoin
                    .replace("{amount}", String.valueOf(starting))
                    .replace("{prefix}", MessageUtils.getPrefix())));
            
            // Выдаем титул
            player.sendTitle(
                MessageUtils.color("&6Добро пожаловать!"),
                MessageUtils.color("&7в клан AbilkaLand"),
                10, 70, 20
            );
        } else {
            // Старый игрок - начисляем ежедневный бонус
            economy.addDailyLogin(player);
            
            // Показываем баланс
            double balance = economy.getBalance(player);
            player.sendMessage(MessageUtils.color("&7Твой баланс: &6" + balance + " 🥛"));
        }
        
        // Запускаем таймер для ежечасного бонуса
        startHourlyBonus(player);
    }
    
    private void startHourlyBonus(Player player) {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (player.isOnline()) {
                plugin.getMilkEconomy().addHourlyPlay(player);
            }
        }, 72000L, 72000L); // Каждый час (20 тиков * 60 секунд * 60 минут)
    }
}
