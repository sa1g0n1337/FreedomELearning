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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.reading.ReadingQuestion;

public class ReadingFragment2 extends Fragment {

    private Button btnSubmit;
    private ProgressDialog progressDialog;

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
        btnSubmit = view.findViewById(R.id.btn_submit);
    }

    public void setEvents() {
        readingQuestionRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        readingRecyclerViewAdapter = new ReadingRecyclerViewAdapter(getContext(), readingQuestions);
        readingQuestionRecycler.setAdapter(readingRecyclerViewAdapter);
    }

    public int Check(String s1,String s2){
        if (s1.matches(s2)){
            return 1;
        }
        else{
            return 0;
        }
    }

    public void quizTest(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                int count;
                for (int j = 0;j<readingQuestions.size();j++){
                    if (readingQuestions.get(j).getChoseAnswer() == null){
                        Toast.makeText(getActivity(),"You must answer all the question !!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                for(int i =0; i<readingQuestions.size();i++){
                    if (Check(readingQuestions.get(i).getChoseAnswer(),readingQuestions.get(i).getCorrectAnswer())==1){
                        result += String.valueOf(i+1) + ": Correct\n";
                    }
                    else{
                        result += String.valueOf(i+1) + ": Wrong\n";
                        result += "Correct Answer: " + readingQuestions.get(i).getCorrectAnswer() + "\n";
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(result);
                builder.setTitle("Result !!!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                builder.create().show();
            }
        });
    }
}
