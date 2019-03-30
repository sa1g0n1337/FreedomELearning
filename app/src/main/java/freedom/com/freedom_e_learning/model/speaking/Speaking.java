package freedom.com.freedom_e_learning.model.speaking;

public class Speaking {

    private int topic;
    private String question;
    private String userAudioURL;

    public Speaking() {
    }

    public Speaking(int topic, String question, String userAudioURL) {
        this.topic = topic;
        this.question = question;
        this.userAudioURL = userAudioURL;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUserAudioURL() {
        return userAudioURL;
    }

    public void setUserAudioURL(String userAudioURL) {
        this.userAudioURL = userAudioURL;
    }
}
