package dev.yanpgabriel.stellar.bot.commands;

import dev.yanpgabriel.stellar.bot.BotReaction;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@StellarCommand
public class PingCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        event.getMessage().addReaction(BotReaction.TOPPEN.toString()).queue();
        long time = System.currentTimeMillis();
        event.getChannel().sendMessage("Calculando...")
                .queue(response -> response.editMessageFormat("Meu ping atual: %d ms", System.currentTimeMillis() - time).queue());
    }

}
