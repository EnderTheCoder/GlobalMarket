package com.ender.globalmarket.storage;

import com.ender.globalmarket.Main;
import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigReader {

    static FileConfiguration config = Main.instance.getConfig();

    public static String getMysqlConfig(String mysqlConfigTag) {
        return config.getString(mysqlConfigTag);
    }

    public static double getTaxRate() {
        return config.getDouble("TaxRate");
    }


}