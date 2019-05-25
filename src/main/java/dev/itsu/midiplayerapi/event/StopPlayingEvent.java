package dev.itsu.midiplayerapi.event;

import dev.itsu.midiplayerapi.entity.MIDIPlayer;

public class StopPlayingEvent extends MIDIEvent {

    private MIDIPlayer midiPlayer;
    private boolean playedToEnd;

    public StopPlayingEvent(MIDIPlayer midiPlayer, Boolean playedToEnd) {
        this.midiPlayer = midiPlayer;
        this.playedToEnd = playedToEnd;
    }

    public MIDIPlayer getMidiPlayer() {
        return midiPlayer;
    }

    public boolean isPlayedToEnd() {
        return playedToEnd;
    }
}
