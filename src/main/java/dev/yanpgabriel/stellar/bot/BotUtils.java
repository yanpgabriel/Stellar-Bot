package dev.yanpgabriel.stellar.bot;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public abstract class BotUtils {

    public static void forceJoinVoiceChat(MessageReceivedEvent event) {
        final GuildVoiceState selfVoiceState = event.getGuild().getSelfMember().getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            BotUtils.joinVoiceChat(event);
        }
    }

    public static void joinVoiceChat(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();
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
        event.getMessage().addReaction(BotReaction.TOPPEN.toString()).queue();
        audioManager.openAudioConnection(connectedChannel);
    }

    public static void checkIamVoiceChannel(MessageReceivedEvent event) {
        final GuildVoiceState selfVoiceState = event.getGuild().getSelfMember().getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            throw new RuntimeException("Eu preciso estar em um canal de voz para que isso funcione.");
        }
    }

    public static void checkInVoiceChannel(MessageReceivedEvent event) {
        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inVoiceChannel()) {
            throw new RuntimeException("Você precisa estar em um canal de voz para que este comando funcione.");
        }
    }

    public static void checkSameVoiceChannel(MessageReceivedEvent event) {
        final Member member = event.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        final GuildVoiceState selfVoiceState = event.getGuild().getSelfMember().getVoiceState();
        if (!memberVoiceState.getChannel().getName().equalsIgnoreCase(selfVoiceState.getChannel().getName())) {
            throw new RuntimeException("Você precisa estar no mesmo canal de voz que eu para que isso funcione ");
        }
    }

    

}
