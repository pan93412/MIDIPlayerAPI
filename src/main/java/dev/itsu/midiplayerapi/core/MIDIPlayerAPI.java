package dev.itsu.midiplayerapi.core;

import dev.itsu.midiplayerapi.event.executor.EventExecutorService;

public class MIDIPlayerAPI {

    private EventExecutorService eventExecutorService;

    private MIDIPlayerAPI() {
        eventExecutorService = new EventExecutorService();
    }

    public EventExecutorService getEventExecutorService() {
        return eventExecutorService;
    }

    public static class Factory {
        private static MIDIPlayerAPI singleton = new MIDIPlayerAPI();
        public static MIDIPlayerAPI getSingleton() {
            return singleton;
        }
    }

}
