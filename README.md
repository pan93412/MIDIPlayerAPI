# MIDIPlayerAPI
MIDI Player API for Nukkit.   
__In development!!__  
  
## Use as a Nukkit plugin
- Download jar or Build as Maven project.
- Create directory NUKKIT_ROOT/plugins/MIDIPlaylist.
- Put some *.mid files into the directory.
- Download resourcepack from "releases".
- Put the resourcepack into NUKKIT_ROOT/resource_packs.
- Run your server.
  
## Commands  
- /midi list  
List MIDI Files which are able to play.  
  
- /midi play [name]  
Play MIDI file.  
  
- /midi stop  
Stop playing.  
  
- /midi pause  
Pause playing.  
  
- /midi instruments  
List Instruments which are able change.  
  
- /midi instrument [name]  
Change an instrument to play MIDI.  
  
## Documentation
### Get MIDI API Instance
<pre>MIDIPlayerAPI api = MIDIPlayerAPI.Factory.getSingleton();</pre>
  
### Load MIDI
<pre>
MIDIMusic music;
String name = "SONG_NAME";
music = MIDILoader.fromFile(name, File);
music = MIDILoader.fromURL(name, URL);
music = MIDILoader.fromPath(name, String);
music = MIDILoader.fromInputStream(name, InputStream);
</pre>
  
### Play, Stop and Pause MIDI
<pre>
MIDIMusic music;//loaded
MIDIPlayer midiPlayer = new MIDIPlayer(Player, music), boolean isBroadcast);
midiPlayer.play();
midiPlayer.stop();
midiPlayer.setPaused(boolean);
</pre>
  
### Handle Event
<pre>
class MyEventListener implements MIDIEventListener {
    @MIDIEventHandler
    public void onMIDIStart(StartPlayingEvent event) {
        Server.getInstance().broadcastMessage("Started: " + event.getMidiPlayer().getMidiMusic().getTitle());
    }
    
    @MIDIEventHandler
    public void onMIDIStopped(StopPlayingEvent event) {
        Server.getInstance().broadcastMessage("Stopped: " + event.getMidiPlayer().getMidiMusic().getTitle());
    }
    
    @MIDIEventHandler
    public void onSetPaused(SetPauseEvent event) {
        Server.getInstance().broadcastMessage("Paused: " + event.getMidiPlayer().isPausing());
    }
}

//Add EventListener
void onEnable() {
    MIDIPlayerAPI api = MIDIPlayerAPI.Factory.getSingleton();
    api.getEventExecutorService().addMIDIEventListener(new MyEventListener());
}
</pre>
  
### Set InstrumentSet
You can change instruments to play and define your own set.  
  
__Change InstrumentSet__
<pre>
Instruments.setInstrumentSet(Instruments.INSTRUMENT_SET_NOTE);//note block
Instruments.setInstrumentSet(Instruments.INSTRUMENT_SET_DEFAULT);//default resource pack
</pre>
  
__Define your original InstrumentSet__
<pre>
class MyInstrumentSet extends InstrumentSetBase {
    @Override
    public String getInstrument(int midiChannel) {
        //define your instrument
        //@See dev/itsu/midiplayerapi/entity/model/instrument/NoteBlockSet.java or DefaultResourcePackSet.java
        return "music.MY_INSTRUMENT_1"; // return instrument's name in your resourcepack.
    }
}

void onEnable() {
    MIDIPlayerAPI api = MIDIPlayerAPI.Factory.getSingleton();
    api.addInstrumentSet("MyInstrumentSet", new MyInstrumentSet());
    Instruments.setInstrumentSet("MyInstrumentSet");
}
</pre>

