package freedom.com.freedom_e_learning.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.User;

public class TeacherSpeakingActivity extends AppCompatActivity {
    private String mUserID;
    private ArrayList<String> uidList = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private TeacherSpeakingAdapter teacherSpeakingAdapter;
    //    Firebase
    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference topicReference;
    RecyclerView rvTeacherSpeaking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_speaking);
        getUserID();
        setControls();
        setEvents();
        getTopicsFromFireBase();
    }

    public void setControls() {
        rvTeacherSpeaking = findViewById(R.id.rv_teacher_speaking);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTeacherSpeaking.setLayoutManager(linearLayoutManager);

        teacherSpeakingAdapter = new TeacherSpeakingAdapter(this, mUserID);
        teacherSpeakingAdapter.setUsers(users);
        rvTeacherSpeaking.setAdapter(teacherSpeakingAdapter);
    }

    public void setEvents() {

    }

    private void getUserID() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USER_ID)) {
            mUserID = intent.getStringExtra(Constants.USER_ID);
        }
    }

    public void getTopicsFromFireBase() {
        Intent intent = getIntent();
        String topic = intent.getStringExtra("TOPIC");
        teacherSpeakingAdapter.setTopic(topic);
//        Lấy đường dẫn tới node topic trong firebase
        topicReference = databaseService.getDatabase().child(Constants.SPEAKING_ANSWER).child(topic);
        topicReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    getUserList(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getUserList(DataSnapshot dataSnapshot) {
//        Lấy hết tất cả topics thêm vào topic list

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            uidList.add(ds.getKey());
        }

        getUsers(uidList);

    }

    private void getUsers(ArrayList<String> uidList) {
        for (String uid : uidList) {
            topicReference = databaseService.getDatabase().child("User").child(uid);
            topicReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    users.add(dataSnapshot.getValue(User.class));

                    if (users.size() > 0)
                        teacherSpeakingAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
