package dev.itsu.midiplayerapi.event.executor;

import dev.itsu.midiplayerapi.event.MIDIEvent;
import dev.itsu.midiplayerapi.event.listener.MIDIEventHandler;
import dev.itsu.midiplayerapi.event.listener.MIDIEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventExecutorService {

    private List<MIDIEventListener> handlers;

    public EventExecutorService() {
        this.handlers = new ArrayList<>();
    }

    public void addMIDIEventListener(MIDIEventListener listener) {
        handlers.add(listener);
    }

    public void removeMIDIEventListener(MIDIEventListener listener) {
        handlers.remove(listener);
    }

    public void execute(Class<? extends MIDIEvent> eventClass, Object... args) {
        for (MIDIEventListener listener : handlers) {
            Class<? extends MIDIEventListener> clazz = listener.getClass();
            for (Method method : clazz.getMethods()) {
                try {
                    if (method.isAnnotationPresent(MIDIEventHandler.class)) {
                        if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals(eventClass)) {
                            Class[] classes = new Class[args.length];
                            int i = 0;
                            for (Object arg : args) {
                                classes[i] = arg.getClass();
                                i++;
                            }
                            method.invoke(clazz.newInstance(), eventClass.getConstructor(classes).newInstance(args));
                        }
                    }
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
