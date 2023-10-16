package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.BanUnit;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.enums.Permissions;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import de.subscripted.advancedvbanhammer.utils.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TempBanCommand extends Command implements TabExecutor {

    private Main plugin;

    public TempBanCommand(Main plugin) {
        super("tempban");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length >= 2) {
            String playername = args[0];
            try {
                if (BanManager.isBanned(getUUID(playername))) {
                    player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.PLAYER_IS_BANNED).replace("%playername%", playername));
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            int banId;
            try {
                banId = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.WRONG_BAN_ID));
                return;
            }

            Configuration banIdsConfig = FileManager.getTempBanIdConfig();
            String banIdStr = String.valueOf(banId);
            String banReason = banIdsConfig.getString("temp-ban-ids." + banIdStr + ".name");
            String banTimeString = banIdsConfig.getString("temp-ban-ids." + banIdStr + ".time");

            if (banReason != null && banTimeString != null) {
                long time = parseTime(banTimeString);
                if (time > 0) {
                    BanManager.ban(getUUID(playername), playername, banReason, time);
                    player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.TEMP_BAN_MESSAGE)
                            .replace("%playername%", playername)
                            .replace("%time%", banTimeString)
                            .replace("%unit%", ""));
                    for (ProxiedPlayer onlinePlayer : ProxyServer.getInstance().getPlayers()) {
                        if (onlinePlayer.hasPermission(FileManager.getPermission(Permissions.PERMISSION_SEEBANBROADCAST))) {
                            onlinePlayer.sendMessage(FileManager.getMessage(ConfigMessage.PLAYER_TEMP_BANNED_BROADCAST)
                                    .replace("%playername%", playername)
                                    .replace("%reason%", banReason)
                                    .replace("%player%", player.getDisplayName())
                                    .replace("%time%", banTimeString));
                        }
                    }
                } else {
                    player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.UNIT_NOT_EXISTING));
                }
            } else {
                player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.WRONG_BAN_ID));
            }
            return;
        }
        player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.TEMP_BAN_USAGE));
    }

    private long parseTime(String timeString) {
        try {
            int value = Integer.parseInt(timeString.replaceAll("[^0-9]", ""));
            String unit = timeString.replaceAll("[^a-zA-Z]", "").toLowerCase();
            BanUnit banUnit = BanUnit.getUnit(unit);
            if (banUnit != null) {
                return value * banUnit.getToSecond();
            }
        } catch (NumberFormatException ignored) {
        }
        return -1;
    }


    private String getUUID(String playername) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(playername);
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

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tabCompletions = new ArrayList<>();
        if (args.length == 1) {
            for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                tabCompletions.add(player.getName());
            }
        } else if (args.length == 2) {
            Configuration banIdsConfig = FileManager.getTempBanIdConfig();
            if (banIdsConfig != null) {
                Configuration banIdsSection = banIdsConfig.getSection("temp-ban-ids");
                if (banIdsSection != null) {
                    for (String banId : banIdsSection.getKeys()) {
                        tabCompletions.add(banId);
                    }
                }
            }
        }
        return tabCompletions;
    }
}
