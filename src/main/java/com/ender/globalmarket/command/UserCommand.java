package com.ender.globalmarket.command;

import com.ender.globalmarket.money.Vault;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

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
        switch (args[0]) {
            case "buy": {
                Player player = (Player) sender;
                EconomyResponse r = econ.depositPlayer(player, 1.05);
                sender.sendMessage(String.format("You have %s", econ.format(econ.getBalance(player.getName()))));
                sender.sendMessage("你成功购买了物品");
                break;
            }
            case "sell": {
                sender.sendMessage("你成功出售了物品");
                break;
            }
            case "money": {
                Player player = (Player) sender;
                sender.sendMessage(String.format("You have %s", Vault.checkCurrency(player.getUniqueId())));
                break;
            }
        }
        return true;
    }
}