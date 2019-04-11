package freedom.com.freedom_e_learning.writing;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import freedom.com.freedom_e_learning.R;

public class WritingFragment1 extends Fragment {

    private ProgressDialog progressDialog;
    private String writingQuestion;
    private TextView txtWritingQuestion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.writing_fragment1, container, false);

        setControl(view);
        return view;

    }

    public void setControl(View view) {
        writingQuestion = getArguments().getString(getString(R.string.WRITING_QUESTION));
        progressDialog = new ProgressDialog(getActivity());
        // Lấy tạo đường dẫn tới node listening của topic 1, sau này sẽ set id của topic dynamic
        txtWritingQuestion = view.findViewById(R.id.tv_writing_question);
        txtWritingQuestion.setMovementMethod(new ScrollingMovementMethod());
        txtWritingQuestion.setText(writingQuestion);
    }


}