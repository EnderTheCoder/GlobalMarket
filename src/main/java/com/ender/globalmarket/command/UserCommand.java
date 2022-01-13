package com.ender.globalmarket.command;

import com.ender.globalmarket.Main;
import com.ender.globalmarket.data.MarketItem;
import com.ender.globalmarket.economy.MarketData;
import com.ender.globalmarket.economy.MarketEconomy;
import com.ender.globalmarket.money.Vault;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;

//import javax.annotation.ParametersAreNonnullByDefault;

public class UserCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        if (args.length == 0) {
            // 参数数量太少，拒绝处理
            return false;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();
        switch (args[0]) {
            case "buy": {
//                if (args.length != 3) sender.sendMessage(ChatColor.RED + "输入的参数太多或者太少，此处应为3个");
//
//                //调试代码，记得移除
//                Vault.addVaultCurrency(uuid, 0.1);
//
//                Material itemToBuy = Material.matchMaterial(args[1]);
//                if (itemToBuy == null) {
//                    sender.sendMessage(ChatColor.RED + "你输入的物品不存在，请在物品名称前加命名空间。如原版物品minecraft:..., 或者机械动力物品create:...");
//                    break;
//                }
//                double neededMoney = MarketEconomy.getBuyingPrice(itemToBuy, Integer.parseInt(args[2]));
//
//                if (Vault.checkCurrency(uuid) >= neededMoney) {
//                    Vault.subtractCurrency(uuid, neededMoney);
//
//                    ItemStack itemToGive = new ItemStack(itemToBuy);
//                    itemToGive.setAmount(1); // 设置为 32 个
//
//                    PlayerInventory inventory = player.getInventory();
//
//                    inventory.addItem(itemToGive);
//
//                    sender.sendMessage(ChatColor.GREEN + String.format("你成功购买了物品 %s", itemToBuy.name()));
//                    sender.sendMessage(String.format(ChatColor.YELLOW + "你的账户余额为 %s", Vault.checkCurrency(uuid)));
//                } else {
//                    sender.sendMessage(ChatColor.RED + String.format("你的账户余额为 %s ， 本次购买需要 %s ， 试试如何去搞点钱来", Vault.checkCurrency(uuid), neededMoney));
//                }
                break;
            }
            case "sell": {
                if (player.getInventory().getItemInMainHand().getType() != null) {
                        MarketItem marketItem = null;
                        marketItem = MarketData.getMarketItem(player.getInventory().getItemInMainHand().getType());

                } else {
                    sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]你需要将需要出售的物品拿在主手中");
                }

                sender.sendMessage("你成功出售了物品");
                break;
            }
            case "money": {
                sender.sendMessage(String.format("你的账户余额为 %s", Vault.checkCurrency(uuid)));
                break;
            }
            case "calc": {
                if (args.length != 4) return false;
                Material item = Material.matchMaterial(args[2]);
                if (item == null) {
                    sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]未找到查询的物品，请检查拼写格式是否为例如minecraft:diamond的样式");
                    return true;
                }
                MarketItem marketItem = MarketData.getMarketItem(item);
                if (marketItem == null) {
                    sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]未找到查询的物品，请检查该物品是否允许交易");
                    return true;
                }

                switch (args[1]) {
                    case "buy": {
                        if (marketItem.x > Integer.parseInt(args[3])) {
                            sender.sendMessage(ChatColor.YELLOW + "[GlobalMarket]输入的数量超过当前市场存量，无法计算");
                            return true;
                        }
                        double price = MarketEconomy.getBuyingPrice(marketItem, Integer.parseInt(args[3]));
                        double tax = MarketEconomy.getTax(price);

                        sender.sendMessage(ChatColor.AQUA + "[GlobalMarket]您查询的物品" + args[2] + "购买" + args[3] + "个的价格是$" + price + ", 同时收取贸易税$" + tax);
                        break;
                    }
                    case "sell": {
                        double price = MarketEconomy.getSellingPrice(marketItem, Integer.parseInt(args[3]));
                        double tax = MarketEconomy.getTax(price);

                        sender.sendMessage(ChatColor.AQUA + "[GlobalMarket]您查询的物品" + args[2] + "出售" + args[3] + "个的价格是$" + price + ", 同时收取贸易税$" + tax);
                        break;
                    }
                    default: {
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }
}