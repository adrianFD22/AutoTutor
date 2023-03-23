package schedule;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.stream.Collectors;


/*
    This class provides a programming interface for selecting the interval of free hours during a
    week. Each selected hour is represented by an interval. These do not collide with each other.
    In the internal representation of the class, week starts at Monday (0) and ends at Sunday (6).

    Package-protected methods:
        - WeekSchedule(): initializes an empty schedule.

        - boolean add(WeekInterval interval): adds a new non colliding interval to the schedule.
        - boolean remove(WeekInterval interval): removes the selected interval.
        - ArrayList<WeekInterval> getScheduleOfDay(int day): returns the intervals that start at the selected day.
 */

class WeekSchedule {

    //-----------------------------------------
    //        Attributes and constructor
    //-----------------------------------------

    private ArrayList<WeekInterval> schedule; // Hours of each day
    private int maxLengthOfIntervals;

    // Constructor
    WeekSchedule() {
        this.schedule = new ArrayList<WeekInterval>();
        this.maxLengthOfIntervals = 0;
    }


    //-----------------------------------------
    //                Methods
    //-----------------------------------------

    // Add a disjoint interval to the available hours. Return true if has been possible to add it.
    boolean add(@NonNull WeekInterval interval) {
        // Check if collides with other interval
        for (WeekInterval currentInterval: schedule) {
            if (interval.collide(currentInterval)) {
                return false;
            }
        }

        // Add the interval
        schedule.add(interval);

        // Update the max_gap_length
        this.maxLengthOfIntervals = Math.max(this.maxLengthOfIntervals, interval.getLength());

        return true;
    }

    // Remove an interval from the list
    boolean remove(@NonNull WeekInterval interval) {
        // Check if it is possible to remove the interval
        boolean removed = schedule.remove(interval);

        // Update the max_gap_length if necessary
        if (removed) {
            this.maxLengthOfIntervals = 0;
            for (WeekInterval current_interval: schedule) {
                this.maxLengthOfIntervals = Math.max(this.maxLengthOfIntervals, current_interval.getLength());
            }
        }

        return removed;
    }


    // Getters

    int getMaxLengthOfIntervals() {
        return this.maxLengthOfIntervals;
    }

    ArrayList<WeekInterval> getSchedule() {
        return this.schedule;
    }
}
