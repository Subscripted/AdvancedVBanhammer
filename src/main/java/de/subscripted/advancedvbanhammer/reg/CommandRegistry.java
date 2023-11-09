package de.subscripted.advancedvbanhammer.reg;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.commands.*;

public class CommandRegistry {

    public void registerCommands(Main instance) {
        instance.getLogger().info("Initializing Commands!");
        instance.getProxy().getPluginManager().registerCommand(instance, new BanCommand());
        instance.getProxy().getPluginManager().registerCommand(instance, new TempBanCommand());
        instance.getProxy().getPluginManager().registerCommand(instance, new CheckCommand());
        instance.getProxy().getPluginManager().registerCommand(instance, new UnbanCommand());
        instance.getProxy().getPluginManager().registerCommand(instance, new InfoCommand());
        instance.getProxy().getPluginManager().registerCommand(instance, new KickCommand());
        instance.getProxy().getPluginManager().registerCommand(instance, new WhatIsBan());
        instance.getProxy().getPluginManager().registerCommand(instance, new MuteCommand());
    }
}