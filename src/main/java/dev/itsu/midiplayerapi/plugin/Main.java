package dev.itsu.midiplayerapi.plugin;

import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import dev.itsu.midiplayerapi.core.MIDILoader;
import dev.itsu.midiplayerapi.core.MIDIPlayerAPI;
import dev.itsu.midiplayerapi.entity.MIDIPlayer;
import dev.itsu.midiplayerapi.entity.model.instrument.Instruments;
import dev.itsu.midiplayerapi.entity.model.instrument.NoteBlockSet;
import dev.itsu.midiplayerapi.event.SetPauseEvent;
import dev.itsu.midiplayerapi.event.StartPlayingEvent;
import dev.itsu.midiplayerapi.event.StopPlayingEvent;
import dev.itsu.midiplayerapi.event.listener.MIDIEventHandler;
import dev.itsu.midiplayerapi.event.listener.MIDIEventListener;

import java.io.File;

public class Main extends PluginBase implements Listener {

    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getCommandMap().register("midi", new MIDICommand());
        MIDIPlayerAPI.Factory.getSingleton().getEventExecutorService().addMIDIEventListener(new EventListener());

        new File("plugins/MIDIPlaylist/").mkdirs();

        getLogger().info("Enabled.");
    }

    public static Main getInstance() {
        return instance;
    }
}
