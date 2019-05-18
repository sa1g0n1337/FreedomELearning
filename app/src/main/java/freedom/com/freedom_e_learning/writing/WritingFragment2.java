package freedom.com.freedom_e_learning.writing;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class WritingFragment2 extends Fragment {


    private static final String TAG = TestActivity.class.getSimpleName();

    private DatabaseService databaseService = DatabaseService.getInstance();

    private ProgressDialog progressDialog;

    private String uid;
    private int topic;
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private WritingCommentsAdapter writingCommentsAdapter;
    private String userAnswer;


    private RecyclerView recyclerView;
    private TextView tv_writingContent;


    private DatabaseReference writingReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.writing_fragment2, container, false);
        setControl(view);
        setEvents();
        return view;
    }

    public void setControl(View view) {

        progressDialog = new ProgressDialog(getActivity());
        // Lấy tạo đường dẫn tới node listening của topic 1, sau này sẽ set id của topic dynamic

        uid = getArguments().getString("User ID");
        topic = getArguments().getInt("TOPIC", 0);


        tv_writingContent = view.findViewById(R.id.tv_writing_content);
        recyclerView = view.findViewById(R.id.teacher_comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setEvents() {

        writingReference = databaseService.getDatabase().child(Constants.WRITING_ANSWER).child(topic + "").child(uid);

        writingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    WritingAnswer writingAnswer = dataSnapshot.getValue(WritingAnswer.class);
                    userAnswer = writingAnswer.getAnswer();
                    teachers = writingAnswer.getTeacher();
                    tv_writingContent.setText(userAnswer);
                    tv_writingContent.setMovementMethod(new ScrollingMovementMethod());
                    if (teachers != null) {
                        writingCommentsAdapter = new WritingCommentsAdapter(getContext(), teachers);
                        recyclerView.setAdapter(writingCommentsAdapter);
                    }
                } else {
                    tv_writingContent.setText("No answer yet!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}