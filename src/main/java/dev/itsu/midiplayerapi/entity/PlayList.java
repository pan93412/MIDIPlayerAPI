package dev.itsu.midiplayerapi.entity;

import cn.nukkit.Player;
import dev.itsu.midiplayerapi.core.MIDILoader;
import dev.itsu.midiplayerapi.core.MIDIPlayerAPI;
import dev.itsu.midiplayerapi.event.StopPlayingEvent;
import dev.itsu.midiplayerapi.event.listener.MIDIEventHandler;
import dev.itsu.midiplayerapi.event.listener.MIDIEventListener;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Objects;

public class PlayList {

    private String name;
    private LinkedHashMap<String, String> playList;
    private MIDIPlayer midiPlayer;
    private int index;

    private boolean isBroadcast;
    private boolean autoplay;
    private Player player;

    public PlayList(boolean isBroadcast, boolean autoplay) {
        this(null, isBroadcast, autoplay);
    }

    public PlayList(String name, boolean isBroadcast, boolean autoplay) {
        this.name = name;
        this.isBroadcast = isBroadcast;
        this.autoplay = autoplay;
        this.playList = new LinkedHashMap<>();
        if (autoplay) MIDIPlayerAPI.Factory.getSingleton().getEventExecutorService().addMIDIEventListener(new EventListener());
    }

    public void add(String name, String path) {
        playList.put(name, path);
    }

    public void remove(String name) {
        playList.remove(name);
    }

    public LinkedHashMap getPlayList() {
        return this.playList;
    }

    public void playFromFirst() throws javax.sound.midi.MidiUnavailableException {
        index = 0;
        play();
    }

    public void playAt(int index) throws javax.sound.midi.MidiUnavailableException {
        this.index = index;
        play();
    }

    public void playNext() throws javax.sound.midi.MidiUnavailableException {
        index++;
        if (index == playList.size()) {
            index = 0;
        }
        play();
    }

    public void playPrevious() throws javax.sound.midi.MidiUnavailableException {
        index--;
        if (index == -1) {
            index = playList.size() - 1;
        }
        play();
    }

    public String getNowPlaying() {
        LinkedList<String> list = new LinkedList<>(playList.keySet());
        return playList.get(list.get(index));
    }

    private void play() throws javax.sound.midi.MidiUnavailableException {
        LinkedList<String> list = new LinkedList<>(playList.keySet());
        midiPlayer = new MIDIPlayer(isBroadcast ? null : player, MIDILoader.fromPath(list.get(index), playList.get(list.get(index))), isBroadcast);
        midiPlayer.play();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isAutoplay() {
        return autoplay;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    public MIDIPlayer getMidiPlayer() {
        return midiPlayer;
    }

    class EventListener implements MIDIEventListener {
        @MIDIEventHandler
        public void onStopped(StopPlayingEvent event) throws javax.sound.midi.MidiUnavailableException {
            if (event.isPlayedToEnd()) {
                if (Objects.equals(event.getMidiPlayer(), midiPlayer)) playNext();
            }
        }
    }
}
