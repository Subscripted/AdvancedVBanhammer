package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import de.subscripted.advancedvbanhammer.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.UUID;

public class UnbanCommand extends Command {

    public UnbanCommand() {
        super("unban");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sender.sendMessage(TextComponent.fromLegacyText(FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE)));
            return;
        }

        if (args.length == 1) {
            String playername = args[0];
            try {
                if (!BanManager.isBanned(getUUID(playername))) {
                    player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.PLAYER_IS_NOT_BANNED).replace("%playername%", playername)));
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.UNBANNED_PLAYER).replace("%playername%", playername)));
            BanManager.unban(getUUID(playername));
            return;
        }
        player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.UNBAN_USAGE)));
    }

    private String getUUID(String playername) {
        ProxiedPlayer player = Main.getInstance().getProxy().getPlayer(playername);
        if (player != null) {
            return player.getUniqueId().toString();
        } else {
            UUID uuid = UUIDFetcher.getUUID(playername);
            if (uuid != null) {
                return uuid.toString();
            } else {
                return null;
            }
        }
    }
}