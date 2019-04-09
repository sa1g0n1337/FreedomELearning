package freedom.com.freedom_e_learning.reading;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.reading.ReadingQuestion;

public class ReadingFragment2 extends Fragment {


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
        return view;

    }

    public void setControl(View view) {
        progressDialog = new ProgressDialog(getActivity());
        readingQuestionRecycler = view.findViewById(R.id.reading_question_recycler);
        readingQuestions = (ArrayList<ReadingQuestion>) getArguments().getSerializable("Reading_questions");
    }

    public void setEvents() {
        readingQuestionRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        readingRecyclerViewAdapter = new ReadingRecyclerViewAdapter(getContext(), readingQuestions);
        readingQuestionRecycler.setAdapter(readingRecyclerViewAdapter);
    }
}
