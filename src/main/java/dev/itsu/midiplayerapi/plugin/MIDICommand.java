package dev.itsu.midiplayerapi.plugin;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import dev.itsu.midiplayerapi.core.MIDILoader;
import dev.itsu.midiplayerapi.core.MIDIPlayerAPI;
import dev.itsu.midiplayerapi.entity.MIDIPlayer;
import dev.itsu.midiplayerapi.entity.model.instrument.Instruments;

import java.io.File;

public class MIDICommand extends Command {

    private MIDIPlayer player;

    public MIDICommand() {
        super("midi", "Play MIDI Command", "/midi <play [name] | stop | pause | instrument [note | default] | instruments | list>");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if (args.length == 0) return false;
        switch (args[0]) {
            case "list":
                File dir = new File("plugins/MIDIPlaylist");

                if (!dir.exists()) {
                    commandSender.sendMessage(TextFormat.RED + "Playlist (plugins/MIDIPlaylist/) is not found.");
                    return true;
                }

                commandSender.sendMessage(TextFormat.GREEN + "MIDI Playlist: (" + dir.listFiles().length + ")");
                for (File file : dir.listFiles()) {
                    commandSender.sendMessage(file.getName().replaceAll(".mid", ""));
                }
                return true;

            case "play":
                if (args.length != 2) {
                    commandSender.sendMessage(TextFormat.RED + "Usage: /midi play [name]");
                    return true;
                }

                File file = new File("plugins/MIDIPlaylist/" + args[1] + ".mid");
                if (!file.exists()) {
                    commandSender.sendMessage(TextFormat.RED + "The music is not found.");
                    return true;
                }

                if (player != null) {
                    player.stop();
                }

                player = new MIDIPlayer(null, MIDILoader.fromFile(file.getName().replaceAll(".mid", ""), file), true);
                player.play();

                return true;

            case "stop":
                if (player == null || !player.isStarted()) {
                    commandSender.sendMessage(TextFormat.RED + "No music is playing.");
                    return true;
                }

                player.stop();

                return true;

            case "pause":
                if (player == null || !player.isStarted()) {
                    commandSender.sendMessage(TextFormat.RED + "No music is playing.");
                    return true;
                }
                player.setPause(!player.isPausing());
                return true;

            case "instrument":
                if (args.length != 2) {
                    commandSender.sendMessage(TextFormat.RED + "Usage: /midi instrument [name]");
                    return true;
                }

                if (MIDIPlayerAPI.Factory.getSingleton().getInstrumentSetKeys().contains(args[1])) {
                    Instruments.setInstrumentSet(MIDIPlayerAPI.Factory.getSingleton().getInstrumentSet(args[1]));
                    commandSender.sendMessage(TextFormat.GREEN + "Changed the instrument to \"" + args[1] + "\"");
                } else {
                    commandSender.sendMessage(TextFormat.RED + "The instrument name: \"" + args[1] + "\" is not found.");
                }

                return true;

            case "instruments":
                commandSender.sendMessage(TextFormat.GREEN + "Instruments: (" + MIDIPlayerAPI.Factory.getSingleton().getInstrumentSetKeys().size() + ")");
                for (String key : MIDIPlayerAPI.Factory.getSingleton().getInstrumentSetKeys()) {
                    commandSender.sendMessage(key);
                }
                return true;
        }
        return false;
    }

}
