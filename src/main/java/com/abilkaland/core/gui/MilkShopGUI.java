package com.abilkaland.core.gui;

import com.abilkaland.core.AbilkaLandCore;
import com.abilkaland.core.managers.MilkEconomy;
import com.abilkaland.core.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class MilkShopGUI {
    private final AbilkaLandCore plugin;
    private final Inventory inventory;
    
    public MilkShopGUI(AbilkaLandCore plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null, 54, 
            MessageUtils.color("&8Магазин &6Молочка"));
        setupItems();
    }
    
    private void setupItems() {
        // Заполняем границы
        ItemStack border = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName(" ");
        border.setItemMeta(borderMeta);
        
        for (int i = 0; i < 54; i++) {
            if (i < 9 || i > 44 || i % 9 == 0 || i % 9 == 8) {
                inventory.setItem(i, border);
            }
        }
        
        // Категория ресурсов
        addCategoryItem(10, Material.DIAMOND, "&bРесурсы", 
            Arrays.asList("&7Алмазы, изумруды, редкие", "&7материалы для строительства"),
            "resources");
        
        // Категория косметики
        addCategoryItem(12, Material.FEATHER, "&dКосметика", 
            Arrays.asList("&7Частицы, питомцы,", "&7уникальная внешность"),
            "cosmetics");
        
        // Категория привилегий
        addCategoryItem(14, Material.NETHER_STAR, "&aПривилегии клана", 
            Arrays.asList("&7Особые права и", "&7возможности в клане"),
            "perks");
        
        // Пример товара
        addShopItem(28, Material.DIAMOND, "&bНабор алмазов", 
            Arrays.asList("", "&764 алмаза для крафта", "", "&eЦена: &6100 🥛", "", "&aЛКМ - купить"),
            100, "give {player} diamond 64");
        
        addShopItem(29, Material.ENCHANTED_BOOK, "&dКнига зачарований", 
            Arrays.asList("", "&7Sharpness V", "", "&eЦена: &6250 🥛", "", "&aЛКМ - купить"),
            250, "give {player} enchanted_book{StoredEnchantments:[{id:sharpness,lvl:5}]} 1");
        
        // Информация о балансе
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(MessageUtils.color("&6Твой баланс"));
        infoMeta.setLore(Arrays.asList(
            "&7Здесь отображается", 
            "&7твоё Молочко",
            "",
            "&eНаведи на товар и",
            "&eнажми ЛКМ для покупки"
        ));
        info.setItemMeta(infoMeta);
        inventory.setItem(49, info);
    }
    
    private void addCategoryItem(int slot, Material material, String name, List<String> lore, String category) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color(name));
        
        List<String> coloredLore = lore.stream()
                .map(MessageUtils::color)
                .toList();
        meta.setLore(coloredLore);
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    private void addShopItem(int slot, Material material, String name, List<String> lore, double price, String command) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageUtils.color(name));
        
        List<String> coloredLore = lore.stream()
                .map(line -> MessageUtils.color(line.replace("{price}", String.valueOf(price))))
                .toList();
        meta.setLore(coloredLore);
        
        item.setItemMeta(meta);
        inventory.setItem(slot, item);
    }
    
    public void open(Player player) {
        // Обновляем информацию о балансе
        MilkEconomy economy = plugin.getMilkEconomy();
        double balance = economy.getBalance(player);
        
        ItemStack balanceItem = inventory.getItem(49);
        if (balanceItem != null) {
            ItemMeta meta = balanceItem.getItemMeta();
            meta.setLore(Arrays.asList(
                MessageUtils.color("&7Твой баланс: &6" + balance + " 🥛"),
                "",
                MessageUtils.color("&eНаведи на товар и"),
                MessageUtils.color("&eнажми ЛКМ для покупки")
            ));
            balanceItem.setItemMeta(meta);
        }
        
        player.openInventory(inventory);
    }
    
    public void handleClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(MessageUtils.color("&8Магазин &6Молочка"))) {
            return;
        }
        
        event.setCancelled(true);
        
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        
        Player player = (Player) event.getWhoClicked();
        int slot = event.getSlot();
        
        // Обработка покупки товаров
        if (slot == 28) { // Алмазы
            handlePurchase(player, 100, "give " + player.getName() + " diamond 64", "Набор алмазов");
        } else if (slot == 29) { // Книга
            handlePurchase(player, 250, "give " + player.getName() + " enchanted_book{StoredEnchantments:[{id:sharpness,lvl:5}]} 1", "Книга зачарований");
        }
    }
    
    private void handlePurchase(Player player, double price, String command, String itemName) {
        MilkEconomy economy = plugin.getMilkEconomy();
        
        if (economy.removeBalance(player, price, "Покупка: " + itemName)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            player.sendMessage(MessageUtils.color("&aВы купили &6" + itemName + " &aза &6" + price + " 🥛"));
            player.closeInventory();
        }
    }
}
