package dev.yanpgabriel.stellar.bot.modules.play;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.yanpgabriel.stellar.bot.BotReaction;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class AudioLoadResult implements AudioLoadResultHandler {

    private final MessageReceivedEvent event;
    private final GuildMusicManager musicManager;
    private final String pesquisaOriginal;

    public AudioLoadResult(MessageReceivedEvent event, GuildMusicManager musicManager, String pesquisaOriginal) {
        this.event = event;
        this.musicManager = musicManager;
        this.pesquisaOriginal = pesquisaOriginal;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        event.getMessage().addReaction(BotReaction.TOPPEN.toString()).queue();
        event.getChannel().sendMessage("Adicionando na queue " + track.getInfo().title).queue();
        musicManager.scheduler.queue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        AudioTrack firstTrack = playlist.getSelectedTrack();

        if (firstTrack == null) {
            firstTrack = playlist.getTracks().get(0);
        }

        musicManager.scheduler.queue(firstTrack);

        event.getMessage().addReaction(BotReaction.TOPPEN.toString()).queue();
        if (playlist.isSearchResult()) {
            event.getChannel().sendMessage("Adicionando na queue " + firstTrack.getInfo().title).queue();
            return;
        }

        event.getChannel().sendMessage("Adicionando na queue " + firstTrack.getInfo().title + " (primeira faixa da playlist " + playlist.getName() + ")").queue();

        final List<AudioTrack> tracks = playlist.getTracks();

        for (final AudioTrack track : tracks) {
            if (firstTrack.getInfo().title.equalsIgnoreCase(track.getInfo().title)) {
                continue;
            }
            musicManager.scheduler.queue(track.makeClone());
        }

        event.getChannel().sendMessage("Adicionando na queue: `")
                .append(String.valueOf(tracks.size()))
                .append("` faixas da playlist `")
                .append(playlist.getName())
                .append('`')
                .queue();
    }

    @Override
    public void noMatches() {
        event.getChannel().sendMessage("Não encontramos nada ao pesquisa: " + pesquisaOriginal).queue();
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        event.getChannel().sendMessage("Não foi possivel tocar: " + exception.getMessage()).queue();
    }

}
