package com.chowkent.studyboss;

public class Stopwatch {
    private long startTime;
    private long stopTime;
    private long timeBuffer;
    private boolean running;

    public Stopwatch() {
        reset();
    }

    public void start() {
        if (!running) {
            running = true;
            startTime = System.currentTimeMillis();
        }
    }

    public void pause() {
        if (running) {
            running = false;
            stopTime = System.currentTimeMillis();
            updateBuffer();
        }
    }

    public void reset() {
        startTime = 0L;
        stopTime = 0L;
        timeBuffer = 0L;
        running = false;
    }

    public long getElapsedTime() {
        if (!running) {
            return timeBuffer;
        } else {
            return (System.currentTimeMillis() - startTime) + timeBuffer;
        }
    }

    public boolean isRunning() {
        return running;
    }

    private void updateBuffer() {
        if (!(timeBuffer > 0)) {
            timeBuffer = stopTime - startTime;
        } else {
            timeBuffer += stopTime - startTime;
        }
    }
}
