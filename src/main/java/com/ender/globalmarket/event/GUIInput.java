package com.ender.globalmarket.event;

import com.alibaba.fastjson.JSON;
import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.data.Trade;
import com.ender.globalmarket.economy.MarketData;
import com.ender.globalmarket.economy.MarketEconomy;
import com.ender.globalmarket.economy.MarketGetInput;
import com.ender.globalmarket.economy.MarketTrade;
import com.ender.globalmarket.player.Inventory;
import com.ender.globalmarket.storage.ConfigReader;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.sql.Timestamp;
import java.util.Objects;

import static org.bukkit.Bukkit.getLogger;

public class GUIInput implements Listener {
    @EventHandler
    public void onInput(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Trade trade = MarketGetInput.isOnInput.get(event.getPlayer());
        if (trade != null && ConfigReader.getTimeOut("Trade") + trade.setTime.getTime() / 1000 > (new Timestamp(System.currentTimeMillis()).getTime()) /1000 ) {//判断交易是否超时，是否存在
            event.setCancelled(true);
            MarketGetInput.isOnInput.put(player, null);
            int amount;
            MarketItem marketItem = MarketData.getMarketItem(trade.material);

            if (trade.type == MarketTrade.type.SELL && event.getMessage().equals("all")) {//如果用户输入all就出售所有物品
                amount = Inventory.calcInventory(player, trade.material);
            } else {
                if (!MarketTrade.isAmountLegal(event.getMessage())) {
                    player.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你输入了一个不合法的数量");
                    return;
                }
                amount = Integer.parseInt(event.getMessage());
            }
            if (trade.type == MarketTrade.type.BUY) {
                if (!MarketTrade.isMarketXEnough(Objects.requireNonNull(MarketData.getMarketItem(trade.material)), amount)) {
                    player.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你输入的数量超过了当前的库存量");
                    return;
                }
                if (!MarketTrade.isAffordFor(player, MarketData.getMarketItem(trade.material), amount)) {
                    player.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你的账户余额不足以进行此次购买");
                    return;
                }
                if (!MarketTrade.isEnoughSlot(player, amount, trade.material)) {
                    player.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你的背包容量不足以进行此次购买");
                    return;
                }
            }
            if (trade.type == MarketTrade.type.SELL) {
                if (!MarketTrade.isPlayerItemEnough(player, trade.material, amount)) {
                    player.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你背包中的物品不足以支付你输入的数量");
                    return;
                }
            }
            MarketTrade.trade(trade.player, marketItem, amount, trade.type);//创建交易
//            MarketTrade.message(player, trade.type, trade.material, amount, MarketEconomy.getBuyingPrice(marketItemCopy, amount), MarketTrade.getTax(player, MarketEconomy.getBuyingPrice(MarketData.getMarketItem(trade.material), amount)));
        }
    }
}
