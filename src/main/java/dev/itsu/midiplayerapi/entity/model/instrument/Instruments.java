package dev.itsu.midiplayerapi.entity.model.instrument;

public class Instruments {

    private static InstrumentSetBase instrumentSet;

    public static final String INSTRUMENT_SET_NOTE = "note";
    public static final String INSTRUMENT_SET_DEFAULT = "default";

    static {
        instrumentSet = new NoteBlockSet();
    }

    public static String get(int channel) {
        return instrumentSet.getInstrument(channel);
    }

    public static InstrumentSetBase getInstrumentSet() {
        return instrumentSet;
    }

    public static void setInstrumentSet(InstrumentSetBase instrumentSet) {
        Instruments.instrumentSet = instrumentSet;
    }

}
