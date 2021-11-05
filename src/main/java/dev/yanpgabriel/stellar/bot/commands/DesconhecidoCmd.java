package dev.yanpgabriel.stellar.bot.commands;

import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@StellarCommand
public class DesconhecidoCmd extends AbstractCmd {
    
    @Override
    public void run(MessageReceivedEvent event, String... args) {
        Message msg = event.getMessage();
        msg.addReaction("\uD83E\uDD14").queue();
        event.getChannel().sendMessage("Comando não encontrado!").queue();
    }

}
