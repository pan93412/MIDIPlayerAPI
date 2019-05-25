package dev.itsu.midiplayerapi.entity.model.instrument;

public class DefaultResourcePackSet extends InstrumentSetBase {
    @Override
    public String getInstrument(int channel) {
        return "music.0";
    }

    @Override
    public String getName() {
        return "DefaultResourcePackSet";
    }
}
