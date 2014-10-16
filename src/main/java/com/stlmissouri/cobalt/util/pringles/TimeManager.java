package com.stlmissouri.cobalt.util.pringles;

/**
 * Sets up and manages basic timed events.
 *
 * @author Ramisme
 * @since Apr 26, 2013
 */
public final class TimeManager {
    private long current, last;

    /**
     * Update the timer.
     */
    public void updateTimer() {
        this.current = getCurrentMillis();
    }

    /**
     * Returns true if the delay has been completed.
     *
     * @param delay
     * @return
     */
    public boolean sleep(final long delay) {
        return (current - last > delay);
    }

    /**
     * @return the current
     */
    public final long getCurrent() {
        return current;
    }

    /**
     * @return the last
     */
    public final long getLast() {
        return last;
    }

    /**
     * @param current the current to set
     */
    public final void setCurrent(long current) {
        this.current = current;
    }

    /**
     * @param last the last to set
     */
    public final void setLast(long last) {
        this.last = last;
    }

    /**
     * Sets the last to the current time
     */
    public final void updateLast() {
        this.last = getCurrentMillis();
    }

    /**
     * @return the current time in milliseconds based on nano time.
     */
    public long getCurrentMillis() {
        return (System.nanoTime() / 1000000);
    }
}
