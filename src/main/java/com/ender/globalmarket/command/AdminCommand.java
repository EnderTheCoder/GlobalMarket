package com.ender.globalmarket.command;

import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.economy.MarketData;
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
                    if (player.getInventory().getItemInMainHand().getType() != null) {
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
                marketItem.x = Integer.parseInt(args[1]);
                marketItem.k = Integer.parseInt(args[2]);
                marketItem.b = Integer.parseInt(args[3]);
                MarketData.putMarketItem(marketItem);
                sender.sendMessage(ChatColor.GREEN + "[GlobalMarketAdmin]成功设置新的可贸易物品" + marketItem.item.name());
                break;
            }
            case "remove": {

                break;
            }
            case "test": {
                ItemStack itemStack = new ItemStack(Material.ACACIA_BUTTON, 64);
                EssInventory.addAllItems(player.getInventory(), itemStack);
                break;
            }
            case "test2": {
//                ItemStack itemStack = new ItemStack(Material.ARROW, Integer.parseInt(args[1]));
                Inventory.subtractInventory(player, Material.ARROW, Integer.parseInt(args[1]));
                break;
            }
            default: {
                return false;
            }
        }

        return true;
    }
}
