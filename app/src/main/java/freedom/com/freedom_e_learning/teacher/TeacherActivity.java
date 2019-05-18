package freedom.com.freedom_e_learning.teacher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.topic.Topic;

public class TeacherActivity extends AppCompatActivity {

    private String mUserID;
    private ArrayList<Topic> topicList = new ArrayList<>();
    private TeacherTopicAdapter teacherTopicAdapter;

    //    Firebase
    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference topicReference;


    Button btnTeacherSpeaking;
    Button btnTeacherWriting;
    RecyclerView rvTeacherTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        getUserID();
        setControls();
        setEvents();

        new TeacherActivity.LoadDataTask().execute();
    }

    public void setControls() {
        btnTeacherSpeaking = findViewById(R.id.btn_teacher_speaking);
        btnTeacherWriting = findViewById(R.id.btn_teacher_writing);
        rvTeacherTopic = findViewById(R.id.rv_teacher_topic);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTeacherTopic.setLayoutManager(linearLayoutManager);

        teacherTopicAdapter = new TeacherTopicAdapter(this, mUserID);
        teacherTopicAdapter.setTopicList(topicList);
        rvTeacherTopic.setAdapter(teacherTopicAdapter);
    }

    public void setEvents() {

    }

    public class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getTopicsFromFireBase();
            return null;
        }
    }


    public void getTopicsFromFireBase() {
//        Lấy đường dẫn tới node topic trong firebase
        topicReference = databaseService.getDatabase().child(Constants.TOPIC_NODE);
        topicReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getData(DataSnapshot dataSnapshot) {
//        Lấy hết tất cả topics thêm vào topic list
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Topic topic = ds.getValue(Topic.class);
            topicList.add(topic);
        }
        if (topicList.size() > 0)
            teacherTopicAdapter.notifyDataSetChanged();
    }


    private void getUserID() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USER_ID)) {
            mUserID = intent.getStringExtra(Constants.USER_ID);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
