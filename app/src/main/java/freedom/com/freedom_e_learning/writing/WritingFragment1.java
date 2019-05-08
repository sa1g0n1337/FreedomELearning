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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
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


        uid = getArguments().getString("User ID");
        topic = getArguments().getInt("TOPIC", 0);
        btnUpload = view.findViewById(R.id.writing_ans_submit);

        edtWritingAns = view.findViewById(R.id.edt_writing_ans);
        getAnsFromFirebase(edtWritingAns);
    }

    public void setEvents() {
        txtWritingQuestion.setText(String.format("Topic %d: %s", topic, writingQuestion));
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
                teacher3.setName("Teacher C");
                teacher3.setComment("Nice!!!!");

                Teacher teacher4 = new Teacher();
                teacher4.setName("Teacher D");
                teacher4.setComment("Con ca vang mau xanh la cay hoi no mau gi?");

                Teacher teacher5 = new Teacher();
                teacher5.setName("Teacher E");
                teacher5.setComment("Nulla bibendum mi ac massa auctor viverra. Nullam consectetur nisl a nisl viverra, in pharetra ipsum semper. Ut sed ante ac ex scelerisque ornare. Cras luctus, tellus eu lobortis consequat, nulla ante ultricies diam, ut tincidunt lacus arcu vitae est. Maecenas nec viverra felis. Pellentesque vitae nibh vel orci molestie dapibus a vitae nisl. Mauris nec feugiat augue. Proin elementum nec metus et fermentum. Suspendisse feugiat sit amet lectus vitae aliquam. Etiam in est et arcu sodales tempor. Mauris posuere est ac ipsum accumsan fringilla. Morbi et justo quis elit faucibus pharetra quis a ex. Proin non porta massa. Aliquam vel luctus libero, sit amet condimentum sapien.");

                Teacher teacher6 = new Teacher();
                teacher6.setName("Teacher F");
                teacher6.setComment("Etiam quis venenatis orci, vitae blandit ipsum. Sed egestas tortor sodales, placerat tortor sit amet, semper mauris. Maecenas tincidunt risus sed lorem tincidunt varius. In hac habitasse platea dictumst. Nunc est ante, pellentesque eget ultricies sit amet, mattis efficitur velit. Phasellus gravida sed mauris at hendrerit. In auctor lobortis felis, eget aliquam dolor tincidunt at. Vivamus vitae tincidunt ipsum, in placerat sapien. Suspendisse eleifend massa in dui efficitur, sit amet sodales magna congue.!!!!");

                ArrayList<Teacher> list = new ArrayList<>();
                list.add(teacher1);
                list.add(teacher2);
                list.add(teacher3);
                list.add(teacher4);
                list.add(teacher5);
                list.add(teacher6);

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
                            Toasty.success(getContext(), "Success!", Toast.LENGTH_SHORT, true).show();
                        } else {
                            writingNode.setValue(answer);
                            Toasty.info(getContext(), "Updated!", Toast.LENGTH_SHORT, true).show();
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