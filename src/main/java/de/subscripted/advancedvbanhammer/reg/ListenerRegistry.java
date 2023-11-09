package de.subscripted.advancedvbanhammer.reg;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.listener.ChatListener;
import de.subscripted.advancedvbanhammer.listener.PlayerJoin;
import de.subscripted.advancedvbanhammer.listener.PlayerLogin;

public class ListenerRegistry {
    public void registerListener(Main instance) {
        instance.getLogger().info("Initializing Listener!");
        instance.getProxy().getPluginManager().registerListener(instance, new PlayerLogin());
        instance.getProxy().getPluginManager().registerListener(instance, new PlayerJoin());
        instance.getProxy().getPluginManager().registerListener(instance, new ChatListener());
    }
}