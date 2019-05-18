package freedom.com.freedom_e_learning.model.reading;

import java.util.ArrayList;

public class ReadingQuestion {

    private String question;
    private ArrayList<String> answers;
    private String correctAnswer;
    private String choseAnswer;

    public String getChoseAnswer() {
        return choseAnswer;
    }

    public void setChoseAnswer(String choseAnswer) {
        this.choseAnswer = choseAnswer;
    }


    public ReadingQuestion() {
    }

    public ReadingQuestion(String question, ArrayList<String> answers, String correctAnswer) {
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

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
