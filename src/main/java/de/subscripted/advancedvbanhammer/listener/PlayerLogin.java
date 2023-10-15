package de.subscripted.advancedvbanhammer.listener;

import java.util.UUID;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ListMessage;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import java.util.UUID;

public class PlayerLogin implements Listener {

    private Main plugin;

    public PlayerLogin(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(LoginEvent event) {
        UUID uuid = event.getConnection().getUniqueId();
        if (BanManager.isBanned(String.valueOf(uuid))) {
            long current = System.currentTimeMillis();
            long end = BanManager.getEnd(uuid.toString());
            if (current < end | end == -1) {
                event.setCancelled(true);
                String reason = BanManager.getReason(uuid.toString());
                String remainingTime = BanManager.getRemainingTime(uuid.toString());
                String banMessage = FileManager.getlist(ListMessage.YOU_GOT_BANNED_TEMP).toString().substring(1, FileManager.getlist(ListMessage.YOU_GOT_BANNED_TEMP).toString().length() - 1).replace("%reason%", reason).replace("%time%", remainingTime).replace(", ", "\n").replace("&","§");
                event.setCancelled(true);
                event.setCancelReason(banMessage);
            }
        }
    }
}