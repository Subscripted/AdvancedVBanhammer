package de.subscripted.advancedvbanhammer.utils;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ListMessage;
import de.subscripted.advancedvbanhammer.sql.MySQL;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BanManager {
    private Main plugin;

    public BanManager(Main plugin) {
        this.plugin = plugin;
    }

    public static void ban(String uuid, String playername, String reason, long seconds) {
        long end = (seconds == -1) ? -1 : System.currentTimeMillis() + (seconds * 1000);
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playername);
        String IP = null;

        try (PreparedStatement statement = MySQL.getConnection().prepareStatement("INSERT INTO BannedPlayers (Spielername, UUID, Ende, Grund) VALUES (?, ?, ?, ?)")) {
            statement.setString(1, playername);
            statement.setString(2, uuid);
            statement.setLong(3, end);
            statement.setString(4, reason);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (player != null) {
            String banMessage = FileManager.getlist(ListMessage.YOU_GOT_BANNED_TEMP).toString().substring(1, FileManager.getlist(ListMessage.YOU_GOT_BANNED_TEMP).toString().length() - 1).replace("%reason%", reason).replace("%time%", getRemainingTime(uuid)).replace(", ", "\n").replace("&", "§");
            player.disconnect(banMessage);
            sendBanWebhook(playername, player.getName(), reason, getRemainingTime(uuid));
        }
    }

    public static void unban(String uuid) {
        try (PreparedStatement statement = MySQL.getConnection().prepareStatement("DELETE FROM BannedPlayers WHERE UUID = ?")) {
            statement.setString(1, uuid);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isBanned(String uuid) throws SQLException {
        try (PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT Ende FROM BannedPlayers WHERE UUID = ?")) {
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
    }


    public static long getEnd(String uuid) {
        try (PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT Ende FROM BannedPlayers WHERE UUID = ?")) {
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getLong("Ende");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static List<String> getBannedPlayers() {
        List<String> list = new ArrayList<>();
        try (PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT Spielername FROM BannedPlayers")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("Spielername"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getRemainingTime(String uuid) {
        long current = System.currentTimeMillis();
        long end = getEnd(uuid);

        if (end == -1) {
            return "§cPERMANENT";
        }

        long millis = end - current;
        long seconds = millis / 1000;
        long minutes = (seconds / 60) % 60;
        long hours = (seconds / 3600) % 24;
        long days = (seconds / 86400) % 7;
        long weeks = seconds / 604800;

        StringBuilder remainingTime = new StringBuilder();

        if (weeks > 0) {
            remainingTime.append("§e").append(weeks).append(" Woche(n) ");
        }
        if (days > 0) {
            remainingTime.append("§e").append(days).append(" Tag(e) ");
        }
        if (hours > 0) {
            remainingTime.append("§e").append(hours).append(" Stunde(n) ");
        }
        if (minutes > 0) {
            remainingTime.append("§e").append(minutes).append(" Minute(n) ");
        }
        if (seconds % 60 > 0) {
            remainingTime.append("§e").append(seconds % 60).append(" Sekunde(n) ");
        }

        return remainingTime.toString().trim();
    }

    private static void sendBanWebhook(String bannedPlayer, String bannedBy, String reason, String remainingTime) {
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1065969605471911976/7q8WWLvQx2Io-03BAl3zUzAAQL8BOqBjE9eFooguPKw8mCHdYqRDDLgsiKvEdL9PMqCL");
        webhook.addEmbedObjects(new DiscordWebhook.EmbedObject()
                .setTitle("Varilx Ban")
                .setDescription("- **Gebannt: **" + bannedPlayer + "\n" +
                        "- **Gebannt von: **" + bannedBy + "\n" +
                        "- **Grund: **" + reason + "\n" +
                        "- **Zeit: **" + remainingTime + "\n"));

        try {
            webhook.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkTempBans() {
        long currentTime = System.currentTimeMillis();

        try (PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT UUID, Ende FROM BannedPlayers WHERE Ende > 0")) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String uuid = rs.getString("UUID");
                long endTime = rs.getLong("Ende");

                if (currentTime >= endTime) {
                    unban(uuid);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static String getReason(String uuid) {
        try (PreparedStatement statement = MySQL.getConnection().prepareStatement("SELECT Grund FROM BannedPlayers WHERE UUID = ?")) {
            statement.setString(1, uuid);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getString("Grund");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

}