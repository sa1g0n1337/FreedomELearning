package freedom.com.freedom_e_learning.model.setup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.SignInActivity;
import freedom.com.freedom_e_learning.model.listening.Listening;
import freedom.com.freedom_e_learning.model.listening.ListeningQuestion;
import freedom.com.freedom_e_learning.model.reading.Reading;
import freedom.com.freedom_e_learning.model.reading.ReadingQuestion;
import freedom.com.freedom_e_learning.model.speaking.Speaking;
import freedom.com.freedom_e_learning.model.topic.Topic;
import freedom.com.freedom_e_learning.model.writing.Writing;

public class SetupTopicActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();

    private DatabaseService mData = DatabaseService.getInstance();


    EditText topicId, topicTitle, topicLevel,
            listeningAudio, listeningTranscript,
            listeningQuestion1, listeningQ1A1, listeningQ1A2, listeningQ1A3, listeningQ1A4,
            speakingQuestion, readingQuestion, speakingAns,
            readingArticle, readingQ1A1, readingQ1A2, readingQ1A3, readingQ1A4,
            writingQuestion, writingAns;
    Button btn;

    ArrayList<ListeningQuestion> listeningQuestions = new ArrayList<>();
    ListeningQuestion listeningQuestion = new ListeningQuestion();
    ArrayList<String> listeningAns = new ArrayList<>();

    ArrayList<ReadingQuestion> readingQuestions = new ArrayList<>();
    ReadingQuestion readingQuest = new ReadingQuestion();
    ArrayList<String> readingAns = new ArrayList<>();

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
                topic.setId(Integer.parseInt(topicId.getText().toString()));
                topic.setTitle(topicTitle.getText().toString());
                topic.setLevel(topicLevel.getText().toString());

                listening.setAudioURL(listeningAudio.getText().toString());
                listening.setTranscript(listeningTranscript.getText().toString());
                listeningQuestion.setQuestion(listeningQuestion1.getText().toString());
                listeningAns.add(listeningQ1A1.getText().toString());
                listeningAns.add(listeningQ1A2.getText().toString());
                listeningAns.add(listeningQ1A3.getText().toString());
                listeningAns.add(listeningQ1A4.getText().toString());
                listeningQuestion.setAnswers(listeningAns);
                listeningQuestion.setCorrectAnswer(listeningQ1A1.getText().toString());
                listeningQuestions.add(listeningQuestion);
                listening.setQuestions(listeningQuestions);


                speaking.setTopic(topic.getId());
                speaking.setQuestion(speakingQuestion.getText().toString());

//                writing.setTopic(topic.getId());
                writing.setQuestion(writingQuestion.getText().toString());
//                writing.setAnswer(writingAns.getText().toString());

                reading.setTopic(topic.getId());
                reading.setArticle(readingArticle.getText().toString());
                readingQuest.setQuestion(readingQuestion.getText().toString());
                readingAns.add(readingQ1A1.getText().toString());
                readingAns.add(readingQ1A2.getText().toString());
                readingAns.add(readingQ1A3.getText().toString());
                readingAns.add(readingQ1A4.getText().toString());
                readingQuest.setAnswers(readingAns);
                readingQuestions.add(readingQuest);
                reading.setQuestions(readingQuestions);

                topic.setListening(listening);
                topic.setSpeaking(speaking);
                topic.setReading(reading);
                topic.setWriting(writing);

                createTopicOnFirebase(topic);

            }
        });
    }


    private void createTopicOnFirebase(final Topic topic) {
        // Create node "Topic/topicId"
        final DatabaseReference userNode = mData.createDatabase("Topic").child(String.valueOf(topic.getId()));
        userNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    userNode.setValue(topic);
                    Log.d(TAG, "Success");
                } else {
                    Log.d(TAG, "Failed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
