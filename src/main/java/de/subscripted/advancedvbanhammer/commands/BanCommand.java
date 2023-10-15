package de.subscripted.advancedvbanhammer.commands;


import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.enums.Permissions;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import de.subscripted.advancedvbanhammer.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanCommand extends Command implements TabExecutor {

    private Main plugin;

    public BanCommand(Main plugin) {
        super("perm-ban");
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
            String placeholder = "%playername%";
            String playername = args[0];
            String banMessage = FileManager.getMessage(ConfigMessage.PLAYER_BAN).replace(placeholder, playername);
            if (BanManager.isBanned(getUUID(playername))) {
                player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.PLAYER_IS_BANNED).replace(placeholder, playername));
                return;
            }

            String banIdStr = args[1];
            Configuration banIdsConfig = FileManager.getBanIdsConfig();
            if (banIdsConfig.contains("ban-ids." + banIdStr)) {
                String reason = banIdsConfig.getString("ban-ids." + banIdStr);
                BanManager.ban(getUUID(playername), playername, reason, -1);
                for (ProxiedPlayer onlinePlayer : ProxyServer.getInstance().getPlayers()) {
                    if (onlinePlayer.hasPermission(FileManager.getPermission(Permissions.PERMISSION_SEEBANBROADCAST))) {
                        onlinePlayer.sendMessage(FileManager.getMessage(ConfigMessage.PLAYER_BANNED_BROADCAST)
                                .replace("%playername%", playername)
                                .replace("%reason%", reason)
                                .replace("%player%", player.getDisplayName()));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', banMessage));
                        return;
                    }
                }
            }
            player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.PLAYER_BAN).replace("%playername%", playername));
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
            Configuration banIdsConfig = FileManager.getBanIdsConfig();
            if (banIdsConfig != null) {
                Configuration banIdsSection = banIdsConfig.getSection("ban-ids");
                if (banIdsSection != null) {
                    for (String banId : banIdsSection.getKeys()) {
                        tabCompletions.add(banId);
                    }
                }
            }
        }
        return tabCompletions;
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
}