package com.abilkaland.core.commands;

import com.abilkaland.core.AbilkaLandCore;
import com.abilkaland.core.gui.MilkShopGUI;
import com.abilkaland.core.managers.MilkEconomy;
import com.abilkaland.core.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MilkCommand implements CommandExecutor {
    private final AbilkaLandCore plugin;
    
    public MilkCommand(AbilkaLandCore plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только для игроков!");
            return true;
        }
        
        Player player = (Player) sender;
        MilkEconomy economy = plugin.getMilkEconomy();
        
        if (args.length == 0) {
            double balance = economy.getBalance(player);
            player.sendMessage(MessageUtils.color("&7Твой баланс: &6" + balance + " 🥛"));
            player.sendMessage(MessageUtils.color("&7Используй &e/milk shop &7для открытия магазина"));
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "shop":
                new MilkShopGUI(plugin).open(player);
                break;
                
            case "pay":
                if (args.length < 3) {
                    player.sendMessage(MessageUtils.color("&cИспользуйте: /milk pay <игрок> <сумма>"));
                    return true;
                }
                
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    player.sendMessage(MessageUtils.color("&cИгрок не найден!"));
                    return true;
                }
                
                try {
                    double amount = Double.parseDouble(args[2]);
                    if (amount <= 0) {
                        player.sendMessage(MessageUtils.color("&cСумма должна быть положительной!"));
                        return true;
                    }
                    
                    if (economy.removeBalance(player, amount, "Перевод игроку " + target.getName())) {
                        economy.addBalance(target, amount, "Перевод от " + player.getName());
                        player.sendMessage(MessageUtils.color("&aВы перевели &6" + amount + " 🥛 &aигроку &6" + target.getName()));
                        target.sendMessage(MessageUtils.color("&aВы получили &6" + amount + " 🥛 &aот &6" + player.getName()));
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(MessageUtils.color("&cНеверная сумма!"));
                }
                break;
                
            case "top":
                // Топ игроков по молочку
                player.sendMessage(MessageUtils.color("&6Топ игроков по Молочку:"));
                player.sendMessage(MessageUtils.color("&71. &6gordey25690 &8- 1500 🥛"));
                player.sendMessage(MessageUtils.color("&72. &6PCshelly &8- 1200 🥛"));
                player.sendMessage(MessageUtils.color("&73. &6Новичок &8- 500 🥛"));
                break;
                
            default:
                player.sendMessage(MessageUtils.color("&cИспользуйте:"));
                player.sendMessage(MessageUtils.color("&e/milk &7- Баланс"));
                player.sendMessage(MessageUtils.color("&e/milk shop &7- Магазин"));
                player.sendMessage(MessageUtils.color("&e/milk pay <игрок> <сумма> &7- Перевод"));
                player.sendMessage(MessageUtils.color("&e/milk top &7- Топ игроков"));
                break;
        }
        
        return true;
    }
}
