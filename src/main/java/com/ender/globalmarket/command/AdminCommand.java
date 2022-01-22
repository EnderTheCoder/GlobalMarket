package com.ender.globalmarket.command;

import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.economy.MarketData;
import com.ender.globalmarket.economy.MarketTrade;
import com.ender.globalmarket.player.EssInventory;
import com.ender.globalmarket.player.Inventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;

        if (args.length < 1) return false;

        switch (args[0]) {
            case "set": {


                MarketItem marketItem = new MarketItem();
                if (args.length == 4) {
                    if (!player.getInventory().getItemInMainHand().getType().name().equals("AIR")) {
                        marketItem.item = player.getInventory().getItemInMainHand().getType();
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]手中没有有效的物品，请检查参数个数或手中是否持有有效物品");
                        return true;
                    }
                } else if (args.length == 5){
                    marketItem.item = Material.matchMaterial(args[4]);
                    if (marketItem.item == null) {
                        sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]无法找到输入的物品，请检查名称");
                        return true;
                    }
                } else return false;

                if (!(MarketTrade.isAmountLegal(args[1]) && MarketTrade.isAmountLegal(args[2]) && MarketTrade.isAmountLegal(args[3]))) {
                    sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你输入的数字不合法");
                    return true;
                }

                marketItem.x = Integer.parseInt(args[1]);
                marketItem.k = Integer.parseInt(args[2]);
                marketItem.b = Integer.parseInt(args[3]);
                if (marketItem.b > 4 || marketItem.b < 1) {
                    sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你输入的市场稳定指数不合法，我们建议在1-4之间");
                    return true;
                }
                MarketData.putMarketItem(marketItem);
                sender.sendMessage(ChatColor.GREEN + "[GlobalMarketAdmin]成功设置新的可贸易物品" + marketItem.item.name());
                break;
            }
            case "remove": {
                if (args.length != 2) return false;
                Material material = Material.matchMaterial(args[1]);
                if (material == null) {
                    sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你输入的物品名不存在");
                    return true;
                }
                MarketItem marketItem = MarketData.getMarketItem(material);
                if (marketItem == null) {
                    sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你输入的物品不在可交易物品列表中");
                    return true;
                }
                MarketData.removeMarketItem(marketItem);
                break;
            }
            case "test": {
            }
            default: {
                return false;
            }
        }

        return true;
    }
}
