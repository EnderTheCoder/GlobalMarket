package com.ender.globalmarket.command;

import com.ender.globalmarket.economy.MarketEconomy;
import com.ender.globalmarket.money.Vault;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.UUID;

//import javax.annotation.ParametersAreNonnullByDefault;

public class UserCommand implements CommandExecutor {
    private static final Economy econ = Vault.getEconomy();
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
                if (args.length != 3) sender.sendMessage(ChatColor.RED + "输入的参数太多或者太少，此处应为3个");

                //调试代码，记得移除
                Vault.addVaultCurrency(uuid, 0.1);

                Material itemToBuy = Material.matchMaterial(args[1]);
                if (itemToBuy == null ) {
                    sender.sendMessage(ChatColor.RED + "你输入的物品不存在，请在物品名称前加命名空间。如原版物品minecraft:..., 或者机械动力物品create:...");
                    break;
                }

                double neededMoney = MarketEconomy.getBuyingPrice(itemToBuy, Integer.parseInt(args[2]));

                if (Vault.checkCurrency(uuid) >= neededMoney) {
                    Vault.subtractCurrency(uuid, neededMoney);

                    ItemStack itemToGive = new ItemStack(itemToBuy);
                    itemToGive.setAmount(1); // 设置为 32 个

                    PlayerInventory inventory = player.getInventory();

                    inventory.addItem(itemToGive);

                    sender.sendMessage(ChatColor.GREEN + String.format("你成功购买了物品 %s", itemToBuy.name()));
                    sender.sendMessage(String.format(ChatColor.YELLOW + "你的账户余额为 %s", Vault.checkCurrency(uuid)));
                } else {
                    sender.sendMessage(ChatColor.RED + String.format("你的账户余额为 %s ， 本次购买需要 %s ， 试试如何去搞点钱来", Vault.checkCurrency(uuid), neededMoney));
                }
                break;
            }
            case "sell": {
                Vault.addVaultCurrency(uuid, MarketEconomy.getSellingPrice());
                sender.sendMessage("你成功出售了物品");
                break;
            }
            case "money": {
                sender.sendMessage(String.format("你的账户余额为 %s", Vault.checkCurrency(uuid)));
                break;
            }
        }
        return true;
    }
}