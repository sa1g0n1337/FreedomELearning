package freedom.com.freedom_e_learning.listening;

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

public class ListeningFragment2 extends Fragment {
    private TextView txtTranscript;
    private String transcript;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listening_fragment2, container, false);

        setControl(view);
        return view;
    }

    public void setControl(View view) {
        txtTranscript = view.findViewById(R.id.txtTranscript);
        txtTranscript.setMovementMethod(new ScrollingMovementMethod());
        transcript = getArguments().getString("Listening_transcript");
        txtTranscript.setText(transcript);
        txtTranscript.setMovementMethod(new ScrollingMovementMethod());
    }
}
