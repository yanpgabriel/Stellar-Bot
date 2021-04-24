package dev.yanpgabriel.stellar.bot;

import net.dv8tion.jda.api.JDA;

import java.util.Objects;

public class Bot {
    
    private static Bot instance;
    
    private JDA jda;

    private Bot() {
    }
    
    public JDA getJDA() {
        return jda;
    }

    public void setJDA(JDA jda) {
        System.out.println(jda.getToken());
        this.jda = jda;
    }

    public static Bot getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Bot();
        }
        return instance;
    }
    
}
