package dev.yanpgabriel.stellar.bot.modules.play;

import dev.yanpgabriel.stellar.bot.Bot;
import dev.yanpgabriel.stellar.services.BotService;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
@ServerEndpoint("/bot/socket/{idServidor}")
public class PlayerSocket {

    @Inject
    BotService botService;
    
    private static final Logger LOG = Logger.getLogger(PlayerSocket.class);

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("idServidor") String idServidor) {
        sessions.put(idServidor, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("idServidor") String idServidor) {
        sessions.remove(idServidor);
        broadcast("User " + idServidor + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("idServidor") String idServidor, Throwable throwable) {
        sessions.remove(idServidor);
        LOG.error("onError", throwable);
        broadcast("User " + idServidor + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("idServidor") String idServidor) {
        String[] split = message.split(" ", 2);
        String cmd = split[0].toLowerCase();
        
        if (!message.startsWith(Bot.PREFIX)) {
            return;
        }

        Guild guildById = botService.getJDA().getGuildById(idServidor);
        
        broadcast(message);
        Bot.getInstance().getComandos()
                .stream()
                .filter(comando -> StringUtils.equalsIgnoreCase(comando.getName(), cmd.substring(1)))
                .findFirst()
                .ifPresent(comando -> {
                    try {
                        comando.runSocket(this, guildById, split);
                    } catch (Exception e) {
                        broadcast(e.getMessage());
                    }
                });
    }

    public void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
    
}
