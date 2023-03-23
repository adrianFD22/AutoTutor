package schedule;

/*
    This class encapsulates the information for constructing a Session object during the Planning
    execution.
 */

class ProtoSession {
    final int length = 60;

    String nameOfSubject;
    String typeOfSession;


    // Constructor
    ProtoSession(String nameOfSubject, String typeOfSession) {
        this.nameOfSubject = nameOfSubject;
        this.typeOfSession = typeOfSession;
    }
}
