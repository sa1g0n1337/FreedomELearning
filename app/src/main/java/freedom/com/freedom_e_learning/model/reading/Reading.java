package freedom.com.freedom_e_learning.model.reading;

import java.util.ArrayList;

public class Reading {

    private int topic;
    private String article;
    private ArrayList<ReadingQuestion> questions;

    public Reading() {
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public ArrayList<ReadingQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<ReadingQuestion> questions) {
        this.questions = questions;
    }
}
