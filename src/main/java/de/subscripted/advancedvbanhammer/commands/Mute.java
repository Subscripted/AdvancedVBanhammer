package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.utils.MuteManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Mute extends Command {
    private Main plugin;

    public Mute(Main plugin) {
        super("mute");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cVerwendung: /mute <Spieler> <Grund>");
            return;
        }

        String playerName = args[0];
        String reason = "";
        for (int i = 1; i < args.length; i++) {
            reason += args[i] + " ";
        }
        reason = reason.trim();

        ProxiedPlayer targetPlayer = plugin.getProxy().getPlayer(playerName);

        if (targetPlayer != null) {
            MuteManager muteManager = new MuteManager();
            muteManager.mutePlayer(targetPlayer.getUniqueId(), true);
            targetPlayer.sendMessage("§cDu wurdest gemutet für: " + reason);
        } else {
            sender.sendMessage("§cSpieler nicht gefunden.");
        }

        sender.sendMessage("§aSpieler " + playerName + " wurde gemutet für: " + reason);
    }
}
