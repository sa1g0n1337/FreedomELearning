package freedom.com.freedom_e_learning.model.listening;

import java.util.ArrayList;

public class ListeningTest {

    private ArrayList<ListeningQuestion> questions = new ArrayList<>();
    private int score;

    public ListeningTest() {
    }

    public ArrayList<ListeningQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<ListeningQuestion> questions) {
        this.questions = questions;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
