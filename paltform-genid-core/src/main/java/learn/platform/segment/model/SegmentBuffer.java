package learn.platform.segment.model;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 双buffer
 */
public class SegmentBuffer {

    private String tag;
    private Segment[] segments;
    private volatile int currentPos;
    private volatile boolean nextReady;
    private volatile boolean initialized;
    private final AtomicBoolean threadRunning;
    private final ReadWriteLock lock;

    private int step;
    private long updateTimestamp;

    public SegmentBuffer(String tag) {
        this.tag = tag;
        segments = new Segment[]{new Segment(this), new Segment(this)};
        this.threadRunning = new AtomicBoolean(false);
        this.lock = new ReentrantReadWriteLock();
    }

    public Segment getCurrent() {
        return segments[currentPos];
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Segment[] getSegments() {
        return segments;
    }

    public void setSegments(Segment[] segments) {
        this.segments = segments;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public boolean isNextReady() {
        return nextReady;
    }

    public void setNextReady(boolean nextReady) {
        this.nextReady = nextReady;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    public AtomicBoolean getThreadRunning() {
        return threadRunning;
    }

    public int nextPos() {
        return (currentPos + 1) & 1;
    }

    public void switchPos() {
        this.currentPos = nextPos();
    }
}
