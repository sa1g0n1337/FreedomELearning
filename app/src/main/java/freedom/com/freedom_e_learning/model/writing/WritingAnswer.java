package freedom.com.freedom_e_learning.model.writing;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.model.Teacher;

public class WritingAnswer extends Writing {

    private String userID;
    private String answer;
    private ArrayList<Teacher> teacher;

    public WritingAnswer() {
    }

    public WritingAnswer(String userID, String answer, ArrayList<Teacher> teacher) {
        this.userID = userID;
        this.answer = answer;
        this.teacher = teacher;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public ArrayList<Teacher> getTeacher() {
        return teacher;
    }

    public void setTeacher(ArrayList<Teacher> teacher) {
        this.teacher = teacher;
    }
}
