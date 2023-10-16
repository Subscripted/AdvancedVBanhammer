package de.subscripted.advancedvbanhammer.listener;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.sql.MySQL;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.sql.SQLException;

public class PlayerJoin implements Listener {
    private Main plugin;

    public PlayerJoin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(ServerConnectedEvent event) throws SQLException {
        ProxiedPlayer p = event.getPlayer();
        if(BanManager.isBanned(p.getUniqueId().toString())){
            BanManager.unban(p.getUniqueId().toString());
        }
    }
}
