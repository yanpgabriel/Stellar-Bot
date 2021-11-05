package dev.yanpgabriel.stellar.bot;

public enum BotReaction {
    
    DEDO_DO_MEIO("\uD83E\uDD14"),
    TOPPEN("\uD83D\uDC4C"),
    ;
    
    private String emoji;
    
    BotReaction(String emoji) {
        this.emoji = emoji;
    }

    @Override
    public String toString() {
        return emoji;
    }

}
