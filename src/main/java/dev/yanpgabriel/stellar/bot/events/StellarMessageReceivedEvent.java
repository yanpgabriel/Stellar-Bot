package dev.yanpgabriel.stellar.bot.events;

import dev.yanpgabriel.stellar.bot.Bot;
import dev.yanpgabriel.stellar.bot.BotReaction;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;

public class StellarMessageReceivedEvent extends ListenerAdapter {

    // http://unicode.org/emoji/charts/full-emoji-list.html reaction
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String[] split = msg.getContentRaw().split(" ", 2);
        String cmd = split[0].toLowerCase();

        if (!cmd.startsWith(Bot.PREFIX)) {
            return;
        }
        
        Bot.getInstance().getComandos()
                .stream()
                .filter(comando -> StringUtils.equalsIgnoreCase(comando.getName(), cmd.substring(1)))
                .findFirst()
                .ifPresent(comando -> {
                    try {
                        comando.run(event, split);
                    } catch (Exception e) {
                        event.getMessage().addReaction(BotReaction.DEDO_DO_MEIO.toString()).queue();
                        event.getChannel().sendMessage(e.getMessage()).queue();
                    }
                });

    }

}
