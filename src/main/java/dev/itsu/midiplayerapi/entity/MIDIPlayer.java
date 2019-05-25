package dev.itsu.midiplayerapi.entity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.network.protocol.PlaySoundPacket;
import cn.nukkit.scheduler.AsyncTask;
import dev.itsu.midiplayerapi.plugin.Main;
import dev.itsu.midiplayerapi.core.MIDIPlayerAPI;
import dev.itsu.midiplayerapi.entity.model.MIDIMusic;
import dev.itsu.midiplayerapi.entity.model.instrument.Instruments;
import dev.itsu.midiplayerapi.event.SetPauseEvent;
import dev.itsu.midiplayerapi.event.StartPlayingEvent;
import dev.itsu.midiplayerapi.event.StopPlayingEvent;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MIDIPlayer {

    private Player player;
    private MIDIMusic midiMusic;
    private boolean isBroadcast;

    private ExecutorService service;
    private List<TrackPlayer> trackPlayers;
    private List<Future<Boolean>> results;
    private int endCount;

    private boolean isPausing;
    private static boolean isStarted;

    public MIDIPlayer(Player player, MIDIMusic midiMusic, boolean isBroadcast) {
        this.player = player;
        this.midiMusic = midiMusic;
        this.isBroadcast = isBroadcast;
        this.trackPlayers = new ArrayList<>();
        this.results = new ArrayList<>();
    }

    public void play() {
        if (isStarted) {
            stop();
        }

        this.endCount = 0;
        isStarted = true;

        load();

        Server.getInstance().getScheduler().scheduleAsyncTask(Main.getInstance(), new AsyncTask() {
            @Override
            public void onRun() {
                try {
                    MIDIPlayerAPI.Factory.getSingleton().getEventExecutorService().execute(StartPlayingEvent.class, MIDIPlayer.this);
                    //results = service.invokeAll(trackPlayers);
                    for (TrackPlayer trackPlayer : trackPlayers) {
                        results.add(service.submit(trackPlayer));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (results != null) {
                    for (Future<Boolean> result : results) {
                        try {
                            if (result.get()) endCount++;
                            if (endCount == midiMusic.getTracks().size()) {
                                isStarted = false;
                                MIDIPlayerAPI.Factory.getSingleton().getEventExecutorService().execute(StopPlayingEvent.class, MIDIPlayer.this, Boolean.TRUE);
                            }
                            service.shutdown();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void stop() {
        service.shutdownNow();
        isStarted = false;
        MIDIPlayerAPI.Factory.getSingleton().getEventExecutorService().execute(StopPlayingEvent.class, this, Boolean.FALSE);
    }

    public void setPause(boolean isPausing) {
        this.isPausing = isPausing;
        trackPlayers.forEach(trackPlayer -> {
            trackPlayer.setPaused(isPausing);
        });
        MIDIPlayerAPI.Factory.getSingleton().getEventExecutorService().execute(SetPauseEvent.class, this);
    }

    public Player getPlayer() {
        return player;
    }

    public MIDIMusic getMidiMusic() {
        return midiMusic;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isPausing() {
        return isPausing;
    }

    public boolean isBroadcast() {
        return isBroadcast;
    }

    private void load() {
        service = Executors.newFixedThreadPool(midiMusic.getTracks().size());
        trackPlayers.clear();

        for (Track track : midiMusic.getTracks()) {
            TrackPlayer trackPlayer = new TrackPlayer() {
                @Override
                protected Boolean run() {
                    MidiEvent previous;
                    int size = track.size();
                    int i = 0;

                    while (i < size) {
                        if (i == 0) {
                            i++;
                            continue;
                        }

                        try {
                            synchronized (this) {
                                if (this.isPaused()) {
                                    wait();
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        MidiEvent event = track.get(i);
                        previous = track.get(i - 1);

                        try {
                            Thread.sleep(Math.abs(Math.round((previous.getTick() - event.getTick()) * midiMusic.getTickPerSecond())));
                        } catch (InterruptedException e) {
                            break;
                        }

                        if (event.getMessage() instanceof ShortMessage) {
                            ShortMessage message = (ShortMessage) event.getMessage();
                            switch (message.getCommand()) {
                                case MIDIMusic.NOTE_ON: {
                                    if (!isBroadcast) {
                                        PlaySoundPacket pk = new PlaySoundPacket();
                                        pk.volume = (float) message.getData2() / 100f;
                                        pk.pitch = (float) Math.pow(2, (message.getData1() - 60) / 12d);
                                        pk.x = (int) player.x;
                                        pk.y = (int) player.y;
                                        pk.z = (int) player.z;
                                        pk.name = Instruments.get(message.getChannel());
                                        player.dataPacket(pk);

                                    } else {
                                        Server.getInstance().getOnlinePlayers().values().forEach(player1 -> {
                                            PlaySoundPacket pk = new PlaySoundPacket();
                                            pk.volume = (float) message.getData2() / 100f;
                                            pk.pitch = (float) Math.pow(2, (message.getData1() - 60) / 12d);
                                            pk.x = (int) player1.x;
                                            pk.y = (int) player1.y;
                                            pk.z = (int) player1.z;
                                            pk.name = Instruments.get(message.getChannel());
                                            player1.dataPacket(pk);
                                        });
                                    }
                                    break;
                                }

                                case MIDIMusic.NOTE_OFF: {
                                    /*
                                     StopSoundPacket pk = new StopSoundPacket();
                                     pk.name = getInstrumentSet(message.getChannel());
                                     player.dataPacket(pk);*/
                                    break;
                                }
                            }
                        }
                        i++;
                    }
                    return true;
                }
            };

            trackPlayers.add(trackPlayer);
        }
    }
}
