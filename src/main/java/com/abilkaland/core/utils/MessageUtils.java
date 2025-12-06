package com.abilkaland.core.utils;

import com.abilkaland.core.AbilkaLandCore;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");
    
    public static String color(String message) {
        if (message == null) return "";
        
        // Поддержка HEX цветов (&#RRGGBB)
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuffer buffer = new StringBuffer();
        
        while (matcher.find()) {
            String color = matcher.group(1);
            matcher.appendReplacement(buffer, ChatColor.of("#" + color).toString());
        }
        matcher.appendTail(buffer);
        
        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }
    
    public static String getPrefix() {
        return color("&8[&6AbilkaLand&8] &7");
    }
    
    public static void broadcast(String message) {
        Bukkit.broadcastMessage(color(message));
    }
    
    public static void sendToPlayer(String playerName, String message) {
        if (Bukkit.getPlayer(playerName) != null) {
            Bukkit.getPlayer(playerName).sendMessage(color(message));
        }
    }
}
