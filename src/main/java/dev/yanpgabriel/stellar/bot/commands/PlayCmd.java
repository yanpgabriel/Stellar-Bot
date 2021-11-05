package dev.yanpgabriel.stellar.bot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.yanpgabriel.stellar.bot.BotReaction;
import dev.yanpgabriel.stellar.bot.BotUtils;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.modules.play.AudioLoadResult;
import dev.yanpgabriel.stellar.bot.modules.play.GuildMusicManager;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerController;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerSocket;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

@StellarCommand
public class PlayCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        final Message msg = event.getMessage();
        final MessageChannel channel = event.getChannel();
        if (args.length < 2) {
            msg.addReaction(BotReaction.DEDO_DO_MEIO.toString()).queue();
            channel.sendMessage("Tem que colocar alguma coisa depois do play pô...").queue();
            return;
        }

        BotUtils.forceJoinVoiceChat(event);
        BotUtils.checkInVoiceChannel(event);
        BotUtils.checkSameVoiceChannel(event);
        
        String pesquisaOriginal = args[1];
        String pesquisa = args[1];

        if (!PlayerController.isUrl(pesquisa)) {
            pesquisa = "ytsearch: " + pesquisa;
        }
        
        GuildMusicManager musicManager = PlayerController.getGuildAudioPlayer(event.getGuild(), channel);

        PlayerController.playerManager.loadItemOrdered(musicManager, pesquisa, new AudioLoadResult(event, musicManager, pesquisaOriginal));        
    }

    @Override
    public void runSocket(PlayerSocket socket, Guild guild, String... args) {
        if (args.length < 2) {
            socket.broadcast("Tem que colocar alguma coisa depois do play pô...");
            return;
        }
        GuildVoiceState selfVoiceState = guild.getSelfMember().getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            socket.broadcast("Eu preciso estar em um canal de voz para que isso funcione.");
            return;
        }
        String pesquisaOriginal = args[1];
        String pesquisa = args[1];
        if (!PlayerController.isUrl(pesquisa)) {
            pesquisa = "ytsearch: " + pesquisa;
        }
        GuildMusicManager musicManager = PlayerController.getGuildAudioPlayer(guild);
        PlayerController.playerManager.loadItemOrdered(musicManager, pesquisa, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                socket.broadcast("Adicionando na queue " + track.getInfo().title);
                musicManager.scheduler.queue(track);
            }
            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }
                musicManager.scheduler.queue(firstTrack);
                if (playlist.isSearchResult()) {
                    socket.broadcast("Adicionando na queue " + firstTrack.getInfo().title);
                    return;
                }
                socket.broadcast("Adicionando na queue " + firstTrack.getInfo().title + " (primeira faixa da playlist " + playlist.getName() + ")");
                final List<AudioTrack> tracks = playlist.getTracks();
                for (final AudioTrack track : tracks) {
                    if (firstTrack.getInfo().title.equalsIgnoreCase(track.getInfo().title)) {
                        continue;
                    }
                    musicManager.scheduler.queue(track.makeClone());
                }
                socket.broadcast("Adicionando na queue: `" + 
                        tracks.size() + 
                        "` faixas da playlist `" + 
                        playlist.getName() + 
                        '`');
            }
            @Override
            public void noMatches() {
                socket.broadcast("Não encontramos nada ao pesquisa: " + pesquisaOriginal);
            }
            @Override
            public void loadFailed(FriendlyException exception) {
                socket.broadcast("Não foi possivel tocar: " + exception.getMessage());
            }
        });
    }
}
