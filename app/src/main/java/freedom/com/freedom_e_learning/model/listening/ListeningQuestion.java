package freedom.com.freedom_e_learning.model.listening;

import java.util.Map;

public class ListeningQuestion {

    private String question;
    private Map<Integer, String> answers;
    private int correctAnswer;

    public ListeningQuestion() {

    }

    public ListeningQuestion(String question, Map<Integer, String> answers, int correctAnswer) {
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<Integer, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, String> answers) {
        this.answers = answers;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}

