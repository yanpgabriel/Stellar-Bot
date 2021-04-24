package dev.yanpgabriel.stellar;

import dev.yanpgabriel.stellar.services.BotService;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.security.auth.login.LoginException;

@ApplicationScoped
public class ApplicationLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger("ApplicationLifecycle");

    @Inject
    BotService botService;

    void onStart(@Observes StartupEvent event) throws LoginException, InterruptedException {
        LOGGER.info("INICIANDO APLICAÇÃO!");
        botService.init();
    }
    void onStop(@Observes ShutdownEvent event) {
        botService.getJDA().getPresence().setIdle(true);
        botService.getJDA().shutdownNow();
        LOGGER.info("PARANDO APLICAÇÃO!");
    }
}
