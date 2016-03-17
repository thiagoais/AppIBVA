package com.vidasnoaltarmda.celulas.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by thiago on 06/03/2016.
 */
public class ConnectionManager {
    private static String url = "jdbc:mysql://a2plcpnl0304.prod.iad2.secureserver.net/celulas";
    private static String driverName = "com.mysql.jdbc.Driver";
    private static String username = "barque";
    private static String password = "1z2x3c4v";
    private static Connection con;

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(driverName);
            DriverManager.setLoginTimeout(1);
            String dbConnectionString = url+"?user="+username+"&password="+password;
            Properties properties = new Properties();
            properties.put("connectTimeout", "2000");
            con = DriverManager.getConnection(dbConnectionString, properties);
        } catch (ClassNotFoundException ex) {
            // log an exception. for example:
            System.out.println("Driver not found.");
        }
        return con;
    }
}