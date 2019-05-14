package freedom.com.freedom_e_learning.reading;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.reading.ReadingQuestion;

public class ReadingFragment2 extends Fragment {


    private ProgressDialog progressDialog;
    private String TopicID;
    private Button btnSubmit;
    private double percent;

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

    public void quizTest(){
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
                result += "Correct Rate: " + String.format("%.2f",percent) + "\n";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Result !!");
                builder.setMessage(result);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String UID = databaseService.getFirebaseAuth().getUid();
                        UID += ":" + String.format("%.2f",percent);
                        Log.d("Final ",UID);
                        String topicid = "Topic " + TopicID;
                        FirebaseDatabase.getInstance().getReference().child("Reading Answer").child(topicid).child("uid:percent").setValue(UID);
                    }
                });
                builder.create().show();
            }
        });
    }

    public int checkAnswer(String s1, String s2) {
        if (s1.matches(s2)) {
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
