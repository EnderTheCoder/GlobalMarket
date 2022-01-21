package com.ender.globalmarket.economy;

import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.storage.Mysql;
import org.bukkit.Material;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class MarketData {
    public static List<MarketItem> getAllMarketItems(){
        Mysql m = new Mysql();

        List<MarketItem> list = new ArrayList<>();


        m.prepareSql("SELECT * FROM market_item_data");
        m.execute();
        ResultSet resultSet = m.getResult();

        try {
            while (resultSet.next()) {
                MarketItem marketItem = new MarketItem();
                marketItem.item = Material.matchMaterial(resultSet.getString("item_name"));
                marketItem.x = resultSet.getInt("x");
                marketItem.k = resultSet.getInt("k");
                marketItem.b = resultSet.getInt("b");
                if (marketItem.item != null) list.add(marketItem);
                else getLogger().warning(String.format("[GlobalMarket]We found an illegal item in your database which names '%s'. Please check if it's a bug.", resultSet.getString("item_name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.close();
        return list;
    }


    public static MarketItem getMarketItem(Material item) {
        Mysql m = new Mysql();
        m.prepareSql("SELECT * FROM market_item_data where item_name = ?");
        m.setData(1, item.name());
        m.execute();
        ResultSet r = m.getResult();
        MarketItem marketItem = new MarketItem();

        try {
            r.next();


            if (r.getString("item_name") == null) {
                return null;
            }

            marketItem.item = item;
            marketItem.x = r.getInt("x");
            marketItem.k = r.getInt("k");
            marketItem.b = r.getInt("b");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.close();
        return marketItem;
    }

    public static void putMarketItem(MarketItem item) {
        Mysql m = new Mysql();
        m.prepareSql("INSERT INTO market_item_data (item_name, x, k, b) values (?, ?, ?, ?)");
        m.setData(1, item.item.name());
        m.setData(2, String.valueOf(item.x));
        m.setData(3, String.valueOf(item.k));
        m.setData(4, String.valueOf(item.b));
        m.execute();
        m.close();
    }

    public static void updateMarketItemStorage(MarketItem item) {
        Mysql m = new Mysql();
        m.prepareSql("UPDATE market_item_data SET x = ?, b = ?, k = ? WHERE item_name = ?");
        m.setData(1, String.valueOf(item.x));
        m.setData(2, String.valueOf(item.b));
        m.setData(3, String.valueOf(item.k));
        m.setData(4, String.valueOf(item.item.name()));
        m.execute();
        m.close();
    }

    public static void removeMarketItem(MarketItem item) {
        Mysql m = new Mysql();
        m.prepareSql("DELETE FROM market_item_data WHERE item_name = ?");
        m.setData(1, item.item.name());
        m.execute();
        m.close();
    }

    public static void removeMarketItem(String itemName) {
        Mysql m = new Mysql();
        m.prepareSql("DELETE FROM market_item_data WHERE item_name = ?");
        m.setData(1, itemName);
        m.execute();
        m.close();
    }
}
