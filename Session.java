package schedule;


/*
    This class is a wrapper for the information related to a session of a given subject. In the
    future we can consider implementing child classes of this one that extend Session with methods
    that generate content for the session as tests, exercises, etc.

    - Immutable

    Public methods:
        - Getters
            - subject
            - type_of_session
            - start
            - end
 */

import java.time.LocalDateTime;

public class Session {

    //-----------------------------------------
    //        Attributes and constructor
    //-----------------------------------------

    // Information about the session
    private Subject subject;
    private String typeOfSession;

    // Time of the session
    private Interval interval;

    // Constructor
    Session(Subject subject, String typeOfSession, Interval interval) {
        this.subject = subject;
        this.typeOfSession = typeOfSession;
        this.interval = interval;
    }


    //-----------------------------------------
    //          Rest of the methods
    //-----------------------------------------

    // Getters
    public String getTypeOfSession() {
        return this.typeOfSession;
    }

    public Subject getSubject() {
        return this.subject;
    }

    public Interval getInterval() {
        return this.interval;
    }
}
