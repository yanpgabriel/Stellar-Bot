package dev.yanpgabriel.stellar;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackState;
import dev.yanpgabriel.stellar.bot.modules.play.PlayerController;

public class MusicaDTO {
    
    private AudioTrackInfo info;
    private AudioTrackState state;
    private String minutagem;
    private String duracao;
    private Object outros;

    public MusicaDTO() {
    }

    public MusicaDTO(AudioTrack track) {
        this.info = track.getInfo();
        this.state = track.getState();
        this.minutagem = PlayerController.formatTime(track.getPosition());
        this.duracao = PlayerController.formatTime(track.getDuration());
        this.outros = track.getUserData();
    }

    public AudioTrackInfo getInfo() {
        return info;
    }

    public void setInfo(AudioTrackInfo info) {
        this.info = info;
    }

    public AudioTrackState getState() {
        return state;
    }

    public void setState(AudioTrackState state) {
        this.state = state;
    }

    public String getMinutagem() {
        return minutagem;
    }

    public void setMinutagem(String minutagem) {
        this.minutagem = minutagem;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public Object getOutros() {
        return outros;
    }

    public void setOutros(Object outros) {
        this.outros = outros;
    }
}
