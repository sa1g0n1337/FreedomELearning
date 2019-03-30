package freedom.com.freedom_e_learning.model.setup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.listening.Listening;
import freedom.com.freedom_e_learning.model.reading.Reading;
import freedom.com.freedom_e_learning.model.speaking.Speaking;
import freedom.com.freedom_e_learning.model.topic.Topic;
import freedom.com.freedom_e_learning.model.writing.Writing;

public class SetupTopicActivity extends AppCompatActivity {

    EditText topicId, topicTitle, topicLevel,
            listeningAudio, listeningTranscript,
            listeningQuestion1, listeningQ1A1, listeningQ1A2, listeningQ1A3, listeningQ1A4,
            speakingQuestion, readingQuestion, speakingAns,
            readingArticle, readingQ1A1, readingQ1A2, readingQ1A3, readingQ1A4,
            writingQuestion, writingAns;
    Button btn;

    Topic topic;
    Listening listening;
    Reading reading;
    Writing writing;
    Speaking speaking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_topic);
        setControls();
        setEvents();
    }

    public void setControls() {
        topicId = findViewById(R.id.topic_id);
        topicTitle = findViewById(R.id.topic_title);
        topicLevel = findViewById(R.id.topic_level);

        listeningAudio = findViewById(R.id.listening_audio);
        listeningTranscript = findViewById(R.id.listening_transcript);
        listeningQuestion1 = findViewById(R.id.listening_question1);
        listeningQ1A1 = findViewById(R.id.listening_question1_ans1);
        listeningQ1A2 = findViewById(R.id.listening_question1_ans2);
        listeningQ1A3 = findViewById(R.id.listening_question1_ans3);
        listeningQ1A4 = findViewById(R.id.listening_question1_ans4);

        speakingQuestion = findViewById(R.id.speaking_question);
        speakingAns = findViewById(R.id.speaking_ans);

        readingArticle = findViewById(R.id.reading_article);
        readingQuestion = findViewById(R.id.reading_question);
        readingQ1A1 = findViewById(R.id.reading_question1_ans1);
        readingQ1A2 = findViewById(R.id.reading_question1_ans2);
        readingQ1A3 = findViewById(R.id.reading_question1_ans3);
        readingQ1A4 = findViewById(R.id.reading_question1_ans4);

        writingQuestion = findViewById(R.id.writing_question);
        writingAns = findViewById(R.id.writing_ans);

        btn = findViewById(R.id.submit);

        topic = new Topic();
        listening = new Listening();
        reading = new Reading();
        speaking = new Speaking();
        writing = new Writing();
    }

    public void setEvents() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
