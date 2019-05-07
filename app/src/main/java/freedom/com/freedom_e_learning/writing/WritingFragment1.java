package freedom.com.freedom_e_learning.writing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.TestActivity;
import freedom.com.freedom_e_learning.model.Teacher;
import freedom.com.freedom_e_learning.model.writing.WritingAnswer;

public class WritingFragment1 extends Fragment {

    private static final String TAG = TestActivity.class.getSimpleName();

    private DatabaseService mData = DatabaseService.getInstance();

    private ProgressDialog progressDialog;
    private String writingQuestion;
    private TextView txtWritingQuestion;
    private EditText edtWritingAns;
    private Button btnUpload;

    private String uid;
    private int topic;
    private WritingAnswer writingAnswer = new WritingAnswer();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.writing_fragment1, container, false);

        setControl(view);
        setEvents();
        return view;

    }

    public void setControl(View view) {
        writingQuestion = getArguments().getString("WRITING_QUESTION");
        progressDialog = new ProgressDialog(getActivity());

        // Lấy tạo đường dẫn tới node listening của topic 1, sau này sẽ set id của topic dynamic
        txtWritingQuestion = view.findViewById(R.id.tv_writing_question);
        txtWritingQuestion.setMovementMethod(new ScrollingMovementMethod());
        txtWritingQuestion.setText(String.format("Topic %d: %s", topic, writingQuestion));

        uid = getArguments().getString("User ID");
        topic = getArguments().getInt("TOPIC", 0);
        btnUpload = view.findViewById(R.id.writing_ans_submit);

        edtWritingAns = view.findViewById(R.id.edt_writing_ans);
        getAnsFromFirebase(edtWritingAns);
    }

    public void setEvents() {

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writingAnswer.setAnswer(edtWritingAns.getText().toString());
                writingAnswer.setUserID(uid);
                writingAnswer.setTopic(topic);

                Teacher teacher1 = new Teacher();
                teacher1.setName("Teacher A");
                teacher1.setComment("Nice");

                Teacher teacher2 = new Teacher();
                teacher2.setName("Teacher B");
                teacher2.setComment("Hoai dep trai vl!");

                Teacher teacher3 = new Teacher();
                teacher3.setName("Teacher A");
                teacher3.setComment("Nice!!!!");

                ArrayList<Teacher> list = new ArrayList<>();
                list.add(teacher1);
                list.add(teacher2);
                list.add(teacher3);

                writingAnswer.setTeacher(list);

                uploadAnswer(writingAnswer);


            }
        });
    }

    private void uploadAnswer(final WritingAnswer answer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!!!");
        builder.setMessage("Do you finish your answer?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final DatabaseReference writingNode = mData.createDatabase(Constants.WRITING_ANSWER).child(String.valueOf(answer.getTopic())).child(answer.getUserID());
                writingNode.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            writingNode.setValue(answer);
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                            builder1.setTitle("Your writing");
                            builder1.setMessage("Saved success!");
                            builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                            builder1.create().show();
                        } else {
                            writingNode.setValue(answer);
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                            builder2.setTitle("Your writing");
                            builder2.setMessage("Update success!");
                            builder2.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            });
                            builder2.create().show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.create().show();
    }

    private void getAnsFromFirebase(final EditText editText) {
        final DatabaseReference writingNode = mData.createDatabase(Constants.WRITING_ANSWER).child(topic + "").child(uid);
        writingNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    WritingAnswer writing = dataSnapshot.getValue(WritingAnswer.class);
                    editText.setText(writing.getAnswer());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}