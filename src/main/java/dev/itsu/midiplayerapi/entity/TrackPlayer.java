package dev.itsu.midiplayerapi.entity;

import java.util.concurrent.Callable;

abstract class TrackPlayer implements Callable<Boolean> {

    private boolean isPaused;

    protected abstract Boolean run();

    @Override
    public Boolean call() {
        return run();
    }

    synchronized final void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
        if (!isPaused) notifyAll();
    }

    boolean isPaused() {
        return isPaused;
    }
}
