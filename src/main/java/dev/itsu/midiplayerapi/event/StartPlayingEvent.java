package dev.itsu.midiplayerapi.event;

import dev.itsu.midiplayerapi.entity.MIDIPlayer;

public class StartPlayingEvent extends MIDIEvent {

    private MIDIPlayer midiPlayer;

    public StartPlayingEvent(MIDIPlayer midiPlayer) {
        this.midiPlayer = midiPlayer;
    }

    public MIDIPlayer getMidiPlayer() {
        return midiPlayer;
    }
}
