package dev.itsu.midiplayerapi.core;

import dev.itsu.midiplayerapi.entity.model.MIDIMusic;

import javax.sound.midi.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MIDILoader {

    public static MIDIMusic fromURL(String name, URL url) throws javax.sound.midi.MidiUnavailableException {
        try {
            return fromInputStream(name, new FileInputStream(new File(url.toURI())));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MIDIMusic fromPath(String name, String path) throws javax.sound.midi.MidiUnavailableException {
        return fromFile(name, new File(path));
    }

    /*
    public static MIDIMusic fromNetwork(String name, URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.connect();

            int httpStatusCode = conn.getResponseCode();

            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception();
            }

            // Input Stream
            DataInputStream stream = new DataInputStream(conn.getInputStream());
            ByteArrayOutputStream bout = new ByteArrayOutputStream();

            byte [] buffer = new byte[1024];
            while(true) {
                int len = stream.read(buffer);
                if(len < 0) {
                    break;
                }
                bout.write(buffer, 0, len);
            }

            stream.close();
            bout.close();

            return fromInputStream(name, new ByteArrayInputStream(bout.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    */

    public static MIDIMusic fromFile(String name, File file) throws javax.sound.midi.MidiUnavailableException {
        try {
            return fromInputStream(name, new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MIDIMusic fromInputStream(String name, InputStream stream) throws javax.sound.midi.MidiUnavailableException {
        try {
            List<Track> tracks;
            double tickPerSecond;

            BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);
            MidiFileFormat format = MidiSystem.getMidiFileFormat(bufferedInputStream);
            Sequence sequence = MidiSystem.getSequence(bufferedInputStream);

            stream.close();
            bufferedInputStream.close();

            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.setSequence(sequence);

            tracks = Arrays.asList(sequence.getTracks());

            if (sequence.getDivisionType() == Sequence.PPQ) {
                tickPerSecond = 1000d / (format.getResolution() * (sequencer.getTempoInBPM() / 60d));

            } else {
                double framePerSecond =
                        (sequence.getDivisionType() == Sequence.SMPTE_24 ? 24
                                : (sequence.getDivisionType() == Sequence.SMPTE_25 ? 25
                                : (sequence.getDivisionType() == Sequence.SMPTE_30 ? 30
                                : (sequence.getDivisionType() == Sequence.SMPTE_30DROP ? 29.97 : 29.97))));
                tickPerSecond = format.getResolution() * framePerSecond;
            }

            return new MIDIMusic(name, tracks, tickPerSecond, TimeUnit.MICROSECONDS.toSeconds(format.getMicrosecondLength()));

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
