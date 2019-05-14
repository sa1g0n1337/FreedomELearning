package freedom.com.freedom_e_learning.model.listening;

import java.util.ArrayList;

public class Listening {
    private String audioURL;
    private String transcript;
    private ArrayList<ListeningQuestion> questions;
    private String TopicId;

    public String getTopicId() {
        return TopicId;
    }

    public void setTopicId(String topicId) {
        TopicId = topicId;
    }

    public Listening() {
    }

    public String getAudioURL() {
        return audioURL;
    }

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public ArrayList<ListeningQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<ListeningQuestion> questions) {
        this.questions = questions;
    }
}
