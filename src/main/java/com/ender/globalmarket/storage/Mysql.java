package com.ender.globalmarket.storage;

import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;

import static org.bukkit.Bukkit.getLogger;

public class Mysql {


    private static Connection connection;
    private PreparedStatement statement;

    public boolean mysqlInit() {


        String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";


        // 连接参数的固定格式
        String DB_URL =
                "jdbc:mysql://" + ConfigReader.getMysqlConfig("Mysql.Address") + ":" +
                        ConfigReader.getMysqlConfig("Mysql.Port") +
                        "/" +
                        ConfigReader.getMysqlConfig("Mysql.DataBaseName") +
                        "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        try {
            // 驱动名称
            Class.forName(JDBC_DRIVER); // forName 又来了！
            connection = DriverManager.getConnection(DB_URL, ConfigReader.getMysqlConfig("Mysql.Username"), ConfigReader.getMysqlConfig("Mysql.password"));
//            getLogger().info(ChatColor.GREEN + "Mysql successfully connected.");

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }



    public void prepareSql(String sql) {
        try {
            statement = getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            getLogger().warning(ChatColor.RED + "An error in mysql occurred while preparing sql.");
            e.printStackTrace();
        }
    }

    public void setData(Integer number,String data) {
        try {
            statement.setString(number, data);
        } catch (SQLException e) {
            getLogger().warning(ChatColor.RED + "An error in mysql occurred while binding data for sql.");
            e.printStackTrace();
        }
    }

    public void execute() {
        try {
            statement.execute();
        } catch (SQLException e) {
            getLogger().warning(ChatColor.RED + "An error in mysql occurred while executing sql.");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResult() {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            getLogger().warning(ChatColor.RED + "An error in mysql occurred while getting sql result.");
            e.printStackTrace();
            return null;
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || !connection.isValid(1000)) {
//                getLogger().info(ChatColor.RED + "Mysql connection is now closed. Trying to creating a new one.");
                mysqlInit();
            }
            return connection;

        } catch (SQLException e) {
            return null;
        }
    }
}

