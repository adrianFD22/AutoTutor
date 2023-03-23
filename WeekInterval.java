package schedule;

import androidx.annotation.NonNull;


/*
    This class represents an interval of time within the circle of [0,1080) corresponding to each minute
    of an entire week.

    Public methods:
        - WeekInterval(int start, int end): initializes an interval with given start and end.
        - WeekInterval(WeekInterval interval): initializes an interval by cloning another one.

        - boolean equals(WeekInterval interval): overrides the equal method.
        - boolean collide(WeekInterval interval): returns if the interval collides (overlaps or abuts) with the current interval.

        - Getters:
            - int getStart()
            - int getEnd()
            - int getLength()
            - int getDayStartOfInterval()
 */

public class WeekInterval {

    //-----------------------------------------
    //        Attributes and constructor
    //-----------------------------------------

    static int MOD = 24*60*7;
    private int start; // Minute of the week when the interval starts
    private int end; // Minute of the week when the interval ends

    // Constructor
    public WeekInterval(int start, int end) throws Exception{
        // Check that parameters are correct
        this.isInstantValid(start); this.isInstantValid(end);
        this.isStartDistinctToEnd(start, end);

        // Assign attributes
        this.start = start;
        this.end = end;
    }

    // Constructor
    public WeekInterval(WeekInterval interval) {
        this.start = interval.getStart();
        this.end = interval.getEnd();
    }


    //-----------------------------------------
    //                Methods
    //-----------------------------------------

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o)
            return true;
        // null check
        if (o == null)
            return false;
        // type check and cast
        if (this.getClass() != o.getClass())
            return false;

        WeekInterval interval = (WeekInterval) o;
        // field comparison
        return interval.start == this.start &&
                interval.end == this.end;
    }

    // Get the modular congruence
    private int getModularCongruence(int x) {
        int shift = (int) Math.ceil(Math.abs((float) x/MOD))*MOD;
        return (x + shift) % MOD;
    }

    // Check if two intervals collide // Check out
    public boolean collide(@NonNull WeekInterval interval) {
        // Check if timestamps (starts and ends) are ordered in a neighborhood of the circle homeomorph to a line
        int[] timeStamps = {
                0,
                getModularCongruence(this.end-this.start),
                getModularCongruence(interval.start - this.start),
                getModularCongruence(interval.end-this.start)};

        for (int i=1; i<timeStamps.length; i++) {
            if (timeStamps[i] <= timeStamps[i-1]) {
                return false;
            }
        }

        return true;
    }


    // Getters
    public int getStart() {
        return this.start;
    }

    public int getEnd() {
        return this.end;
    }

    public int getLength() {
        return this.end - this.start;
    }

    public int getDayStartOfInterval() {
        return this.start/(24*60);
    }


    // Check that the parameters are correct
    private void isInstantValid(int instant) throws Exception {
        if (! (instant >= 0 && instant < MOD )) {
            throw new Exception("Invalid interval time");
        }
    }

    private void isStartDistinctToEnd(int start, int end) throws Exception {
        if (start == end) {
            throw new Exception("Invalid length of interval");
        }
    }
}

