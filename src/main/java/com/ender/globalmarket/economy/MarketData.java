package com.ender.globalmarket.economy;

import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.storage.Mysql;
import org.bukkit.Material;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MarketData {
    public static ResultSet getAllMarketItems() throws SQLException {
        Mysql m = new Mysql();
        m.prepareSql("SELECT * FROM market_item_data");
        m.execute();

        return m.getResult();
    }


    public static MarketItem getMarketItem(Material item) {
        Mysql m = new Mysql();
        m.prepareSql("SELECT * FROM market_item_data where item_name = ?");
        m.setData(1, String.valueOf(item.getKey()));
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
        return marketItem;
    }

    public static void putMarketItem(MarketItem item) {
        Mysql m = new Mysql();
        m.prepareSql("INSERT INTO market_item_data (item_name, x, k, b) values (?, ?, ?, ?)");
        m.setData(1, String.valueOf(item.item.getKey()));
        m.setData(2, String.valueOf(item.x));
        m.setData(3, String.valueOf(item.k));
        m.setData(4, String.valueOf(item.b));
        m.execute();
    }

    public static void updateMarketItemStorage(MarketItem item) {
        Mysql m = new Mysql();
        m.prepareSql("UPDATE market_item_data SET x = ?, b = ?, k = ? WHERE item_name = ?");
        m.setData(1, String.valueOf(item.x));
        m.setData(2, String.valueOf(item.b));
        m.setData(3, String.valueOf(item.k));
        m.setData(4, String.valueOf(item.item.getKey()));
        m.execute();
    }
}
