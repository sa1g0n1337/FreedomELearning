package freedom.com.freedom_e_learning;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.model.Teacher;
import freedom.com.freedom_e_learning.model.setup.SetupTopicActivity;
import freedom.com.freedom_e_learning.model.writing.WritingAnswer;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();

    private Toolbar mToolbar;
    private DatabaseService mData = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Set up content");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ProgressDialog progressDialog = new ProgressDialog(this);

        Button btn = findViewById(R.id.btn_setup);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, SetupTopicActivity.class);
                startActivity(intent);
            }
        });


        Button test = findViewById(R.id.test1);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Teacher teacher1 = new Teacher();
                teacher1.setName("Tokuda");
                teacher1.setComment("Nice");

                Teacher teacher2 = new Teacher();
                teacher2.setName("XXXX");
                teacher2.setComment("OOOOOO");

                Teacher teacher3 = new Teacher();
                teacher3.setName("Ricardo");
                teacher3.setComment("Nice!!!!");

                ArrayList<Teacher> list = new ArrayList<>();
                list.add(teacher1);
                list.add(teacher2);
                list.add(teacher3);

                final WritingAnswer answer = new WritingAnswer();
                answer.setTopic(1);
                answer.setUserID("Hb89OzGjYqgUdOQgC6yB96xBJ6z1");
                answer.setAnswer("This is an answer");
                answer.setTeacher(list);

                // Create node "Topic/topicId"
                final DatabaseReference userNode = mData.createDatabase("Writing Answer").child(String.valueOf(answer.getTopic())).child(answer.getUserID());
                userNode.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            userNode.setValue(answer);
                            Log.d(TAG, "Success");
                        } else {
                            userNode.setValue(answer);
                            Log.d(TAG, "Update");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
