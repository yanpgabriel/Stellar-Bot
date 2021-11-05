package dev.yanpgabriel.stellar.bot.commands;


import dev.yanpgabriel.stellar.bot.modules.play.PlayerSocket;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class AbstractCmd {
    
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String cmdName) {
        this.name = cmdName;
    }

    public void run(MessageReceivedEvent event, String... args) {
    }

    public void runSocket(PlayerSocket socket, Guild guild, String... args) {
    }

}

