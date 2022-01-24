package com.ender.globalmarket.random.subhandler;

import com.ender.globalmarket.data.MarketEvent;
import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.economy.MarketData;
import com.ender.globalmarket.random.RandomEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class MarketXHandler implements RandomEventHandler {

    @Override
    public void run(MarketEvent event) {
        MarketItem marketItem = MarketData.getMarketItem(event.material);

        if (marketItem == null) return;
        //广播全服
        Bukkit.broadcast(ChatColor.DARK_GREEN + "[GlobalMarket]" + event.ban, "globalmarket.broadcast");

        double temp = marketItem.x;
        temp *= 1 + (event.function ? event.percent : - event.percent);
        marketItem.x = (int) temp;
        MarketData.updateMarketItem(marketItem);
    }

}
