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

public class TeacherWritingActivity extends AppCompatActivity {

    private String mUserID;
    private ArrayList<String> uidList = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private TeacherWritingAdapter teacherWritingAdapter;


    //    Firebase
    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference topicReference;

    RecyclerView rvTeacherWriting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_writing);

        getUserID();
        setControls();
        setEvents();

//        new TeacherWritingActivity.LoadDataTask().execute();
        getTopicsFromFireBase();
    }

    public void setControls() {
        rvTeacherWriting = findViewById(R.id.rv_teacher_writing);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTeacherWriting.setLayoutManager(linearLayoutManager);

        teacherWritingAdapter = new TeacherWritingAdapter(this, mUserID);
        teacherWritingAdapter.setUsers(users);
        rvTeacherWriting.setAdapter(teacherWritingAdapter);


    }

    public void setEvents() {

    }

//    public class LoadDataTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            getTopicsFromFireBase();
//            return null;
//        }
//    }


    public void getTopicsFromFireBase() {
        Intent intent = getIntent();
        String topic = intent.getStringExtra("TOPIC");
        teacherWritingAdapter.setTopic(topic);
//        Lấy đường dẫn tới node topic trong firebase
        topicReference = databaseService.getDatabase().child("WRITING ANSWER").child(topic);
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
                        teacherWritingAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void getUserID() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USER_ID)) {
            mUserID = intent.getStringExtra(Constants.USER_ID);
        }
    }


}

