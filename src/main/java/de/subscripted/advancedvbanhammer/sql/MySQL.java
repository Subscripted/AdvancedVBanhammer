package de.subscripted.advancedvbanhammer.sql;

import de.subscripted.advancedvbanhammer.BungeeBan;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class MySQL {


    public static String username;
    public static String password;
    public static String database;
    public static String host;
    public static String port;
    private static Connection con;

    public static CompletableFuture<Connection> getConnection() {
        return CompletableFuture.supplyAsync(() -> {
            if (!isConnected()) {
                connect();
            }
            return con;
        });
    }

    public static void connect() {
        if (isConnected()) {
            return;
        }

        try {
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
            ProxyServer.getInstance().getConsole().sendMessage(BungeeBan.getInstance().getPrefix() + "MySQL Verbindung erstellt!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        if (isConnected()) {
            try {
                con.close();
                ProxyServer.getInstance().getConsole().sendMessage(BungeeBan.getInstance().getPrefix() + "MySQL Verbindung geschlossen!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isConnected() {
        return con != null;
    }

    public static void createTable() {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS BannedPlayers (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100))");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void update(String query) {
        if (isConnected()) {
            try {
                con.createStatement().executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
