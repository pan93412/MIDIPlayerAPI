package dev.itsu.midiplayerapi.core;

import dev.itsu.midiplayerapi.entity.model.instrument.DefaultResourcePackSet;
import dev.itsu.midiplayerapi.entity.model.instrument.InstrumentSetBase;
import dev.itsu.midiplayerapi.entity.model.instrument.Instruments;
import dev.itsu.midiplayerapi.entity.model.instrument.NoteBlockSet;
import dev.itsu.midiplayerapi.event.executor.EventExecutorService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MIDIPlayerAPI {

    private EventExecutorService eventExecutorService;
    private Map<String, InstrumentSetBase> instruments;

    private MIDIPlayerAPI() {
        eventExecutorService = new EventExecutorService();

        instruments = new HashMap<>();
        addInstrumentSet(Instruments.INSTRUMENT_SET_NOTE, new NoteBlockSet());
        addInstrumentSet(Instruments.INSTRUMENT_SET_DEFAULT, new DefaultResourcePackSet());
    }

    public EventExecutorService getEventExecutorService() {
        return eventExecutorService;
    }

    public void addInstrumentSet(String name, InstrumentSetBase instrumentSet) {
        instruments.put(name, instrumentSet);
    }

    public InstrumentSetBase getInstrumentSet(String name) {
        return instruments.get(name);
    }

    public Set<String> getInstrumentSetKeys() {
        return instruments.keySet();
    }

    public static class Factory {
        private static MIDIPlayerAPI singleton = new MIDIPlayerAPI();
        public static MIDIPlayerAPI getSingleton() {
            return singleton;
        }
    }

}
