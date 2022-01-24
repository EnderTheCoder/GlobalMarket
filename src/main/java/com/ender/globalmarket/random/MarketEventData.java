package com.ender.globalmarket.random;

import com.ender.globalmarket.data.MarketEvent;
import com.ender.globalmarket.storage.Mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarketEventData {
    public static List<MarketEvent> getALLEvents() {
        Mysql m = new Mysql();
        m.prepareSql("SELECT * FROM market_events");
        m.execute();
        ResultSet r = m.getResult();
        List<MarketEvent> data = new ArrayList<>();
        try {
            while (r.next()) {
                MarketEvent event = new MarketEvent(
                        r.getString("event_type"),
                        r.getBoolean("func"),
                        r.getDouble("percent"),
                        r.getString("item_name"),
                        r.getString("ban"),
                        r.getInt("weight")
                        );
                data.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void setMarketEvent(MarketEvent event) {
        Mysql m = new Mysql();
        m.prepareSql("INSERT INTO market_events (event_type, func, percent, item_name, ban, weight) VALUES (?, ?, ?, ?, ?, ?)");
        m.setData(1, String.valueOf(event.type));
        m.setData(2, String.valueOf(event.function));
        m.setData(3, String.valueOf(event.percent));
        m.setData(4, event.material.name());
        m.setData(5, event.ban);
        m.setData(6, String.valueOf(event.weight));
        m.execute();
        m.close();
    }

    public static void removeMarketEvent(int id) {
        Mysql m = new Mysql();
        m.prepareSql("DELETE FROM market_events WHERE id = ?");
        m.setData(1, String.valueOf(id));
        m.execute();
        m.close();
    }
}
