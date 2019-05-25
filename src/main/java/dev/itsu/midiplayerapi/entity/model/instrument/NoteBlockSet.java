package dev.itsu.midiplayerapi.entity.model.instrument;

public class NoteBlockSet extends InstrumentSetBase {

    @Override
    public String getInstrument(int channel) {
        switch (channel) {
            case 113:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 114:
                return "note.pling";

            case 120:
                return "note.snare";

            case 115:
            case 116:
            case 117:
            case 118:
            case 119:
                return "note.bd";

            default:
                return "note.harp";

        }
    }

    @Override
    public String getName() {
        return "NoteBlockSet";
    }
}
