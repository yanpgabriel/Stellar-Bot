package dev.yanpgabriel.stellar.bot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import dev.yanpgabriel.stellar.bot.BotReaction;
import dev.yanpgabriel.stellar.bot.BotUtils;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.modules.play.GuildMusicManager;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerController;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@StellarCommand
public class SkipCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        MessageChannel channel = event.getChannel();

        BotUtils.checkIamVoiceChannel(event);
        BotUtils.checkInVoiceChannel(event);
        BotUtils.checkSameVoiceChannel(event);

        final GuildMusicManager musicManager = PlayerController.getGuildAudioPlayer(event.getGuild());
        final AudioPlayer audioPlayer = musicManager.player;

        if (audioPlayer.getPlayingTrack() == null) {
            event.getMessage().addReaction(BotReaction.DEDO_DO_MEIO.toString()).queue();
            channel.sendMessage("Não há nenhuma faixa sendo reproduzida no momento.").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        event.getMessage().addReaction(BotReaction.TOPPEN.toString()).queue();
        channel.sendMessage("Pulou a faixa atual.").queue();
    }

}
