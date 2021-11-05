package dev.yanpgabriel.stellar.bot.annotations;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StellarCommand {
   
   String description() default "";
   String[] aliases() default {};

}

