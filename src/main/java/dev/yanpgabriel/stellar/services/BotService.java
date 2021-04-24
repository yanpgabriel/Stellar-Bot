package dev.yanpgabriel.stellar.services;

import dev.yanpgabriel.stellar.bot.events.StellarMessageReceivedEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.security.auth.login.LoginException;
import java.util.Objects;

@ApplicationScoped
public class BotService {
    
    Logger logger = LoggerFactory.getLogger(BotService.class);
    
    @ConfigProperty(name = "discord.bot.token")
    String token;
    
    private static JDA jda;
    
    public JDA init() throws LoginException, InterruptedException {
        if (Objects.isNull(jda)) {
            logger.info("Bot iniciando...");
            jda = configureMemoryUsage(JDABuilder.createDefault(token)
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(Activity.streaming("nada o.O", "https://www.twitch.tv/yanznho"))
                .addEventListeners(new StellarMessageReceivedEvent()))
                .build();
            logger.info("Bot em funcionamento!");
        } else {
            logger.info("Bot já iniciado!");
        }
        return jda.awaitReady();
    }

    public JDABuilder configureMemoryUsage(JDABuilder builder) {
        return builder
        .disableCache(CacheFlag.ACTIVITY)
        .setMemberCachePolicy(MemberCachePolicy.VOICE.or(MemberCachePolicy.OWNER))
        .setChunkingFilter(ChunkingFilter.NONE)
        .disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING)
        .setLargeThreshold(50);
    }

    public JDA getJDA() {
        return jda;
    }
}
