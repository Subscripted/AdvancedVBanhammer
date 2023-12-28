package de.subscripted.advancedvbanhammer.sql;

import de.subscripted.advancedvbanhammer.Main;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.md_5.bungee.api.ProxyServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;


@Getter
@Setter
public class MySQL {

    private static Connection con;


    public static String username;
    public static String password;
    public static String database;
    public static String host;
    public static String port;


    public static CompletableFuture<Connection> getConnection() {
        return CompletableFuture.supplyAsync(() -> {
            if (!isConnected()) {
                connect();
            }
            return con;
        });
    }

    @SneakyThrows
    public static void connect() {
        if (isConnected()) {
            return;
        }

        con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
        ProxyServer.getInstance().getConsole().sendMessage(Main.getInstance().getPrefix() + "MySQL Verbindung erstellt!");
    }

    @SneakyThrows
    public static void close() {
        if (isConnected()) {
            con.close();
            ProxyServer.getInstance().getConsole().sendMessage(Main.getInstance().getPrefix() + "MySQL Verbindung geschlossen!");
        }
    }

    public static boolean isConnected() {
        return con != null;
    }

    @SneakyThrows
    public static void createTable() {
        if (isConnected()) {
            con.createStatement().executeUpdate("CREATE TABLE IF NOT EXISTS BannedPlayers (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), Sender VARCHAR(100))");
        }
    }

    @SneakyThrows
    public static void update(String query) {
        if (isConnected()) {
            con.createStatement().executeUpdate(query);
        }
    }
}
