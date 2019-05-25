package dev.itsu.midiplayerapi.event;

import dev.itsu.midiplayerapi.entity.MIDIPlayer;

public class SetPauseEvent extends MIDIEvent {

    private MIDIPlayer midiPlayer;

    public SetPauseEvent(MIDIPlayer midiPlayer) {
        this.midiPlayer = midiPlayer;
    }

    public MIDIPlayer getMidiPlayer() {
        return midiPlayer;
    }

}
