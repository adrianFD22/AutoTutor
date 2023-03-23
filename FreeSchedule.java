package schedule;

/*
    This class provides control for managing the next available hours and getting the next empty
    interval of given length.

    Package-protected:
        - popFirstInterval(int length, ...)
 */

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.stream.Collectors;

class FreeSchedule {

    //-----------------------------------------
    //       Attributes and constructor
    //-----------------------------------------

    // Schedules
    WeekSchedule weekSchedule;
    ArrayList<Interval> freeSchedule;

    // Date
    LocalDateTime currentDate, auxDate; // aux_date is used for creating new intervals in the freeSchedule

    // Constructor
    FreeSchedule(WeekSchedule weekSchedule, LocalDateTime date) {
        this.freeSchedule = new ArrayList<Interval>();
        this.weekSchedule = weekSchedule;
        this.currentDate = date;
        this.auxDate = date;
    }


    //-----------------------------------------
    //          Rest of the methods
    //-----------------------------------------

    // Enlarge the freeSchedule enough to access to the i-th position
    private void enlargeSchedule(int i) { // Watch out
        while (i >= freeSchedule.size()) { // Enlarge by adding weeks until the freeSchedule be large enough

            // aux_date
            int dayOfTheWeek = this.auxDate.getDayOfWeek().getValue() - 1;
            int minOfTheWeek = this.auxDate.getMinute() + this.auxDate.getHour() * 60 + dayOfTheWeek * 24;

            // Get the free weekIntervals of the week
            ArrayList<WeekInterval> schedule = this.weekSchedule.getSchedule();
            schedule = (ArrayList<WeekInterval>) schedule.stream().filter(interval -> interval.getStart() >= minOfTheWeek).collect(Collectors.toList());

            Interval interval;
            LocalDateTime start;
            int shift;

            // Turn the weekIntervals into intervals and add them
            for (WeekInterval weekInterval : schedule) {
                shift = weekInterval.getStart() - Interval.formatLocalDateTime(this.auxDate);
                start = this.auxDate.plus(shift, ChronoUnit.MINUTES);

                interval = new Interval(start, weekInterval.getLength());
                this.freeSchedule.add(interval);
            }

            // Update aux_date to monday 0:00 next week
            this.auxDate = this.auxDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            this.auxDate = LocalDate.from(this.auxDate).atTime(0, 0);
        }
    }

    // Find the next available interval to the position i
    private Interval getInterval(int i) {
        // Enlarge if necessary
        this.enlargeSchedule(i);

        // Return interval
        return this.freeSchedule.get(i);
    }

    // Get the first available gap of given length and starting after a given quantity of days and certain minute
    Interval popFirstInterval(int length, int daysBeforeStart) throws Exception {
        // Check that there is enough space available in the week schedule
        if (length > weekSchedule.getMaxLengthOfIntervals()) {
            throw new Exception("WeekSchedule object does not contain large enough intervals");
        }

        // Iterate to find the first available gap
        int i = 0;
        Interval interval;

        while (true) { //Watch out for this

            interval = this.getInterval(i);

            // Check if it is spaced enough
            int daysSpaced = (int) Duration.between(this.currentDate, interval.getStart()).toDays();

            if (daysSpaced >= daysBeforeStart) {
                // Check if the length is large enough
                if (interval.getLength() >= length) {
                    return interval;
                }
            }

            i++;
        }
    }

}
