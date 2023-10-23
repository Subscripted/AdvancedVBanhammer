package de.subscripted.advancedvbanhammer.listener;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.utils.MuteManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    private MuteManager muteManager;

    public ChatListener(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer p = (ProxiedPlayer) event.getSender();
        if (muteManager.isMuted(p.getUniqueId())) {
            event.setCancelled(true);
            p.sendMessage("Du bist gemutet und kannst nicht schreiben.");
        }
    }
}
