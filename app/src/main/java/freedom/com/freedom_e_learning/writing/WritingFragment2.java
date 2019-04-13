package freedom.com.freedom_e_learning.writing;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import freedom.com.freedom_e_learning.R;

public class WritingFragment2 extends Fragment {

    private ProgressDialog progressDialog;


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

    }

    public void setEvents() {
    }
}