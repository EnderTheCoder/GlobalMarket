package com.ender.globalmarket.economy;

import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.storage.ConfigReader;
import org.bukkit.Material;

import static org.bukkit.Bukkit.getLogger;

public class MarketEconomy {

    public static double formatMoney(double money) {
        return Double.parseDouble(String.format("%.2f", money));
    }

    public static double getBuyingPrice(MarketItem item, int count) {
        double price = 0.0;

        while (count > 0) {
            count--;
            item.x--;
            price += calculate(item);
        }
        return formatMoney(price);
    }

    public static double getSellingPrice(MarketItem item, int count) {
        double price = 0.0;
        while (count > 0) {
            count--;
            price += calculate(item);
            item.x++;
        }
        return formatMoney(price);
    }

    public static double getTax(double price) {
        return formatMoney(price * ConfigReader.getTaxRate());
    }
    //价格计算函数式:f(x)=k/(x+1)^(1/b)
    public static double calculate(MarketItem item) {
        double price = item.k / Math.pow((double) item.x + 1.0,  1.0 / (double) item.b);
        return Math.max(price, 0.01);
    }



}
