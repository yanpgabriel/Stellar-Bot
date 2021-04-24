package dev.yanpgabriel.stellar.bot.events;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class StellarMessageReceivedEvent extends ListenerAdapter {

    // http://unicode.org/emoji/charts/full-emoji-list.html reaction
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String cmg = msg.getContentRaw().toLowerCase();
        MessageChannel channel = event.getChannel();
        if (cmg.equals(".ping")) {
            msg.addReaction("\uD83D\uDC4C").queue();
            long time = System.currentTimeMillis();
            channel.sendMessage("Calculando...")
                    .queue(response -> response.editMessageFormat("Meu ping atual: %d ms", System.currentTimeMillis() - time).queue());
        }
        else if(cmg.equals(".vem")) {
            if(!event.getGuild().getSelfMember().hasPermission(Permission.VOICE_CONNECT)) {
                channel.sendMessage("Não tenho permissão para entrar em chat de voz!").queue();
                return;
            }
            VoiceChannel connectedChannel = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();
            if(connectedChannel == null) {
                msg.addReaction("\uD83D\uDD95").queue();
                channel.sendMessage("Você não esta em nenhum chat de voz!").queue();
                return;
            }
            AudioManager audioManager = event.getGuild().getAudioManager();
            msg.addReaction("\uD83D\uDC4C").queue();
            audioManager.openAudioConnection(connectedChannel);
        } 
        else if(cmg.equals(".sai")) {
            VoiceChannel connectedChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
            if(connectedChannel == null) {
                msg.addReaction("\uD83E\uDD14").queue();
                channel.sendMessage("Não estou em nenhum chat de voz para sair '-'").queue();
                return;
            }
            msg.addReaction("\uD83D\uDC4C").queue();
            event.getGuild().getAudioManager().closeAudioConnection();
        }
    }

}
