package com.ender.globalmarket.storage;

import com.ender.globalmarket.Main;
import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigReader {

    public static FileConfiguration config = Main.instance.getConfig();
    // 由于三个方法都要使用，我们将这个变量抽取出来到最外层

    public static boolean isPlayerRegistered(String playerName) {

        return config.contains(playerName.toLowerCase());
    }

    public static int getMoney(String playerName) {
        return (int) config.get("a");
    }

    public static boolean setMoney(String playerName) {
        return true;
    }
}