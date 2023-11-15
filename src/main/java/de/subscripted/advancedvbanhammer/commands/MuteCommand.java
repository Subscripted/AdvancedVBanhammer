package de.subscripted.advancedvbanhammer.commands;

import de.subscripted.advancedvbanhammer.Main;
import de.subscripted.advancedvbanhammer.utils.MuteManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MuteCommand extends Command {

    private static final String MUTE_COMMAND_NAME = "mute";

    public MuteCommand() {
        super(MUTE_COMMAND_NAME);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(TextComponent.fromLegacyText("§cVerwendung: /mute <Spieler> <Grund>"));
            return;
        }

        String playerName = args[0];
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(args[i]).append(" ");
        }
        reason = new StringBuilder(reason.toString().trim());

        ProxiedPlayer targetPlayer = Main.getInstance().getProxy().getPlayer(playerName);

        if (targetPlayer != null) {
            MuteManager muteManager = new MuteManager();
            muteManager.mutePlayer(targetPlayer.getUniqueId(), true);
            targetPlayer.sendMessage(TextComponent.fromLegacyText("§cDu wurdest gemutet für: " + reason));
        } else {
            sender.sendMessage(TextComponent.fromLegacyText("§cSpieler nicht gefunden."));
        }

        sender.sendMessage(TextComponent.fromLegacyText("§aSpieler " + playerName + " wurde gemutet für: " + reason));
    }
}