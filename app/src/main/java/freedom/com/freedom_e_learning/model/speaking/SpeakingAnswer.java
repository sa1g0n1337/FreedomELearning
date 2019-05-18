package freedom.com.freedom_e_learning.model.speaking;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.model.Teacher;

public class SpeakingAnswer extends Speaking {
    private String userID;
    private ArrayList<Teacher> teacher;

    public SpeakingAnswer() {
    }

    public SpeakingAnswer(String userID, ArrayList<Teacher> teacher) {
        this.userID = userID;
        this.teacher = teacher;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Teacher> getTeacher() {
        return teacher;
    }

    public void setTeacher(ArrayList<Teacher> teacher) {
        this.teacher = teacher;
    }
}
