package freedom.com.freedom_e_learning.reading;

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

public class ReadingFragment1 extends Fragment {

    private ProgressDialog progressDialog;


    private TextView txtReadingArticle;
    private String readingArticle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reading_fragment1, container, false);

        setControl(view);
        return view;

    }

    public void setControl(View view) {
        readingArticle = getArguments().getString(getString(R.string.READING_ARTICLE));
        progressDialog = new ProgressDialog(getActivity());
        // Lấy tạo đường dẫn tới node listening của topic 1, sau này sẽ set id của topic dynamic
        txtReadingArticle = view.findViewById(R.id.tv_reading_article);
        txtReadingArticle.setMovementMethod(new ScrollingMovementMethod());
        txtReadingArticle.setText(readingArticle);
    }

}
