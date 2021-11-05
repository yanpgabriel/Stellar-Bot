package dev.yanpgabriel.stellar.bot.commands;

import dev.yanpgabriel.stellar.bot.BotReaction;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerSocket;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@StellarCommand
public class SaiCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        MessageChannel channel = event.getChannel();
        VoiceChannel connectedChannel = event.getGuild().getSelfMember().getVoiceState().getChannel();
        if(connectedChannel == null) {
            event.getMessage().addReaction(BotReaction.DEDO_DO_MEIO.toString()).queue();
            channel.sendMessage("Não estou em nenhum chat de voz para sair '-'").queue();
            return;
        }
        event.getMessage().addReaction(BotReaction.TOPPEN.toString()).queue();
        event.getGuild().getAudioManager().closeAudioConnection();
    }

    @Override
    public void runSocket(PlayerSocket socket, Guild guild, String[] split) {
        VoiceChannel connectedChannel = guild.getSelfMember().getVoiceState().getChannel();
        if(connectedChannel == null) {
            socket.broadcast("Não estou em nenhum chat de voz para sair '-'");
            return;
        }
        guild.getAudioManager().closeAudioConnection();
    }
}
