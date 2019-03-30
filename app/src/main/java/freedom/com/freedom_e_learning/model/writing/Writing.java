package freedom.com.freedom_e_learning.model.writing;

public class Writing {

    private String question;
    private String answer;
    private int topic;

    public Writing() {
    }

    public Writing(String question, String answer, int topic) {
        this.question = question;
        this.answer = answer;
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }
}
