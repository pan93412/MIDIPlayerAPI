package dev.itsu.midiplayerapi.entity.model;

import javax.sound.midi.Track;
import java.util.List;

public class MIDIMusic {

    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;

    private String title;
    private List<Track> tracks;
    private double tickPerSecond;
    private long length;

    public MIDIMusic(String title, List<Track> tracks, double tickPerSecond, long length) {
        this.title = title;
        this.tracks = tracks;
        this.tickPerSecond = tickPerSecond;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public double getTickPerSecond() {
        return tickPerSecond;
    }

    public long getLength() {
        return length;
    }
}
