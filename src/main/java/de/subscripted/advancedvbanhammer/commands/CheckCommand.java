package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.utils.BanManager;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import de.subscripted.advancedvbanhammer.utils.UUIDFetcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import java.util.List;
import java.util.UUID;

public class CheckCommand extends Command {

    private Main plugin;

    public CheckCommand(Main plugin) {
        super("check");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE));
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                List<String> list = BanManager.getBannedPlayers();
                if (list.size() == 0) {
                    sender.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.NO_ONE_IS_BANNED));
                }
                for (String allBanned : BanManager.getBannedPlayers()) {
                    sender.sendMessage(plugin.getPrefix() + allBanned + "§9(§eGrund: §r§c " + BanManager.getReason(getUUID(allBanned)) + "§7)");
                }
                return;
            }

            String playername = args[0];
            player.sendMessage(plugin.getPrefix() + FileManager.getMessage(ConfigMessage.CHECK_IF_BANNED).replace("%playername%", playername) + (BanManager.isBanned(getUUID(playername)) ?  FileManager.getMessage(ConfigMessage.CHECK_IS_BANNED): FileManager.getMessage(ConfigMessage.CHECK_IS_NOT_BANNED)));
            if (BanManager.isBanned(getUUID(playername))) {
                player.sendMessage(plugin.getPrefix() + ChatColor.YELLOW + "Grund: " + ChatColor.RED + BanManager.getReason(getUUID(playername)));
                player.sendMessage(plugin.getPrefix() + ChatColor.YELLOW + "Verbleibende Zeit: " + ChatColor.RED + BanManager.getRemainingTime(getUUID(playername)));
            }
            return;
        }
        player.sendMessage(plugin.getPrefix() + ChatColor.RED + "Verwendung: /check <nutzer>");
        player.sendMessage(plugin.getPrefix() + ChatColor.RED + "Verwendung: /check list");
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
