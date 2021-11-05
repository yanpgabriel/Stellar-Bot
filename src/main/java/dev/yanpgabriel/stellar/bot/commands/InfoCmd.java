package dev.yanpgabriel.stellar.bot.commands;

import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerSocket;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.stream.Collectors;

@StellarCommand
public class InfoCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        final TextChannel channel = event.getTextChannel();
        channel.sendMessage(String.format("Id do Servidor: %s", event.getGuild().getId())).queue();
        channel.sendMessage(String.format("Id do canal: %s", channel.getId())).queue();
    }

    @Override
    public void runSocket(PlayerSocket socket, Guild guild, String... args) {
        List<String> voiceChannels = guild.getVoiceChannels().stream()
                .map(vc -> vc.getName())
                .collect(Collectors.toList());
        StringBuffer sb = new StringBuffer();
        sb.append("List de canais de voz:");
        voiceChannels.forEach(vc -> sb.append("\n-> ").append(vc));
        socket.broadcast(sb.toString());
    }
}
