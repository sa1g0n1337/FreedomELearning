package freedom.com.freedom_e_learning.model.topic;

import freedom.com.freedom_e_learning.model.listening.Listening;
import freedom.com.freedom_e_learning.model.reading.Reading;
import freedom.com.freedom_e_learning.model.speaking.Speaking;
import freedom.com.freedom_e_learning.model.writing.Writing;

public class Topic {
    private int id;
    private String title;
    private String level;
    private Listening listening;
    private Speaking speaking;
    private Reading reading;
    private Writing writing;

    public Topic() {
    }

    public Topic(int id, String title, String level, Listening listening, Speaking speaking, Reading reading, Writing writing) {
        this.id = id;
        this.title = title;
        this.level = level;
        this.listening = listening;
        this.speaking = speaking;
        this.reading = reading;
        this.writing = writing;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Listening getListening() {
        return listening;
    }

    public void setListening(Listening listening) {
        this.listening = listening;
    }

    public Speaking getSpeaking() {
        return speaking;
    }

    public void setSpeaking(Speaking speaking) {
        this.speaking = speaking;
    }

    public Reading getReading() {
        return reading;
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }

    public Writing getWriting() {
        return writing;
    }

    public void setWriting(Writing writing) {
        this.writing = writing;
    }
}
