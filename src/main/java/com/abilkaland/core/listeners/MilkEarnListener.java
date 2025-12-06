package com.abilkaland.core.listeners;

import com.abilkaland.core.AbilkaLandCore;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MilkEarnListener implements Listener {
    private final AbilkaLandCore plugin;
    
    public MilkEarnListener(AbilkaLandCore plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        // Алмазы
        if (event.getBlock().getType() == Material.DIAMOND_ORE) {
            double amount = plugin.getConfig().getDouble("milk-economy.earn-rules.mining-diamond", 15);
            plugin.getMilkEconomy().addBalance(player, amount, "Добыча алмаза");
        }
        
        // Другие руды можно добавить здесь
    }
    
    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player player = event.getEntity().getKiller();
            
            // За убийство моба
            double amount = plugin.getConfig().getDouble("milk-economy.earn-rules.mob-kill", 2);
            plugin.getMilkEconomy().addBalance(player, amount, "Убийство " + event.getEntityType().toString());
        }
    }
}
