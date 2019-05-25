# MIDIPlayerAPI
MIDI Player API for Nukkit.  
  
## Documentation
### Get MIDI Instance
<pre>MIDIPlayerAPI api = MIDIPlayerAPI.Factory.getSingleton();</pre>
  
### Load MIDI
<pre>
MIDIMusic music;
String name = "SONG_NAME";
music = MIDILoader.fromFile(name, File);
music = MIDILoader.fromURL(name, URL);
music = MIDILoader.fromPath(name, String);
music = MUDILoader.fromInputStream(name, InputStream);
</pre>
  
### Play, Stop and Pause MIDI
<pre>
MIDIMusic music;//loaded
MIDIPlayer midiPlayer = new MIDIPlayer(Player, music), boolean isBroadcast);
midiPlayer.play();
midiPlayer.stop();
midiPlayer.setPaused(boolean);
</pre>
  
### Add EventListener
<pre>
MIDIPlayerAPI api = MIDIPlayerAPI.Factory.getSingleton();
api.getEventExecutorService().addMIDIEventListener(MIDIEventListener);
</pre>
  
### Handle Event
<pre>
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
</pre>
  
### Set Instrument Set
You can define your own set.
<pre>
Instruments.setInstrumentSet(new DefaultResourcePackSet());
Instruments.setInstrumentSet(new NoteBlockSet());
</pre>

