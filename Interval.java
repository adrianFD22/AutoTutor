package schedule;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


/*
    This class encapsulates the information referring to an interval in time.

    - Immutable

    Public methods:
        - Getters
            -
 */

public class Interval {

    //-----------------------------------------
    //        Attributes and constructor
    //-----------------------------------------

    private LocalDateTime start, end;
    private int length;

    // Constructor from timestamps
    Interval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;

        this.length = (int) Duration.between(this.start, this.end).toMinutes();
    }

    // Constructor from start and length
    Interval(LocalDateTime start, int length) {
        this.start = start;
        this.end = start.plus((long) length, ChronoUnit.MINUTES);

        this.length = length;
    }


    //-----------------------------------------
    //             Static methods
    //-----------------------------------------

    public static int formatLocalDateTime(LocalDateTime date) {
        int min = date.getMinute();
        int hour = date.getHour();
        int dayOfTheWeek = date.getDayOfWeek().getValue()-1;

        int minOfTheWeek = min + hour*60 + dayOfTheWeek*24*60;
        return minOfTheWeek;
    }


    //-----------------------------------------
    //          Rest of the methods
    //-----------------------------------------

    // Getters
    public LocalDateTime getStart() {
        return this.start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    public int getLength() {
        return this.length;
    }
}
