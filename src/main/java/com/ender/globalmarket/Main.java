package com.ender.globalmarket;

import com.ender.globalmarket.command.UserCommand;
import com.ender.globalmarket.money.Vault;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class Main extends JavaPlugin {
    public static JavaPlugin instance;
    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getLogger().info(ChatColor.GREEN + "欢迎使用Ender's Global Market插件，加载成功");

        if (Bukkit.getPluginCommand("globalmarket") != null) {
            Bukkit.getPluginCommand("globalmarket").setExecutor(new UserCommand());
        }
        Vault.vaultSetup();
//        if (!setupEconomy() ) {
//            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
//            getServer().getPluginManager().disablePlugin(this);
//            return;
//        }
//        setupPermissions();
//        setupChat();
    }
}