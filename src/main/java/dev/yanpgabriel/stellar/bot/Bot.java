package dev.yanpgabriel.stellar.bot;

import dev.yanpgabriel.stellar.bot.commands.AbstractCmd;
import net.dv8tion.jda.api.JDA;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Bot {
    
    private static Bot instance;
    public static String PREFIX = ".";
    
    private JDA jda;
    private Set<AbstractCmd> comandos = new HashSet<>();

    private Bot() {
    }
    
    public JDA getJDA() {
        return jda;
    }

    public void setJDA(JDA jda) {
        System.out.println(jda.getToken());
        this.jda = jda;
    }

    public void limparComandos() {
        comandos.clear();
    }
    
    public void removerComando(AbstractCmd command) {
        comandos.remove(command);
    }
    
    public void registrarComando(AbstractCmd command) {
        comandos.add(command);
    }
    
    public void registrarComandos(Set<AbstractCmd> commands) {
        comandos.addAll(commands);
    }

    public Set<AbstractCmd> getComandos() {
        return comandos;
    }

    public static Bot getInstance() {
        if (Objects.isNull(instance)) {
            instance = new Bot();
        }
        return instance;
    }
    
}
