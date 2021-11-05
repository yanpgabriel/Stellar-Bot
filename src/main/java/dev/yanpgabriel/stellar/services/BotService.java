package dev.yanpgabriel.stellar.services;

import dev.yanpgabriel.stellar.bot.Bot;
import dev.yanpgabriel.stellar.bot.annotations.StellarCommand;
import dev.yanpgabriel.stellar.bot.commands.AbstractCmd;
import dev.yanpgabriel.stellar.bot.events.StellarMessageReceivedEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.security.auth.login.LoginException;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class BotService {
    
    Logger logger = LoggerFactory.getLogger(BotService.class);
    
    @ConfigProperty(name = "discord.bot.token")
    String token;
    
    private static JDA jda;

    public JDA getJDA() {
        return jda;
    }
    
    public JDA init() throws LoginException, InterruptedException {
        if (Objects.isNull(jda)) {
            logger.info("Bot iniciando...");
            jda = configureMemoryUsage(JDABuilder.createDefault(token)
                .setBulkDeleteSplittingEnabled(false)
                .setActivity(Activity.streaming("nada o.O", "https://www.twitch.tv/yanznho"))
                .addEventListeners(new StellarMessageReceivedEvent()))
                .build();
            registerCommands();
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

    public void registerCommands() {
        Set<AbstractCmd> commands = getInstantiatedClassesInPath("dev.yanpgabriel.stellar.bot.commands", StellarCommand.class);
        commands.forEach(command -> {
            String className = command.getClass().getSimpleName();
            command.setName(StringUtils.removeEndIgnoreCase(className, "Cmd"));
        });
        Bot.getInstance().registrarComandos(commands);
    }
    
    private static <T> Set<T> getInstantiatedClassesInPath(String path, Class<? extends Annotation> classType) {
        Reflections reflections = new Reflections(path);
        return reflections.getTypesAnnotatedWith(classType).stream()
                .filter(Objects::nonNull)
                .filter(clazz -> Objects.nonNull(clazz.getAnnotation(classType)))
                .map(clazz -> (T) createInstanceFromClass(clazz))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static <T> T createInstanceFromClass(Class<?> clazz) {
        try {
            return (T) clazz.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            // MessageUtils.sendDebugMessage("Falha ao criar instancia da classe '%s'.", clazz.getName());
            return null;
        }
    }
}
