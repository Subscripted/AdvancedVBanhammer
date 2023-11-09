package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.enums.ConfigMessage;
import de.subscripted.advancedvbanhammer.enums.Permissions;
import de.subscripted.advancedvbanhammer.utils.FileManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand extends Command implements TabExecutor {

    public InfoCommand() {
        super("bancontroller");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(TextComponent.fromLegacyText(FileManager.getMessage(ConfigMessage.SENDER_IS_CONSOLE)));
            return;
        }

        if (args.length < 1) {
            player.sendMessage(TextComponent.fromLegacyText("§8§m----------" + "§x§f§b§1§c§1§5§lB§x§f§b§2§c§1§5§la§x§f§b§3§c§1§4§ln§x§f§c§4§d§1§4§lC§x§f§c§5§d§1§3§lo§x§f§c§6§d§1§3§ln§x§f§c§7§d§1§3§lt§x§f§c§8§d§1§2§lr§x§f§c§9§d§1§2§lo§x§f§d§a§e§1§1§ll§x§f§d§b§e§1§1§ll§x§f§d§c§e§1§0§le§x§f§d§d§e§1§0§lr" + "§8§m----------"));
            player.sendMessage(TextComponent.fromLegacyText("§x§f§b§1§c§1§5§lD§x§f§c§7§d§1§3§le§x§f§d§d§e§1§0§lv §8§l· §7Subscripted"));
            player.sendMessage(TextComponent.fromLegacyText("§x§f§b§1§c§1§5§lV§x§f§b§3§c§1§4§le§x§f§c§5§d§1§3§lr§x§f§c§7§d§1§3§ls§x§f§c§9§d§1§2§li§x§f§d§b§e§1§1§lo§x§f§d§d§e§1§0§ln §8§l· §71.0.0"));
            player.sendMessage(TextComponent.fromLegacyText("§x§f§b§1§c§1§5§lL§x§f§b§2§e§1§5§li§x§f§b§3§f§1§4§lc§x§f§c§5§1§1§4§le§x§f§c§6§3§1§3§ln§x§f§c§7§4§1§3§ls§x§f§c§8§6§1§2§le§x§f§c§9§7§1§2§l-§x§f§c§a§9§1§1§lU§x§f§d§b§b§1§1§ls§x§f§d§c§c§1§0§le§x§f§d§d§e§1§0§lr §8§l· §aVarilx.de"));
            player.sendMessage(TextComponent.fromLegacyText("§x§f§b§1§c§1§5§lL§x§f§b§4§3§1§4§li§x§f§c§5§1§1§4§le§x§f§c§6§a§1§3§ln§x§f§c§7§4§1§3§le§x§f§c§8§6§1§2§le§x§f§c§9§7§1§2§ln§x§f§d§b§e§1§1§ls§x§f§d§c§c§1§0§le §8§l· §7Ayont"));
            player.sendMessage(TextComponent.fromLegacyText("§x§f§b§1§c§1§5§lS§x§f§b§4§3§1§4§lt§x§f§c§5§1§1§4§lo§x§f§c§7§d§1§3§lr§x§f§c§9§d§1§2§la§x§f§d§b§e§1§1§lg§x§f§d§d§e§1§0§le §8§l· §7MySQL"));
            player.sendMessage(TextComponent.fromLegacyText("§x§f§b§1§c§1§5§lS§x§f§b§4§3§1§4§lr§x§f§c§6§a§1§3§le§x§f§c§9§0§1§2§lv§x§f§d§b§7§1§1§le§x§f§d§d§e§1§0§lr §8§l· §7BungeeCord"));
            player.sendMessage(TextComponent.fromLegacyText("§x§f§b§1§c§1§5§lP§x§f§b§4§3§1§4§lr§x§f§c§6§a§1§3§le§x§f§c§9§0§1§2§lf§x§f§d§b§7§1§1§li§x§f§d§d§e§1§0§lx §8§l· §7" + Main.getInstance().getPrefix()));
            player.sendMessage(TextComponent.fromLegacyText("§8§m-----------------------------------"));
            return;
        }

        if (args.length == 1) {
            FileManager.setup(Main.getInstance());
            FileManager.reloadPrefix(Main.getInstance());
            sender.sendMessage(TextComponent.fromLegacyText(Main.getInstance().getPrefix() + FileManager.getMessage(ConfigMessage.RELOAD_SUCCESS)));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> tabCompletions = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission(FileManager.getPermission(Permissions.PERMISSION_ALL))) {
            tabCompletions.add("reload");
        }
        return tabCompletions;
    }
}