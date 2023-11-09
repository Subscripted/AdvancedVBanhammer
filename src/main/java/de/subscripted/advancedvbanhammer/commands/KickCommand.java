package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.enums.ListMessage;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class KickCommand extends Command {

    public KickCommand() {
        super("kick", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(FileManager.getlist(ListMessage.PLAYER_KICK_DISCONNECT_SCREEN).toString()));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + "Nicht genügend Argumente. Verwendung: /kick <Spielername> <Grund>"));
            return;
        }

        String reason = args[1];
        String playername = args[0];
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playername);

        player.disconnect(TextComponent.fromLegacyText(FileManager.getMessage(ConfigMessage.PLAYER_KICK_DISCONNECT_SCREEN).replace("%reason%", reason)));
    }
}