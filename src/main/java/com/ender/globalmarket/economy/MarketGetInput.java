package com.ender.globalmarket.economy;

import com.ender.globalmarket.data.Trade;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MarketGetInput {

    public static HashMap<Player, Trade> isOnInput = new HashMap<>();//判断玩家是否在交易状态
    public static void message(MarketTrade.type type, Player player) {
        switch (type) {
            case BUY: {
                player.sendMessage(ChatColor.AQUA + "[GlobalMarket]请按下T并输入你想购买的数量");
                break;
            }
            case SELL: {
                player.sendMessage(ChatColor.AQUA + "[GlobalMarket]请按下T并输入你想出售的数量，输入all可出售身上所有物品");
                break;
            }
        }
    }
}
