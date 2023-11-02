package de.subscripted.advancedvbanhammer.listener;

import de.subscripted.advancedvbanhammer.util.BanManager;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.sql.SQLException;

public class PlayerJoin implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final ServerConnectedEvent event) throws SQLException {
        final var player = event.getPlayer();
        if (BanManager.isBanned(player.getUniqueId().toString())) BanManager.unban(player.getUniqueId().toString());
    }
}
