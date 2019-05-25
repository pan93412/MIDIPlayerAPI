package dev.itsu.midiplayerapi;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.plugin.PluginBase;
import dev.itsu.midiplayerapi.core.MIDILoader;
import dev.itsu.midiplayerapi.core.MIDIPlayerAPI;
import dev.itsu.midiplayerapi.entity.MIDIPlayer;
import dev.itsu.midiplayerapi.entity.PlayList;
import dev.itsu.midiplayerapi.entity.model.instrument.DefaultResourcePackSet;
import dev.itsu.midiplayerapi.entity.model.instrument.Instruments;
import dev.itsu.midiplayerapi.entity.model.instrument.NoteBlockSet;
import dev.itsu.midiplayerapi.event.SetPauseEvent;
import dev.itsu.midiplayerapi.event.StartPlayingEvent;
import dev.itsu.midiplayerapi.event.StopPlayingEvent;
import dev.itsu.midiplayerapi.event.listener.MIDIEventHandler;
import dev.itsu.midiplayerapi.event.listener.MIDIEventListener;

import java.io.File;

public class Main extends PluginBase implements Listener, MIDIEventListener {

    private static Main instance;
    private MIDIPlayer midiPlayer;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(this, this);
        MIDIPlayerAPI.Factory.getSingleton().getEventExecutorService().addMIDIEventListener(this);

        Instruments.setInstrumentSet(new DefaultResourcePackSet());

        getLogger().info("Enabled.");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        /*
        if (midiPlayer != null) {
            if (midiPlayer.isPausing()) {
                midiPlayer.setPause(false);
            } else {
                midiPlayer.setPause(true);
            }

        } else {
            try {
                midiPlayer = new MIDIPlayer(null, MIDILoader.fromInputStream("ピースサイン", this.getClass().getClassLoader().getResourceAsStream("Peace_Sign.mid")), true);
                midiPlayer.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        if (list != null) {
            list.getMidiPlayer().stop();
            return;
        }

        list = new PlayList(true, true);
        File file = new File("playlist");
        for (File f : file.listFiles()) {
            list.add(f.getName(), f.getAbsolutePath());
        }
        list.playFromFirst();
    }
    PlayList list;


    @MIDIEventHandler
    public void onStart(StartPlayingEvent event) {
        Server.getInstance().broadcastMessage("Started: " + event.getMidiPlayer().getMidiMusic().getTitle());
    }

    @MIDIEventHandler
    public void onStopped(StopPlayingEvent event) {
        Server.getInstance().broadcastMessage("Stopped: " + event.getMidiPlayer().getMidiMusic().getTitle());
    }

    @MIDIEventHandler
    public void onPaused(SetPauseEvent event) {
        Server.getInstance().broadcastMessage("Paused: " + event.getMidiPlayer().isPausing());
    }

    public static Main getInstance() {
        return instance;
    }
}
