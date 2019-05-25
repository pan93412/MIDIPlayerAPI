package dev.itsu.midiplayerapi.plugin;

import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import dev.itsu.midiplayerapi.core.MIDIPlayerAPI;

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
