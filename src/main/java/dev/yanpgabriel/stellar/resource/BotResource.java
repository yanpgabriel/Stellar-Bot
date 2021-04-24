package dev.yanpgabriel.stellar.resource;

import dev.yanpgabriel.stellar.services.BotService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("bot")
public class BotResource {
    
    @Inject
    BotService botService;

    @GET
    public String bot() {
        return botService.getJDA().getSelfUser().getName();
    }
    
}
