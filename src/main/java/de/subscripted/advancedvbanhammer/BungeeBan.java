package de.subscripted.advancedvbanhammer;

import de.subscripted.advancedvbanhammer.registry.CommandRegistry;
import de.subscripted.advancedvbanhammer.registry.ListenerRegistry;
import de.subscripted.advancedvbanhammer.sql.MySQL;
import de.subscripted.advancedvbanhammer.util.ConsolWriter;
import de.subscripted.advancedvbanhammer.util.MuteManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeBan extends Plugin {

    @Getter
    private static BungeeBan instance;

    @Getter
    private MuteManager muteManager;

    @Getter
    private String prefix;

    @Override
    public void onEnable() {
        instance = this;
        ConsolWriter.printPluginInfo();
        ConsolWriter.setupFiles();

        muteManager = new MuteManager();

        registerListener();
        registerCommands();

    }

    @Override
    public void onDisable() {
        instance = null;
        MySQL.close();
    }


    private void registerListener() {
        ListenerRegistry listenerRegistry = new ListenerRegistry();
        listenerRegistry.registerListener(instance);
    }

    private void registerCommands() {
        CommandRegistry commandRegistry = new CommandRegistry();
        commandRegistry.registerCommands(instance);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}



