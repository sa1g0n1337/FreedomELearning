package freedom.com.freedom_e_learning.reading;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.reading.ReadingQuestion;

public class ReadingFragment2 extends Fragment {


    private ProgressDialog progressDialog;
    private String TopicID;
    private Button btnSubmit;
    private double percent;
    private String UID;
    private String topicid;
    private String dateSaved;

    private DatabaseService databaseService = DatabaseService.getInstance();

    ArrayList<ReadingQuestion> readingQuestions;

    private RecyclerView readingQuestionRecycler;
    private ReadingRecyclerViewAdapter readingRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reading_fragment2, container, false);

        setControl(view);
        setEvents();
        quizTest();
        return view;

    }

    public void setControl(View view) {
        progressDialog = new ProgressDialog(getActivity());
        readingQuestionRecycler = view.findViewById(R.id.reading_question_recycler);
        readingQuestions = (ArrayList<ReadingQuestion>) getArguments().getSerializable("Reading_questions");
        TopicID = (String) getArguments().getSerializable("Topic");
        btnSubmit = view.findViewById(R.id.btnReadingSubmit);
    }

    public void quizTest() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                int totalCorrectAnswer = 0;
                for (int i = 0; i < readingQuestions.size(); i++) {
                    if (readingQuestions.get(i).getChoseAnswer() == null) {
                        Toast.makeText(getActivity(), "You must answer all the question", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        int compareResult;
                        compareResult = checkAnswer(readingQuestions.get(i).getChoseAnswer(), readingQuestions.get(i).getCorrectAnswer());
                        if (compareResult == 1) {
                            result += "Question" + String.valueOf(i + 1) + ": Correct\n";
                            totalCorrectAnswer += 1;
                        } else {
                            result += "Question" + String.valueOf(i + 1) + ": Wrong\n";
                            result += "Correct answer: " + readingQuestions.get(i).getCorrectAnswer() + "\n";
                        }
                    }
                }
                Log.d("Total ", String.valueOf(totalCorrectAnswer));
                percent = Float.parseFloat(String.valueOf(totalCorrectAnswer)) / Float.parseFloat(String.valueOf(readingQuestions.size()));
                percent = percent * 100;
                Log.d("percent ", String.valueOf(percent));
                result += "------------------------------------\n";
                result += "Correct Rate: " + String.format("%.2f", percent) + "\n";
                MaterialStyledDialog.Builder dialog = new MaterialStyledDialog.Builder(getActivity());
                dialog.setIcon(R.drawable.icon_success);
                dialog.setDescription(result);
                dialog.setPositiveText("Submit");
                dialog.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UID = databaseService.getFirebaseAuth().getUid();
                        Log.d("Final ", UID);
                        topicid = "Topic " + TopicID;
                        final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        final Date date = new Date();
                        Log.d("date ", dateFormat.format(date));
                        FirebaseDatabase.getInstance().getReference().child("Reading Answer").child(topicid).child(UID).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    return;
                                } else {
                                    if (dataSnapshot.child(dateFormat.format(date)).exists()) {
                                        return;
                                    } else {
                                        FirebaseDatabase.getInstance().getReference().child("Reading Answer").child(topicid).child(UID).child(dateFormat.format(date)).setValue(String.format("%.2f", percent));
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                dialog.build();
                dialog.show();
            }
        });
    }

    public int checkAnswer(String s1, String s2) {
        if (s1.equals(s2)) {
            return 1;
        } else {
            return 0;
        }
    }

    public void setEvents() {
        readingQuestionRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        readingRecyclerViewAdapter = new ReadingRecyclerViewAdapter(getContext(), readingQuestions);
        readingQuestionRecycler.setAdapter(readingRecyclerViewAdapter);
    }
}
