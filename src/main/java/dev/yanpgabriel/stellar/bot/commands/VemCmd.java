package dev.yanpgabriel.stellar.bot.commands;

import dev.yanpgabriel.stellar.bot.BotUtils;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerSocket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

@StellarCommand
public class VemCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        BotUtils.joinVoiceChat(event);
    }

    @Override
    public void runSocket(PlayerSocket socket, Guild guild, String... args) {
        if (args.length < 2) {
            return;
        }
        String txt = args[1];
        guild.getVoiceChannels().stream()
                .filter(vc -> vc.getName().equalsIgnoreCase(txt))
                .findFirst()
                .ifPresent(vc -> {
                    if(!guild.getSelfMember().hasPermission(Permission.VOICE_CONNECT)) {
                        socket.broadcast("Não tenho permissão para entrar em chat de voz!");
                        return;
                    }
                    AudioManager audioManager = guild.getAudioManager();
                    audioManager.openAudioConnection(vc);
                    socket.broadcast("Entrei na sala '" + vc.getName() + "'.");
                });
    }
}
