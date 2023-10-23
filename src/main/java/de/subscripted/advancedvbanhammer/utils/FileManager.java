package de.subscripted.advancedvbanhammer.utils;


import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.enums.ListMessage;
import de.subscripted.advancedvbanhammer.enums.Permissions;
import de.subscripted.advancedvbanhammer.sql.MySQL;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileManager {

    private static File configFile;
    private static File mysqlFile;
    private static File banIdsFile;
    private static File tempBanIdsFile;
    private static Configuration config;
    private static Configuration mysqlConfig;
    private static Configuration banIdsConfig;
    private static Configuration tempBanIdConfig;

    public static void setup(Plugin plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        mysqlFile = new File(plugin.getDataFolder(), "mysql.yml");
        banIdsFile = new File(plugin.getDataFolder(), "banids.yml");
        tempBanIdsFile = new File(plugin.getDataFolder(), "tbanid.yml");


        if (!tempBanIdsFile.exists()) {
            try {
                tempBanIdsFile.getParentFile().mkdirs();
                Files.copy(plugin.getResourceAsStream("tbanid.yml"), tempBanIdsFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        if (!configFile.exists()) {
            try {
                configFile.getParentFile().mkdirs();
                Files.copy(plugin.getResourceAsStream("config.yml"), configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!mysqlFile.exists()) {
            try {
                mysqlFile.getParentFile().mkdirs();
                Files.copy(plugin.getResourceAsStream("mysql.yml"), mysqlFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!banIdsFile.exists()) {
            try {
                banIdsFile.getParentFile().mkdirs();
                Files.copy(plugin.getResourceAsStream("banids.yml"), banIdsFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            mysqlConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(mysqlFile);
            banIdsConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(banIdsFile);
            tempBanIdConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(tempBanIdsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDefaultConfig();
        setDefaultMySQL();
        setDefaultTempBanIdConfig();
    }

    public static Configuration getConfig() {
        return config;
    }

    public static Configuration getMySQLConfig() {
        return mysqlConfig;
    }

    public static Configuration getBanIdsConfig() {
        return banIdsConfig;
    }

    public static Configuration getTempBanIdConfig() {
        return tempBanIdConfig;
    }

    public static void setDefaultConfig() {
        Configuration cfg = getConfig();

        // Prefix

        if (!cfg.contains("prefix")) {
            cfg.set("prefix", "&a&lVarilx &7&l· ");

            //Messages

        }
        if (!cfg.contains("message.player_not_found")) {
            cfg.set("message.player_not_found", "&cDer von dir angegebene Spieler wurde nicht gefunden!");
        }
        if (!cfg.contains("message.player_is_banned")) {
            cfg.set("message.player_is_banned", "&c %playername% ist bereits gebannt! ");
        }
        if (!cfg.contains("message.temp_ban_usage")) {
            cfg.set("message.temp_ban_usage", "&cVerwendung: /tempban <nutzer> <zeit> <einheit> <ban-id> <grund>");
        }
        if (!cfg.contains("message.reload_success")) {
            cfg.set("message.reload_success", "&aBanManager-Konfigurationen wurden erfolgreich neu geladen.");
        }
        if (!cfg.contains("message.player_ban")) {
            cfg.set("message.player_ban", "&7Du hast %playername% &cPERMANENT &7gebannt!");
        }
        if (!cfg.contains("message.ban_usage")) {
            cfg.set("message.ban_usage", "&cVerwendung: /ban <nutzer> <ban-id>");
        }
        if (!cfg.contains("message.wrong_ban_id")) {
            cfg.set("message.wrong_ban_id", "&cUngültige Ban-ID angegeben.");
        }
        if (!cfg.contains("message.sender_is_console")) {
            cfg.set("message.sender_is_console", "&cDieser befehl kann nur von einem Spieler ausgeführt werden!.");
        }
        if (!cfg.contains("message.must_be_a_number")) {
            cfg.set("message.must_be_a_number", "&cWert muss eine Zahl sein!");
        }
        if (!cfg.contains("message.to_big_number")) {
            cfg.set("message.to_big_number", "&cDieser Wert ist zu Groß!");
        }
        if (!cfg.contains("message.temp_ban_message")) {
            cfg.set("message.temp_ban_message", "&7Du hast &e%playername% &efür &e&l%time% %unit% &7gebannt!");
        }
        if (!cfg.contains("message.unit_not_existing")) {
            cfg.set("message.unit_not_existing", "&cDiese Einheit existiert nicht!");
        }
        if (!cfg.contains("message.no_one_is_banned")) {
            cfg.set("message.no_one_is_banned", "&cEs ist niemand gebannt!");
        }
        if (!cfg.contains("message.player_is_not_banned")) {
            cfg.set("message.player_is_not_banned", "&e%playername% &7ist nicht gebannt!");
        }
        if (!cfg.contains("message.check_if_banned")) {
            cfg.set("message.check_if_banned", "&7Name: &c%playername%§ " + "\n" + "&7Gebannt: ");
        }
        if (!cfg.contains("message.unbanned_player")) {
            cfg.set("message.unbanned_player", "&7Du hast &c%playername%§ &7entbann!");
        }
        if (!cfg.contains("message.unban_usage")) {
            cfg.set("message.unban_usage", "&cBenutze /unban <playername>");
        }
        if (!cfg.contains("message.check_is_banned")) {
            cfg.set("message.check_is_banned", "&cGebannt");
        }
        if (!cfg.contains("message.check_is_not_banned")) {
            cfg.set("message.check_is_not_banned", "&aNicht gebannt!");
        }
        if (!cfg.contains("message.player_banned_broadcast")) {
            cfg.set("message.player_banned_broadcast", "&c%player% &7hat &c%playername% &7für %reason% &7gebannt! &cPERMANENT");
        }
        if (!cfg.contains("message.player_temp_banned_broadcast")) {
            cfg.set("message.player_temp_banned_broadcast", "&c%player% &7hat &c%playername% &7für %reason% &7gebannt! &c[Zeit: &e%time&c &e%unit%]");
        }
        if (!cfg.contains("message.kick_usage")) {
            cfg.set("message.kick_usage", "&c/kick <playername> <reason>");
        }


        // Permissions

        if (!cfg.contains("permissions.permission_all")) {
            cfg.set("permissions.permission_all", "bancontroller.*");
        }
        if (!cfg.contains("permissions.permission_tempban")) {
            cfg.set("permissions.permission_tempban", "bancontroller.tempban");
        }
        if (!cfg.contains("permissions.permission_ban")) {
            cfg.set("permissions.permission_ban", "bancontroller.ban");
        }
        if (!cfg.contains("permissions.permission_check_user")) {
            cfg.set("permissions.permission_check_user", "bancontroller.chekuser");
        }
        if (!cfg.contains("permissions.permission_seebanbroadcast")) {
            cfg.set("permissions.permission_seebanbroadcast", "bancontroller.seebanbroadcast");
        }

        //Lists
        List<String> kicklist = new ArrayList<>();
        kicklist.add("&7Du wurdest gekickt!");
        kicklist.add("&7Du wurdest wegen &c%reason% &7gekickt!");
        if (!cfg.contains("lists.player_kick_disconnect_screen")) {
            cfg.set("lists.player_kick_disconnect_screen", kicklist);
        }

        List<String> banlist = new ArrayList<>();
        banlist.add("&c&lGebannt!");
        banlist.add("&7Du wurdest wegen &c%reason% &7gebannt!");
        banlist.add("&7Verbleibende Zeit: &c%time%");
        if (!cfg.contains("lists.you_got_banned_temp")) {
            cfg.set("lists.you_got_banned_temp", banlist);
        }

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void readConfig() {
        Main.getInstance().setPrefix(ChatColor.translateAlternateColorCodes('&', config.getString("prefix") + " §r"));
    }

    public static void setDefaultMySQL() {
        Configuration cfg = getMySQLConfig();

        if (!cfg.contains("username")) {
            cfg.set("username", "root");
        }

        if (!cfg.contains("password")) {
            cfg.set("password", "password");
        }

        if (!cfg.contains("database")) {
            cfg.set("database", "localhost");
        }

        if (!cfg.contains("host")) {
            cfg.set("host", "localhost");
        }

        if (!cfg.contains("port")) {
            cfg.set("port", "3306");
        }

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, mysqlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setDefaultTempBanIdConfig() {
        Configuration cfg = getTempBanIdConfig();

        if (!cfg.contains("temp-ban-ids")) {
            cfg.set("temp-ban-ids.1.name", "Beleidigung");
            cfg.set("temp-ban-ids.1.time", "12h");

            cfg.set("temp-ban-ids.2.name", "Spam");
            cfg.set("temp-ban-ids.2.time", "1h");

            cfg.set("temp-ban-ids.3.name", "Werbung");
            cfg.set("temp-ban-ids.3.time", "2w");

            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, tempBanIdsFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readMySQL() {
        MySQL.username = mysqlConfig.getString("username");
        MySQL.password = mysqlConfig.getString("password");
        MySQL.database = mysqlConfig.getString("database");
        MySQL.host = mysqlConfig.getString("host");
        MySQL.port = mysqlConfig.getString("port");
    }

    public static void reloadPrefix(Main plugin) {
        Configuration cfg = getConfig();
        if (cfg.contains("prefix")) {
            String prefix = ChatColor.translateAlternateColorCodes('&', cfg.getString("prefix") + " §r");
            plugin.setPrefix(prefix);
        }
    }

    public static String getMessage(ConfigMessage configMessage) {
        return config.getString("message." + configMessage.name().toLowerCase()).replace("&", "§");
    }

    public static String getPermission(Permissions configPermission) {
        return Objects.requireNonNull(config.getString("permissions." + configPermission.name().toLowerCase())).replace("&", "§");
    }

    public static List<String> getlist(ListMessage listMessage) {
        return config.getStringList("lists." + listMessage.name().toLowerCase());
    }
}
