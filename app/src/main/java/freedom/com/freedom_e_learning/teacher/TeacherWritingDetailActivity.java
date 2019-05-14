package freedom.com.freedom_e_learning.teacher;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;
import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.Teacher;
import freedom.com.freedom_e_learning.model.writing.WritingAnswer;

public class TeacherWritingDetailActivity extends AppCompatActivity {

    String topic, userID, teacherID, teacherName, teacherComment;
    private TextView tvUserAnswer;
    private EditText edtTeacherComment;
    private WritingAnswer writingAnswer;
    private Button btnSubmit;

    private DatabaseService databaseService = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_detail);
        setControls();
        setEvents();
        getUserAnswer();


    }

    public class LoadDataTask extends AsyncTask<Void, Void, Void> {

        private String comment;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            submitComment(this.comment);
            return null;
        }
    }

    public void setControls() {

        tvUserAnswer = findViewById(R.id.tv_user_writing_content);
        tvUserAnswer.setMovementMethod(new ScrollingMovementMethod());
        edtTeacherComment = findViewById(R.id.edt_teacher_comment);
        btnSubmit = findViewById(R.id.teacher_writing_submit);

        teacherID = databaseService.getFirebaseAuth().getCurrentUser().getUid();

    }

    public void setEvents() {

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherComment = edtTeacherComment.getText().toString();
                TeacherWritingDetailActivity.LoadDataTask loadDataTask = new TeacherWritingDetailActivity.LoadDataTask();
                loadDataTask.setComment(teacherComment);
                loadDataTask.execute();
                return;
            }
        });
    }

    public void getUserAnswer() {
        Intent intent = getIntent();
        topic = intent.getStringExtra("TOPIC");
        userID = intent.getStringExtra("USER_ID");
        final DatabaseReference writingAnsRef = databaseService.getDatabase().child("WRITING ANSWER").child(topic).child(userID);
        writingAnsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                writingAnswer = dataSnapshot.getValue(WritingAnswer.class);
                tvUserAnswer.setText(writingAnswer.getAnswer());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void submitComment(String comment) {
        final DatabaseReference teacherRef = databaseService.createDatabase(Constants.WRITING_ANSWER).child(topic).child(userID).child("teacher");
        final Teacher teacher = new Teacher();
        teacherName = databaseService.getFirebaseAuth().getCurrentUser().getDisplayName();
        teacher.setName(teacherName);
        teacher.setComment(comment);
        teacher.setId(teacherID);
        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Teacher temp = ds.getValue(Teacher.class);
                        if (temp.getId().equals(teacherID)) {
                            teacherRef.child(ds.getKey()).setValue(teacher);
                            Toasty.success(TeacherWritingDetailActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                            return;
                        }

                        counter++;
                    }

                    teacherRef.child(counter + "").setValue(teacher);
                } else {
                    teacherRef.child("0").setValue(teacher);
                }


//                teacherRef.setValue(teacher);
//
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
