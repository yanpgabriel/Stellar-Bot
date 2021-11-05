package dev.yanpgabriel.stellar.resource;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.yanpgabriel.stellar.MusicaDTO;
import dev.yanpgabriel.stellar.bot.modules.play.GuildMusicManager;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerController;
import dev.yanpgabriel.stellar.services.BotService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

@Path("bot")
public class BotResource {
    
    @Inject
    BotService botService;

    @GET
    public String bot() {
        return botService.getJDA().getSelfUser().getName();
    }

    @GET
    @Path("queue/{idServidor}")
    public List<MusicaDTO> queue(@PathParam("idServidor") String idServidor) {
        GuildMusicManager guildMusicManager = PlayerController.getGuildAudioPlayerById(idServidor);
        final AudioPlayer player = guildMusicManager.player;
        final AudioTrack playingTrack = player.getPlayingTrack();
        final BlockingQueue<AudioTrack> queue = guildMusicManager.scheduler.getQueue();

        if (playingTrack == null) {
            return Collections.emptyList();
        }

        List<MusicaDTO> musicaDTOS = new ArrayList<>();
        musicaDTOS.add(new MusicaDTO(playingTrack));
        musicaDTOS.addAll(queue.stream().map(MusicaDTO::new).collect(Collectors.toList()));
        return musicaDTOS;
    }
    
}
