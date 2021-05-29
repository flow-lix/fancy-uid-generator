package learn.platform.id;

import java.util.concurrent.atomic.AtomicLong;

public class Segment {

    private AtomicLong value = new AtomicLong(0);
    private long max;
    private int step;
    private SegmentBuffer buffer;

    public Segment(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    public AtomicLong getValue() {
        return value;
    }

    public void setValue(AtomicLong value) {
        this.value = value;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public SegmentBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(SegmentBuffer buffer) {
        this.buffer = buffer;
    }

    public long getIdle() {
        return getMax() - getValue().get();
    }
}
