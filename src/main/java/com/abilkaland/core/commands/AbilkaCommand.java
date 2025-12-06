package com.abilkaland.core.commands;

import com.abilkaland.core.AbilkaLandCore;
import com.abilkaland.core.managers.ClanManager;
import com.abilkaland.core.utils.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AbilkaCommand implements CommandExecutor {
    private final AbilkaLandCore plugin;
    
    public AbilkaCommand(AbilkaLandCore plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showMainMenu(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "info":
                showClanInfo(sender);
                break;
                
            case "members":
                showMembers(sender);
                break;
                
            case "help":
                showHelp(sender);
                break;
                
            case "reload":
                if (sender.hasPermission("abilkaland.admin")) {
                    plugin.getConfigManager().reloadConfigs();
                    sender.sendMessage(MessageUtils.color("&aКонфиги перезагружены!"));
                } else {
                    sender.sendMessage(MessageUtils.color("&cНет прав!"));
                }
                break;
                
            default:
                sender.sendMessage(MessageUtils.color("&cИспользуйте: /abilka [info|members|help]"));
                break;
        }
        
        return true;
    }
    
    private void showMainMenu(CommandSender sender) {
        String prefix = MessageUtils.getPrefix();
        String clanName = plugin.getConfig().getString("settings.clan-name", "AbilkaLand");
        String motd = plugin.getConfig().getString("settings.clan-motd", "");
        
        sender.sendMessage(MessageUtils.color("&8&m--------------------------------"));
        sender.sendMessage(MessageUtils.color("&6&l" + clanName));
        sender.sendMessage(MessageUtils.color("&7" + motd));
        sender.sendMessage("");
        sender.sendMessage(MessageUtils.color("&e/abilka info &7- Информация о клане"));
        sender.sendMessage(MessageUtils.color("&e/abilka members &7- Список участников"));
        sender.sendMessage(MessageUtils.color("&e/abilka help &7- Помощь по командам"));
        sender.sendMessage(MessageUtils.color("&e/milk &7- Управление молочком"));
        sender.sendMessage(MessageUtils.color("&8&m--------------------------------"));
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            double balance = plugin.getMilkEconomy().getBalance(player);
            sender.sendMessage(MessageUtils.color("&7Твой баланс: &6" + balance + " 🥛"));
        }
    }
    
    private void showClanInfo(CommandSender sender) {
        ClanManager clanManager = plugin.getClanManager();
        
        sender.sendMessage(MessageUtils.color("&8&m--[ &6Информация о клане &8&m]--"));
        sender.sendMessage(MessageUtils.color("&7Название: &6" + clanManager.getClanName()));
        sender.sendMessage(MessageUtils.color("&7Создан: &6" + clanManager.getCreationDate()));
        sender.sendMessage(MessageUtils.color("&7Участников: &6" + clanManager.getMemberCount()));
        sender.sendMessage(MessageUtils.color("&7Онлайн: &6" + clanManager.getOnlineCount()));
        sender.sendMessage(MessageUtils.color("&8&m----------------------------"));
        
        String gimn = "Пушистый комочек с кактуса слетел,\n" +
                      "В наш клан попал — и сразу приумнел!\n" +
                      "Здесь каждый брат и каждый — друг,\n" +
                      "В AbilkaLand мы все вокруг!";
        
        for (String line : gimn.split("\n")) {
            sender.sendMessage(MessageUtils.color("&7" + line));
        }
    }
    
    private void showMembers(CommandSender sender) {
        // Здесь можно добавить вывод списка участников
        sender.sendMessage(MessageUtils.color("&6Список участников:"));
        sender.sendMessage(MessageUtils.color("&7- gordey25690 &8[Основатель]"));
        sender.sendMessage(MessageUtils.color("&7- PCshelly &8[Казначей]"));
        sender.sendMessage(MessageUtils.color("&7и другие котики..."));
    }
    
    private void showHelp(CommandSender sender) {
        sender.sendMessage(MessageUtils.color("&8&m--[ &6Помощь по командам &8&m]--"));
        sender.sendMessage(MessageUtils.color("&e/abilka &7- Главное меню"));
        sender.sendMessage(MessageUtils.color("&e/milk &7- Баланс и магазин"));
        sender.sendMessage(MessageUtils.color("&e/milk shop &7- Открыть магазин"));
        sender.sendMessage(MessageUtils.color("&e/milk pay <игрок> <сумма> &7- Перевести молочко"));
        sender.sendMessage(MessageUtils.color("&8&m----------------------------"));
    }
}
