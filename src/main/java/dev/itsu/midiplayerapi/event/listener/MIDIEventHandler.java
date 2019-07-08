package dev.itsu.midiplayerapi.event.listener;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MIDIEventHandler {
}
