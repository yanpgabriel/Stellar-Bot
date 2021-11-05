package dev.yanpgabriel.stellar.bot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import dev.yanpgabriel.stellar.bot.BotReaction;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.modules.play.GuildMusicManager;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerController;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@StellarCommand
public class QueueCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        final TextChannel channel = event.getTextChannel();
        final GuildMusicManager musicManager = PlayerController.getGuildAudioPlayer(event.getGuild());
        final AudioPlayer player = musicManager.player;
        final AudioTrack playingTrack = player.getPlayingTrack();
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.getQueue();

        if (playingTrack == null) {
            channel.sendMessage("Não há nenhuma faixa sendo reproduzida no momento.").queue();
            return;
        }

        final MessageAction messageAction = channel.sendMessage("**Queue:**\n");

        final AudioTrackInfo playingTrackInfo = playingTrack.getInfo();
        messageAction.append(String.format(":notes: Tocando agora `%s` by `%s` (Link: <%s>)\n", playingTrackInfo.title, playingTrackInfo.author, playingTrackInfo.uri));

        if (queue.isEmpty()) {
            messageAction.queue();
            return;
        }

        final int trackCount = Math.min(queue.size(), 10);
        final List<AudioTrack> trackList = new ArrayList<>(queue);

        for (int i = 0; i <  trackCount; i++) {
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();

            messageAction.append('#')
                    .append(String.valueOf(i + 1))
                    .append(" `")
                    .append(String.valueOf(info.title))
                    .append(" by ")
                    .append(info.author)
                    .append("` [`")
                    .append(PlayerController.formatTime(track.getDuration()))
                    .append("`]\n");
        }

        if (trackList.size() > trackCount) {
            messageAction.append("And `")
                    .append(String.valueOf(trackList.size() - trackCount))
                    .append("` more...");
        }
        messageAction.queue();
        event.getMessage().addReaction(BotReaction.TOPPEN.toString()).queue();
    }

}
