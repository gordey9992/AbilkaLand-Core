package com.abilkaland.core.integration;

import com.abilkaland.core.AbilkaLandCore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class PlaceholderExpansion extends PlaceholderExpansion {
    private final AbilkaLandCore plugin;
    
    public PlaceholderExpansion(AbilkaLandCore plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public @NotNull String getIdentifier() {
        return "abilka";
    }
    
    @Override
    public @NotNull String getAuthor() {
        return "DeepSeek & gordey25690 & PCshelly";
    }
    
    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }
    
    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) return "";
        
        switch (params.toLowerCase()) {
            case "milk":
                if (player.isOnline()) {
                    double balance = plugin.getMilkEconomy().getBalance(player.getPlayer());
                    return String.valueOf((int) balance);
                }
                return "0";
                
            case "clan_name":
                return plugin.getConfig().getString("settings.clan-name", "AbilkaLand");
                
            case "clan_tag":
                return plugin.getConfig().getString("settings.clan-tag", "&6[Abilka]");
                
            case "player_milk":
                if (player.isOnline()) {
                    double balance = plugin.getMilkEconomy().getBalance(player.getPlayer());
                    return balance + " 🥛";
                }
                return "0 🥛";
                
            case "milk_formatted":
                if (player.isOnline()) {
                    double balance = plugin.getMilkEconomy().getBalance(player.getPlayer());
                    return String.format("%,.0f", balance) + " 🥛";
                }
                return "0 🥛";
        }
        
        return null;
    }
}
