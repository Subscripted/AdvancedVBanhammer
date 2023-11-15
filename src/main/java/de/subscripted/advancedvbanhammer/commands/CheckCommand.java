package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.enums.ListMessage;
import de.subscripted.advancedvbanhammer.enums.Permissions;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import de.subscripted.advancedvbanhammer.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CheckCommand extends Command {

    private static final String CHECK_COMMAND_NAME = "check";
    private static final String KICK_PERMISSION = FileManager.getPermission(Permissions.PERMISSION_KICK);
    private static final String ALL_PERMISSION = FileManager.getPermission(Permissions.PERMISSION_ALL);

    public CheckCommand() {
        super(CHECK_COMMAND_NAME);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sender.sendMessage(TextComponent.fromLegacyText(FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE)));
            return;
        }

        /*

        if (!sender.hasPermission(KICK_PERMISSION) || !sender.hasPermission(ALL_PERMISSION)){
            sender.sendMessage("No Permissions");
            return;
        }


         */

        switch (args.length) {
            case 1 -> {
                if (args[0].equalsIgnoreCase("list")) {
                    List<String> list = BanManager.getBannedPlayers();
                    if (list.isEmpty()) {
                        sender.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.NO_ONE_IS_BANNED)));
                    }
                    for (String allBanned : BanManager.getBannedPlayers()) {
                        sender.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.GET_ALL_BANNED_WITH_REASON).replace("%bannedPlayer%", allBanned).replace("%reason%", BanManager.getReason(getUUID(allBanned)))));
                    }
                    return;
                }
                String PLAYER_NAME = args[0];
                try {
                    player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.CHECK_IF_BANNED).replace("%playername%", PLAYER_NAME) + (BanManager.isBanned(getUUID(PLAYER_NAME)) ? FileManager.getMessage(ConfigMessage.CHECK_IS_BANNED) : FileManager.getMessage(ConfigMessage.CHECK_IS_NOT_BANNED))));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (BanManager.isBanned(getUUID(PLAYER_NAME))) {
                        player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getlist(ListMessage.ALL_BANNED_REASON_AND_TIME)).toString().replace("%reason%", BanManager.getReason(getUUID(PLAYER_NAME)).replace("%time%", BanManager.getRemainingTime(getUUID(PLAYER_NAME)))));
                        player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.PLAYER_BAN_STATUS_CHECK_WHO_BANNED).replace("%bannername%", BanManager.getWhoBanned(getUUID(PLAYER_NAME))).replace("%playername%", PLAYER_NAME)));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
                player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + ChatColor.RED + "Verwendung: /check <nutzer>"));
                player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + ChatColor.RED + "Verwendung: /check list"));
            }
        }
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