package com.ender.globalmarket.player;

import com.ender.globalmarket.storage.ConfigReader;
import com.ender.globalmarket.storage.Mysql;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRegData {
    public static boolean isVIP(Player player) {
        if (!ConfigReader.getEnableNoTax()) return false;

        Mysql m = new Mysql();
        m.prepareSql("SELECT level FROM user WHERE username = ?");
        m.setData(1, player.getName());
        m.execute();
        ResultSet resultSet = m.getResult();
        try {
            resultSet.next();
            return resultSet.getInt("level") > 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        m.close();
        return false;
    }
}
