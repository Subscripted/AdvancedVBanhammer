package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.enums.Permissions;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import de.subscripted.advancedvbanhammer.utils.UUIDFetcher;
import lombok.SneakyThrows;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BanCommand extends Command implements TabExecutor {

    private static final String PLACEHOLDER = "%playername%";
    private static final String BAN_COMMAND_NAME = "perm-ban";
    private static final String permissionAll = FileManager.getPermission(Permissions.PERMISSION_ALL);
    private static final String permissionBan = FileManager.getPermission(Permissions.PERMISSION_BAN);

    public BanCommand() {
        super(BAN_COMMAND_NAME);
    }

    @Override
    @SneakyThrows
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sender.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE)));
            return;
        }

        if (args.length == 2) {
            String playername = args[0];
            String banIdStr = args[1];
            String senderName = sender.getName();

            try {
                int banId = Integer.parseInt(banIdStr);
                String banMessage = FileManager.getMessage(ConfigMessage.PLAYER_BAN).replace(PLACEHOLDER, playername);

                if (BanManager.isBanned(getUUID(playername))) {
                    player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.PLAYER_IS_BANNED).replace(PLACEHOLDER, playername)));
                    return;
                }

                String banIdPath = "ban-ids." + banId;
                Configuration banIdsConfig = FileManager.getBanIdsConfig();

                if (banIdsConfig != null && banIdsConfig.contains(banIdPath)) {
                    String reason = banIdsConfig.getString(banIdPath);
                    BanManager.ban(getUUID(playername), playername, reason, -1, senderName);

                    sendBanBroadcast(player, playername, reason);
                    player.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', banMessage)));
                    return;
                }

                player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.PLAYER_BAN).replace(PLACEHOLDER, playername)));
            } catch (NumberFormatException e) {
                player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.MUST_BE_A_NUMBER)));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            player.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + ChatColor.RED + "Usage: /perm-ban <playername> <banId>"));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tabCompletions = new ArrayList<>();
        if (args.length == 1) {
            for (ProxiedPlayer onlinePlayer : Main.getInstance().getProxy().getPlayers()) {
                tabCompletions.add(onlinePlayer.getName());
            }
        } else if (args.length == 2) {
            Configuration banIdsConfig = FileManager.getBanIdsConfig();
            if (banIdsConfig != null) {
                tabCompletions.addAll(banIdsConfig.getSection("ban-ids").getKeys());
            }
        }
        return tabCompletions;
    }

    private String getUUID(String playername) {
        ProxiedPlayer player = Main.getInstance().getProxy().getPlayer(playername);
        if (player != null) {
            return player.getUniqueId().toString();
        } else {
            return UUIDFetcher.getUUID(playername).toString();
        }
    }

    private void sendBanBroadcast(ProxiedPlayer player, String playername, String reason) {
        for (ProxiedPlayer onlinePlayer : player.getServer().getInfo().getPlayers()) {
            if (onlinePlayer.hasPermission(FileManager.getPermission(Permissions.PERMISSION_SEEBANBROADCAST))) {
                onlinePlayer.sendMessage(TextComponent.fromLegacyText(FileManager.getMessage(ConfigMessage.PLAYER_BANNED_BROADCAST)
                        .replace(PLACEHOLDER, playername)
                        .replace("%reason%", reason)
                        .replace("%player%", player.getDisplayName())));
            }
        }
    }
}
