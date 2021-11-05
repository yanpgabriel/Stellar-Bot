package dev.yanpgabriel.stellar.bot.commands;

import dev.yanpgabriel.stellar.bot.BotUtils;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.modules.play.GuildMusicManager;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerController;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@StellarCommand
public class StopCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        final TextChannel channel = event.getTextChannel();
        final Member self = event.getGuild().getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        BotUtils.checkIamVoiceChannel(event);
        BotUtils.checkInVoiceChannel(event);
        BotUtils.checkSameVoiceChannel(event);

        final GuildMusicManager musicManager = PlayerController.getGuildAudioPlayer(event.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        channel.sendMessage("O player foi interrompido e a fila foi limpa").queue();
    }

}
