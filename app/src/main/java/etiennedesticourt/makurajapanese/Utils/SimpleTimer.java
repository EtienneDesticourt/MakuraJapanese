package etiennedesticourt.makurajapanese.Utils;

public class SimpleTimer {
    private long start;

    private SimpleTimer() {
        start = System.nanoTime();
    }

    public static SimpleTimer start() {
        return new SimpleTimer();
    }

    public int getDuration() {
        long end = System.nanoTime();
        long duration = end - start;
        return (int) (duration / 1000000000);
    }
}
