package de.subscripted.advancedvbanhammer.utils;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.sql.MySQL;

public class ConsolWriter {

    public static void printPluginInfo() {
        System.out.println("[]=====[Enabling AdvancedVBanhammer]=====[]");
        System.out.println("| Information:");
        System.out.println("|   Name: AdvancedVBanhammer");
        System.out.println("|   Developer: Subscripted");
        System.out.println("|   Version: " + Main.getInstance().getDescription().getVersion());
        System.out.println("|   Storage: MySQL");
        System.out.println("|   License: Varilx.de");
        System.out.println("|   System: BungeeCord");
        System.out.println("[]================================[]");
    }

    public static void setupFiles() {
        System.out.println("[]=====[Enabling Configs]=====[]");
        System.out.println("| Setup files...");
        FileManager.setup(Main.getInstance());
        System.out.println("| reading config´s...");
        FileManager.readConfig();
        System.out.printf("| reading MySQL...");
        FileManager.readMySQL();
        System.out.println("| MySQL connected!");
        MySQL.connect();
        System.out.println("| MySQL Table created if not existing!");
        MySQL.createTable();
        System.out.println("[]================================[]");
    }
}
