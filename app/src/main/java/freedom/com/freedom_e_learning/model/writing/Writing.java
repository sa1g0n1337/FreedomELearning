package freedom.com.freedom_e_learning.model.writing;

public class Writing {

    private String question;
    private int topic;

    public Writing() {
    }

    public Writing(String question, int topic) {
        this.question = question;
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }
}
