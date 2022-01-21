package com.ender.globalmarket.data;

import com.ender.globalmarket.economy.MarketTrade;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.sql.Timestamp;

public class Trade {
    public Trade(Player player, MarketTrade.type type, Material material) {
        this.player = player;
        this.type = type;
        this.material = material;
        this.setTime = new Timestamp(System.currentTimeMillis());
    }
    public Timestamp setTime;
    public Player player;
    public MarketTrade.type type;
    public Material material;
}
