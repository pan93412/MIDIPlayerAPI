package dev.itsu.midiplayerapi.plugin;

import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;
import dev.itsu.midiplayerapi.event.SetPauseEvent;
import dev.itsu.midiplayerapi.event.StartPlayingEvent;
import dev.itsu.midiplayerapi.event.StopPlayingEvent;
import dev.itsu.midiplayerapi.event.listener.MIDIEventHandler;
import dev.itsu.midiplayerapi.event.listener.MIDIEventListener;

public class EventListener implements MIDIEventListener {

    @MIDIEventHandler
    public void onStart(StartPlayingEvent event) {
        Server.getInstance().broadcastMessage(TextFormat.GREEN + "Started: " + event.getMidiPlayer().getMidiMusic().getTitle());
    }

    @MIDIEventHandler
    public void onStopped(StopPlayingEvent event) {
        Server.getInstance().broadcastMessage(TextFormat.GREEN + "Stopped: " + event.getMidiPlayer().getMidiMusic().getTitle());
    }

    @MIDIEventHandler
    public void onPaused(SetPauseEvent event) {
        if (!event.getMidiPlayer().isPausing()) Server.getInstance().broadcastMessage(TextFormat.GREEN + "Paused: " + event.getMidiPlayer().isPausing());
        else Server.getInstance().broadcastMessage(TextFormat.GREEN + "Started: " + event.getMidiPlayer().getMidiMusic().getTitle());
    }

}
