package de.subscripted.advancedvbanhammer;

import de.subscripted.advancedvbanhammer.commands.*;
import de.subscripted.advancedvbanhammer.listener.PlayerJoin;
import de.subscripted.advancedvbanhammer.listener.PlayerLogin;
import de.subscripted.advancedvbanhammer.sql.MySQL;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.ConsolWriter;
import de.subscripted.advancedvbanhammer.utils.DiscordWebhook;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.net.URL;
import java.util.concurrent.TimeUnit;


public class Main extends Plugin {

    @Getter
    private static Main instance;
    public static String redfooter = "https://cdn.discordapp.com/attachments/1055223755909111808/1133836888449503262/Unbdassddasadsadssaasadedsddsdsadsanannt-1.png";
    DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1153753499700572202/cGeTTjjz5vETUHxqFtVZmIpNd4Z74qAmNNch0PufIUHQX1Fa42aQEALPF1-YnmXwKV8N");
    private URL url;

    @Getter
    private String prefix;


    @Override
    public void onEnable() {

        instance = this;
        ConsolWriter.printPluginInfo();
        ConsolWriter.setupFiles();

        PluginManager pm = getProxy().getPluginManager();
        pm.registerListener(this, new PlayerLogin(this));
        pm.registerListener(this, new PlayerJoin(this));
        getProxy().getPluginManager().registerCommand(this, new BanCommand(this));
        getProxy().getPluginManager().registerCommand(this, new TempBanCommand(this));
        getProxy().getPluginManager().registerCommand(this, new CheckCommand(this));
        getProxy().getPluginManager().registerCommand(this, new UnbanCommand(this));
        getProxy().getPluginManager().registerCommand(this, new InfoCommand(this));
        getProxy().getPluginManager().registerCommand(this, new KickCommand(this));
        getProxy().getPluginManager().registerCommand(this, new WhatIsBan(this));
    }

    @Override
    public void onDisable() {
        MySQL.close();
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}



