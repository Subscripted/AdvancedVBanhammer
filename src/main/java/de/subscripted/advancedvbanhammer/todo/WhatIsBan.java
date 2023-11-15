package de.subscripted.advancedvbanhammer.todo;

import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WhatIsBan extends Command implements TabExecutor {

    public WhatIsBan() {
        super("reasons");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer player)) {
            sender.sendMessage(TextComponent.fromLegacyText(FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE)));
            return;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("temp")) {
            Set<String> reasonIds = new HashSet<>(FileManager.getTempBanIdConfig().getSection("temp-ban-ids").getKeys());
            StringBuilder reasonsMessage = new StringBuilder(" §7TempBan Reasons:\n");

            for (String reasonId : reasonIds) {
                String reasonName = FileManager.getTempBanIdConfig().getString("temp-ban-ids." + reasonId + ".name");
                String time = FileManager.getTempBanIdConfig().getString("temp-ban-ids." + reasonId + ".time");
                String formattedTime = formatTime(time);
                reasonsMessage.append("§e").append(reasonId).append("§7: ").append(reasonName).append(" (Time: ").append(formattedTime).append(")\n");
            }
            player.sendMessage(TextComponent.fromLegacyText("§7§m[]=====[§cAdvancedVBanhammer§7]=====[]"));
            player.sendMessage(TextComponent.fromLegacyText(reasonsMessage.toString()));
            player.sendMessage(TextComponent.fromLegacyText("§7§n[]================================[]"));
        } else if (args.length == 1 && args[0].equalsIgnoreCase("perm")) {
            Set<String> reasonIds = new HashSet<>(FileManager.getBanIdsConfig().getSection("ban-ids").getKeys());
            StringBuilder reasonMessage = new StringBuilder("§aBan Reasons:\n");

            for (String reasonId : reasonIds) {
                String reasonName = FileManager.getBanIdsConfig().getString("ban-ids." + reasonId + ".name");
                reasonMessage.append("§e").append(reasonId).append("§7: ").append(reasonName).append("\n");
            }

            player.sendMessage(TextComponent.fromLegacyText(reasonMessage.toString()));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1) {
            list.add("perm");
            list.add("temp");
        }
        return list;
    }

    private String formatTime(String time) {
        long seconds = Long.parseLong(time.substring(0, time.length() - 1));
        char unit = time.charAt(time.length() - 1);
        String unitName;

        switch (unit) {
            case 's':
                unitName = "Sekunde(n)";
                break;
            case 'm':
                unitName = "Minute(n)";
                break;
            case 'h':
                unitName = "Stunde(n)";
                break;
            case 'd':
                unitName = "Tag(e)";
                break;
            case 'w':
                unitName = "Woche(n)";
                break;
            default:
                return "Ungültige Einheit";
        }
        return seconds + " " + unitName;
    }
}