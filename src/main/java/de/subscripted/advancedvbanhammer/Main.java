package de.subscripted.advancedvbanhammer;

import de.subscripted.advancedvbanhammer.reg.CommandRegistry;
import de.subscripted.advancedvbanhammer.reg.ListenerRegistry;
import de.subscripted.advancedvbanhammer.sql.MySQL;
import de.subscripted.advancedvbanhammer.utils.ConsolWriter;
import de.subscripted.advancedvbanhammer.utils.MuteManager;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    @Getter
    private static Main instance;

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