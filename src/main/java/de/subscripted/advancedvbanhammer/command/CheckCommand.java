package de.subscripted.advancedvbanhammer.command;

import de.subscripted.advancedvbanhammer.BungeeBan;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.util.BanManager;
import de.subscripted.advancedvbanhammer.util.FileManager;
import de.subscripted.advancedvbanhammer.util.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class CheckCommand extends Command {

    public CheckCommand() {
        super("check");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sender.sendMessage(TextComponent.fromLegacyText(FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE)));
            return;
        }

        switch (args.length) {
            case 1 -> {
                if (args[0].equalsIgnoreCase("list")) {
                    List<String> list = BanManager.getBannedPlayers();
                    if (list.isEmpty()) {
                        sender.sendMessage(TextComponent.fromLegacyText(BungeeBan.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.NO_ONE_IS_BANNED)));
                    }
                    for (String allBanned : BanManager.getBannedPlayers()) {
                        sender.sendMessage(TextComponent.fromLegacyText(BungeeBan.getInstance().getPrefix() + allBanned + "§9(§eGrund: §r§c " + BanManager.getReason(getUUID(allBanned)) + "§7)"));
                    }
                    return;
                }
                String playername = args[0];
                try {
                    player.sendMessage(TextComponent.fromLegacyText(BungeeBan.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.CHECK_IF_BANNED).replace("%playername%", playername) + (BanManager.isBanned(getUUID(playername)) ? FileManager.getMessage(ConfigMessage.CHECK_IS_BANNED) : FileManager.getMessage(ConfigMessage.CHECK_IS_NOT_BANNED))));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (BanManager.isBanned(getUUID(playername))) {
                        player.sendMessage(TextComponent.fromLegacyText(BungeeBan.getInstance().getPrefix() + ChatColor.YELLOW + "Grund: " + ChatColor.RED + BanManager.getReason(getUUID(playername))));
                        player.sendMessage(TextComponent.fromLegacyText(BungeeBan.getInstance().getPrefix() + ChatColor.YELLOW + "Verbleibende Zeit: " + ChatColor.RED + BanManager.getRemainingTime(getUUID(playername))));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
                player.sendMessage(TextComponent.fromLegacyText(BungeeBan.getInstance().getPrefix() + ChatColor.RED + "Verwendung: /check <nutzer>"));
                player.sendMessage(TextComponent.fromLegacyText(BungeeBan.getInstance().getPrefix() + ChatColor.RED + "Verwendung: /check list"));
            }
        }
    }

    private String getUUID(String playername) {
        ProxiedPlayer player = BungeeBan.getInstance().getProxy().getPlayer(playername);
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
