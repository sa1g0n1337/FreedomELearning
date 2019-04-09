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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.listening.Listening;

public class ListeningFragment2 extends Fragment {
    private TextView txtTranscript;
    private String transcript;
    private Listening listening;

    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference listeningReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listening_fragment2, container, false);

        setControl(view);
        setEvent();
        return view;
    }

    public void setControl(View view) {
        txtTranscript = view.findViewById(R.id.txtTranscript);
        txtTranscript.setMovementMethod(new ScrollingMovementMethod());
        listeningReference = databaseService.getDatabase().child(Constants.TOPIC_NODE).child("1").child(Constants.LISTENING_NODE);

    }

    public void setEvent() {
        getTranscript();

    }

    public void getTranscript() {
        listeningReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listening = dataSnapshot.getValue(Listening.class);
                transcript = listening.getTranscript();

                txtTranscript.setText(transcript);
                txtTranscript.setTextSize(20);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
