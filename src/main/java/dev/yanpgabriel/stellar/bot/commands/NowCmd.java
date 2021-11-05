package dev.yanpgabriel.stellar.bot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import dev.yanpgabriel.stellar.bot.BotReaction;
import dev.yanpgabriel.stellar.bot.BotUtils;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.modules.play.GuildMusicManager;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerController;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@StellarCommand
public class NowCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        final TextChannel channel = event.getTextChannel();

        BotUtils.checkIamVoiceChannel(event);
        BotUtils.checkInVoiceChannel(event);
        BotUtils.checkSameVoiceChannel(event);
        
        final GuildMusicManager musicManager = PlayerController.getGuildAudioPlayer(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.player;
        final AudioTrack track = audioPlayer.getPlayingTrack();

        if (track == null) {
            channel.sendMessage("Não há nenhuma faixa sendo reproduzida no momento.").queue();
            return;
        }

        final AudioTrackInfo info = track.getInfo();
        event.getMessage().addReaction(BotReaction.TOPPEN.toString()).queue();
        channel.sendMessageFormat(":notes: Tocando agora `%s de %s [%s/%s]` (Link: <%s>)",
                info.title,
                info.author,
                PlayerController.formatTime(track.getPosition()), 
                PlayerController.formatTime(track.getDuration()), 
                info.uri).queue();
    }

}
