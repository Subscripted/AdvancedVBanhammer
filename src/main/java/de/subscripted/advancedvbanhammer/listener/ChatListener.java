package de.subscripted.advancedvbanhammer.listener;

import de.subscripted.advancedvbanhammer.Main;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;


public class ChatListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent event) {
        final var player = (ProxiedPlayer) event.getSender();
        if (Main.getInstance().getMuteManager().isMuted(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(TextComponent.fromLegacyText("Du bist gemutet und kannst nicht schreiben."));
        }
    }
}