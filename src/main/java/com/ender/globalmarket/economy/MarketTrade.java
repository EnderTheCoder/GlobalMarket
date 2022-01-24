package com.ender.globalmarket.economy;

import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.money.Vault;
import com.ender.globalmarket.player.Inventory;
import com.ender.globalmarket.player.PlayerRegData;
import com.ender.globalmarket.storage.Mysql;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class MarketTrade {
    public enum type{
        SELL,
        BUY
    }

    //贸易
    public static void trade(Player player, MarketItem marketItem, int amount, type type) {
        double price = 0.0;
        double tax = 0.0;
        switch (type) {
            case SELL: {
                //计算价格
                price = MarketEconomy.getSellingPrice(marketItem, amount);
                //计算贸易税
                tax = (PlayerRegData.isVIP(player)) ? 0.0 : MarketEconomy.getTax(price);
                //更新市场数据
                MarketData.updateMarketItem(marketItem);
                //更新玩家货币数据
                Vault.addVaultCurrency(player.getUniqueId(), price - tax);
                //更新玩家储存
                Inventory.subtractInventory(player, marketItem.item, amount);
                break;
            }
            case BUY: {
                price = MarketEconomy.getBuyingPrice(marketItem, amount);
                tax = (PlayerRegData.isVIP(player)) ? 0.0 : MarketEconomy.getTax(price);
                MarketData.updateMarketItem(marketItem);
                Vault.subtractCurrency(player.getUniqueId(), price + tax);
                Inventory.addInventory(player, marketItem.item, amount);
                break;
            }
        }
        //记录贸易
        log(player, marketItem.item, type, price, tax, amount);
        message(player, type, marketItem.item, amount, price, tax);
    }
    //计算税后购买价格
    public static double getBuyPriceWithTax(Player player, MarketItem marketItem, int amount) {
        double price = 0.0;
        double tax = 0.0;
        price = MarketEconomy.getBuyingPrice(marketItem, amount);
        tax = (PlayerRegData.isVIP(player)) ? 0.0 : MarketEconomy.getTax(price);
        return price + tax;
    }
    //获取用户贸易税
    public static double getTax(Player player, double price) {
        return (PlayerRegData.isVIP(player)) ? 0.0 : MarketEconomy.getTax(price);
    }
    //检查用户是否有足够的钱
    public static boolean isAffordFor(Player player, MarketItem marketItem, int amount) {
        return Vault.checkCurrency(player.getUniqueId()) >= getBuyPriceWithTax(player, marketItem, amount);
    }
    //检查玩家是否有足够的背包空格
    public static boolean isEnoughSlot(Player player, int amount, Material material) {
        return  Inventory.calcEmpty(player) >= amount / material.getMaxStackSize() + ((amount % material.getMaxStackSize() > 0) ? 1 : 0);
    }
    //检测物品数量合法性
    public static boolean isAmountLegal(String amount) {
        try {
            int i = Integer.parseInt(amount);
            return i > 0;
        } catch (Exception e) {
            return false;
        }
    }
    //检测物品是否存在
    public static boolean isMaterialExists(String material) {
        return (Material.matchMaterial(material) != null);
    }
    //检测市场是否存在该物品
    public static boolean isMarketItemExists(Material material) {
        return MarketData.getMarketItem(material) != null;
    }
    //检测市场库存是否充足
    public static boolean isMarketXEnough(MarketItem marketItem, int amount) {
        return marketItem.x >= amount;
    }
    //检测玩家背包中是否有足够的物品
    public static boolean isPlayerItemEnough(Player player, Material material, int amount) {
        return Inventory.calcInventory(player, material) >= amount;
    }
    //记录交易
    public static void log(Player player, Material material, type type, double price, double tax ,int amount) {
        Mysql m = new Mysql();
        m.prepareSql("INSERT INTO market_log (username, type, time, item, amount, price, tax) VALUES (?, ?, ?, ?, ?, ?, ?)");
        m.setData(1, player.getName());
        m.setData(2, String.valueOf(type));
        m.setData(3, String.valueOf(new Timestamp(System.currentTimeMillis()).getTime() /1000));
        m.setData(4, material.name());
        m.setData(5, String.valueOf(amount));
        m.setData(6, String.valueOf(price));
        m.setData(7, String.valueOf(tax));
        m.execute();
        m.close();
    }
    //交易提示信息
    public static void message(Player player, type type, Material material, int amount, double price, double tax) {
        player.sendMessage(ChatColor.GREEN + String.format("[GlobalMarket]你成功%s了%s个%s，税后%s$%s，其中贸易税为$%s",
                (type == MarketTrade.type.BUY) ? "购买" : "出售",
                amount,
                material.name(),
                (type == MarketTrade.type.BUY) ? "花费" : "所得",
                (type == MarketTrade.type.BUY) ? MarketEconomy.formatMoney(price + tax) : MarketEconomy.formatMoney(price - tax),
                tax
        ));
    }
}
