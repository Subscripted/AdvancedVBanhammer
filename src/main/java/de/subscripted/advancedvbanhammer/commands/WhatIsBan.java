package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WhatIsBan extends Command implements TabExecutor {

    private Main plugin;

    public WhatIsBan(Main plugin) {
        super("reasons");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (args.length == 1 && args[0].equalsIgnoreCase("temp")) {
            Set<String> reasonIds = (Set<String>) FileManager.getTempBanIdConfig().getSection("temp-ban-ids").getKeys();
            StringBuilder reasonsMessage = new StringBuilder("&aTempBan Reasons:\n");

            for (String reasonId : reasonIds) {
                String reasonName = FileManager.getTempBanIdConfig().getString("temp-ban-ids." + reasonId + ".name");
                reasonsMessage.append("&e").append(reasonId).append("&7: ").append(reasonName).append("\n");
            }

            player.sendMessage(reasonsMessage.toString());
        }else
        if (args.length == 1 && args[0].equalsIgnoreCase("perm")){
            Set<String> reasonIds = (Set<String>) FileManager.getBanIdsConfig().getSection("ban-ids").getKeys();
            StringBuilder reasonMessage = new StringBuilder("&aBan Reasons:\n");

            for (String reasonId : reasonIds){
                String reasonName = FileManager.getBanIdsConfig().getString("ban-ids. " + reasonId);
                reasonMessage.append("&e").append(reasonId).append("&7: ").append(reasonName).append("\n");
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> list = new ArrayList<>();
        if (args.length == 1){
            list.add("perm");
            list.add("temp");
        }
        return list;
    }
}