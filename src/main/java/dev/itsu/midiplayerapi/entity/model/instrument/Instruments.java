package dev.itsu.midiplayerapi.entity.model.instrument;

public class Instruments {

    private static InstrumentSetBase instrumentSet;

    static {
        instrumentSet = new DefaultResourcePackSet();
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
