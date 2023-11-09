package de.subscripted.advancedvbanhammer.listener;

import de.subscripted.advancedvbanhammer.enums.ListMessage;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.sql.SQLException;

public class PlayerLogin implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(final LoginEvent event) throws SQLException {
        final var uuid = event.getConnection().getUniqueId();

        if (BanManager.isBanned(String.valueOf(uuid))) {

            final long current = System.currentTimeMillis();
            final long end = BanManager.getEnd(uuid.toString());

            if (current < end | end == -1) {

                final String reason = String.valueOf(BanManager.getReason(uuid.toString()));

                final String remainingTime = BanManager.getRemainingTime(uuid.toString());

                String banMessage = FileManager.getlist(ListMessage.YOU_GOT_BANNED_TEMP)
                        .toString()
                        .substring(1, FileManager.getlist(ListMessage.YOU_GOT_BANNED_TEMP)
                                .toString().length() - 1)
                        .replace("%reason%", reason)
                        .replace("%time%", remainingTime)
                        .replace(", ", "\n")
                        .replace("&", "§");

                event.setCancelled(true);
            }
        }
    }
}