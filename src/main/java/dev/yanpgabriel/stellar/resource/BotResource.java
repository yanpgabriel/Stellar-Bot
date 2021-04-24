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
    @Path("token")
    public String token() {
        return botService.getJDA().getToken();
    }
    
}
