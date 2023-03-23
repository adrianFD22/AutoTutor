package schedule;


import androidx.annotation.NonNull;

import com.google.common.collect.Iterators;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;


/*
    The planning class storages the hours in which to assign the study sessions and which of these
    have been already assigned. Its programming interface allows to select the available hours
    of each day of the week and the subjects that should be organized.

    Public methods:
        - Planning(): initializes an empty Planning object.

        - ArrayList<Session> getTimetable(): get the planned events for the coming days
        - void updateDate(LocalDateTime date): update the date

        - Subject:
            - boolean addSubject(Subject subject)
            - boolean removeSubject(Subject subject)
            - ArrayList<Subject> getSubjects()

        - WeekSchedule:
            - boolean addWeekInterval(WeekInterval interval)
            - boolean removeWeekInterval(WeekInterval interval)
            - ArrayList<WeekInterval> getDayWeekInterval(int day)
*/

public class Planning {

    //-----------------------------------------
    //             Nested classes
    //-----------------------------------------

    // Wrapper to encapsulate Subject and queue of ProtoSessions
    static class SubjectProtoSessionsWrapper {
        private Subject subject;
        Queue<ProtoSession> protoSessions;

        // Constructor
        SubjectProtoSessionsWrapper(@NonNull Subject subject) {
            this.subject = subject;
            this.protoSessions = subject.getSessions();
        }


        // Getters

        Subject getSubject() {
            return this.subject;
        }

        Queue<ProtoSession> getProtoSessions() {
            return this.protoSessions;
        }
    }


    //-----------------------------------------
    //       Attributes and constructor
    //-----------------------------------------

    // Array containing the total hours to plan per week
    private WeekSchedule weekSchedule; // Available hours each week to plan

    // Attributes to maintain the planning updated to the current date
    private LocalDateTime date;

    //private int day_of_the_week;
    //private int min_of_the_day;

    // Subjects to plan
    private ArrayList<Subject> subjects;

    // Constructor
    public Planning(LocalDateTime date) {
        this.date = date;

        this.weekSchedule = new WeekSchedule();
        this.subjects = new ArrayList<Subject>();
    }


    //-----------------------------------------
    //           Planning methods
    //-----------------------------------------

    // Update the date: min, hour, day and year.
    public void updateDate(LocalDateTime date) {
        this.date = date;
    }

    // Get timetable
    public ArrayList<Session> getTimetable() throws Exception {
        // Refresh attributes
        ArrayList<Session> timetable = new ArrayList<Session>();

        // Initialize array of subjects grouped by priority
        ArrayList<SubjectProtoSessionsWrapper>[] priorityList = new ArrayList[Subject.N_PRIORITIES];

        for (Subject subject: subjects) {
            priorityList[subject.getPriority()].add(new SubjectProtoSessionsWrapper(subject));
        }

        // Initialize a FreeSchedule object
        FreeSchedule freeSchedule = new FreeSchedule(weekSchedule, date);

        // Plan for each priority group
        ArrayList<SubjectProtoSessionsWrapper> currentPriorityList;
        Iterator<SubjectProtoSessionsWrapper> priorityListIterator;

        for (int i=0; i<Subject.N_PRIORITIES; i++) {
            currentPriorityList = priorityList[i];
            priorityListIterator = Iterators.cycle(currentPriorityList); // Cyclic iterator

            SubjectProtoSessionsWrapper subjectWrapped;
            Queue<ProtoSession> sessionQueue;

            Subject subject;
            ProtoSession protoSession;
            Session session;
            Interval interval;

            // Priority-first plan subjects
            while (priorityListIterator.hasNext()) {
                subjectWrapped = priorityListIterator.next();

                subject = subjectWrapped.getSubject();
                sessionQueue = subjectWrapped.getProtoSessions();

                // Check if current subject has been already completely planned
                if (sessionQueue.isEmpty()) {
                    priorityListIterator.remove();
                    continue;
                }

                // Get next protoSession to plan
                protoSession = sessionQueue.poll();

                // Get the first available gap
                interval = freeSchedule.popFirstInterval(protoSession.length, subject.getSpace());

                // Create and add session
                session = new Session(subject, protoSession.typeOfSession, interval);
                timetable.add(session);
            }
        }

        return timetable;
    }


    //-----------------------------------------
    //           Subject methods
    //-----------------------------------------

    // Add a new subject
    public boolean addSubject(@NonNull Subject subject) {
        // Check that the subject's name is not repeated
        ArrayList<String> subjectNames = (ArrayList<String>) subjects.stream().map(Subject::getName).collect(Collectors.toList());
        if (subjectNames.contains(subject.getName())) { return false;}

        // Add
        this.subjects.add(subject);

        return true;
    }

    // Remove a subject
    public boolean removeSubject(String nameSubject) {
        // Check if subject is contained in the list of subjects to plan
        Optional<Subject> opSubject = subjects.stream().filter(s -> s.getName().equals(nameSubject)).findFirst();

        if (opSubject.isPresent()) {
            Subject subject = opSubject.get();

            // Remove
            this.subjects.remove(subject);
        }

        return opSubject.isPresent();
    }

    // Get list of subjects
    public ArrayList<Subject> getSubjects() {
        return this.subjects;
    }

    // Setters
    public boolean setDifficultySubject(String nameSubject, int difficulty) {
        // Check if subject is in the list
        Optional<Subject> optionalSubject = subjects.stream().filter(s -> s.getName().equals(nameSubject)).findFirst();

        if (! optionalSubject.isPresent()) {
            return false;
        }

        Subject subject = optionalSubject.get();

        // Set difficulty
        subject.setDifficulty(difficulty);

        return true;
    }

    public boolean setSpaceSubject(String nameSubject, int space) {
        // Check if subject is in the list
        Optional<Subject> optionalSubject = subjects.stream().filter(s -> s.getName().equals(nameSubject)).findFirst();

        if (! optionalSubject.isPresent()) {
            return false;
        }

        Subject subject = optionalSubject.get();

        // Set space
        subject.setSpace(space);

        return true;
    }

    public boolean setPrioritySubject(String nameSubject, int priority) throws Exception{
        // Check if subject is in the list
        Optional<Subject> optionalSubject = subjects.stream().filter(s -> s.getName().equals(nameSubject)).findFirst();

        if (! optionalSubject.isPresent()) {
            return false;
        }

        Subject subject = optionalSubject.get();

        // Set difficulty
        subject.setPriority(priority);

        return true;
    }


    //-----------------------------------------
    //          WeekSchedule methods
    //-----------------------------------------

    // Add a new interval to the available schedule
    public boolean addWeekInterval(WeekInterval interval) {
        boolean result = this.weekSchedule.add(interval);

        return result;
    }

    // Remove an interval from the available schedule
    public boolean removeWeekSchedule(WeekInterval interval) {
        boolean result = this.weekSchedule.remove(interval);

        return result;
    }

    // Get the schedule of one day of the week
    public ArrayList<WeekInterval> getDayWeekSchedule(int day) throws Exception {
        // Get weekSchedule
        ArrayList<WeekInterval> schedule = this.weekSchedule.getSchedule();

        // Filter the schedule to get the sessions which start at the given day
        ArrayList<WeekInterval> schedule_day = (ArrayList<WeekInterval>) schedule.stream().filter(weekInterval -> weekInterval.getDayStartOfInterval() == day).collect(Collectors.toList());

        return schedule_day;
    }

}
