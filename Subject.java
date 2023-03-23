package schedule;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;


/*
Notes:
    - The idea is to have an external variable "pagesPerSession" that is resized according to the "difficulty" attribute.
    - Im not sure when the "update" method works fine. The sessions could be organize in incoherent ways with the old organization. Must to check it.
*/

/*
    This class storages the related information to each subject that the Planning class will use to
    make the final timetable.

    Public methods:
        - Subject( a lot of params ): initializes a new Subject object from given parameters.

        - Getters:
            - String getName()
            - int getDifficulty()
            - int getPriority()
            - int getpages()
            - int getSpace()
            - ArrayList<Session> getSessions()
            - int getcurrentPage()
            - int getpagesPerSession()
 */

public class Subject {

    // Number of different priorities
    static final int N_PRIORITIES = 4;

    //-----------------------------------------
    //       Attributes and constructor
    //-----------------------------------------

    // Subject parameters: difficulty between 0 and 1
    private int difficulty, pages, space, priority;
    private String name;

    // Session parameters
    private Map<String, Float> sessionProportions;
    private int pagesPerSession;

    // Tracking progress
    private int currentPage;


    // Constructor
    public Subject(String name, int pagesPerSessionGlobal, int difficulty, boolean exercises, int pages, int space, int priority) throws Exception{
        // Initialize user parameters
        this.pages = pages;
        this.name = name;

        this.setDifficulty(difficulty);
        this.setSpace(space);
        this.setPriority(priority);
        this.setPagesPerSessionGlobal(pagesPerSessionGlobal);

        // Proportion of sessions
        this.sessionProportions = new HashMap<String, Float>();

        this.sessionProportions.put("standard", 1F);
        this.sessionProportions.put("retrieval", 0.25F);
        if (exercises) this.sessionProportions.put("exercises", 0.25F);

        // Progress
        this.currentPage = 0;
    }

    // Clone
    public Subject(@NonNull Subject subject) {
        this.pages = subject.pages;
        this.name = subject.name;
        this.difficulty = subject.difficulty;
        this.space = subject.space;
        this.priority = subject.priority;
        this.pagesPerSession = subject.pagesPerSession;
        this.sessionProportions = subject.sessionProportions;
        this.currentPage = subject.currentPage;
    }


    //-----------------------------------------
    //             Public methods
    //-----------------------------------------

    // Create and return the queue of remaining sessions
    public Queue<ProtoSession> getSessions() {
        Queue<ProtoSession> sessions = new LinkedList<ProtoSession>();

        // I think this can be better. No sense to have this variable pagesPerSession
        int n_standard = (int) Math.ceil((this.pages - this.currentPage) / (float) pagesPerSession);

        // Calculate
        Map<String, Integer> start_session_at = new HashMap<String, Integer>();
        Map<String, Integer> n_sessions = new HashMap<String, Integer>();
        int n_sessions_current;

        for (String type_of_session: sessionProportions.keySet()) {
            n_sessions_current = (int) (n_standard*sessionProportions.get(type_of_session));
            n_sessions.put(type_of_session, n_sessions_current);
            start_session_at.put(type_of_session, n_standard % n_sessions_current);
        }

        // Organize sessions
        // by alternating proportionally between the standard ones

        for (int i=0; i<n_standard; i++) { // Please check that this works
            sessions.add(new ProtoSession(this.name,"standard"));

            for (String type_of_session: sessionProportions.keySet()) {
                if (i % n_sessions.get(type_of_session) == start_session_at.get(type_of_session)) {
                    sessions.add(new ProtoSession(this.name, type_of_session));
                }
            }
        }

        return (LinkedList) sessions;
    }

    // Mark the current standard session as completed
    public void standardSessionCompleted() {
        this.currentPage += this.pagesPerSession;
    }

    public void standardSessionCompleted(int pages_completed) {
        this.currentPage += pages_completed;
    }


    // Getters
    public String getName() {
        return this.name;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getPages() {
        return this.pages;
    }

    public int getSpace() {
        return this.space;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getPagesPerSession() {
        return this.pagesPerSession;
    }


    // Setters
    void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    void setSpace(int space) {
        this.space = space;
    }

    void setPriority(int priority) throws Exception{
        if (priority < 0 || N_PRIORITIES >= priority) {
            throw new Exception("Invalid priority");
        }
        this.priority = priority;
    }

    void setPagesPerSessionGlobal(int pagesPerSession_global)   {
        this.pagesPerSession = (int) (pagesPerSession_global * (this.difficulty + 0.5));
    }
}
