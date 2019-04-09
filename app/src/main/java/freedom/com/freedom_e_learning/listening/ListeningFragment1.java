package freedom.com.freedom_e_learning.listening;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.listening.Listening;
import freedom.com.freedom_e_learning.model.listening.ListeningQuestion;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListeningFragment1 extends Fragment {


    private Button btn;
    private boolean playPause;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private boolean initialStage = true;
    private String audioUrl;
    Listening listening;
    ArrayList<ListeningQuestion> listeningQuestions;

    private RecyclerView recyclerView;
    private ListeningRecyclerViewAdapter listeningRecyclerViewAdapter;

    // Lấy thông tin database hiện tại
    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference listeningReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listening_fragment1, container, false);

        setControl(view);
        setEvents();
        return view;

    }

    public void setControl(View view) {

        btn = view.findViewById(R.id.audioStreamBtn);
        mediaPlayer = new MediaPlayer();
        progressDialog = new ProgressDialog(getActivity());

        // Lấy tạo đường dẫn tới node listening của topic 1, sau này sẽ set id của topic dynamic
        listeningReference = databaseService.getDatabase().child(Constants.TOPIC_NODE).child("1").child(Constants.LISTENING_NODE);
        recyclerView = view.findViewById(R.id.listening_fragment1_recycler);
    }

    public void setEvents() {
        getListeningData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final String url = "http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";

        // Set cho nút audio
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(url);
                } catch (IllegalArgumentException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    mediaPlayer.prepare();
                } catch (IllegalStateException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
                }
                mediaPlayer.start();
            }
        });
    }

    public void getListeningData() {
        // Lấy dữ liệu từ firebase trong node
        listeningReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Nhận data từ node listening lưu vào model listening
                listening = dataSnapshot.getValue(Listening.class);
                listeningQuestions = listening.getQuestions();
                audioUrl = listening.getAudioURL();

                // Tạo adaper cho recyclerview cho mấy câu trắc nghiệm của listening
                listeningRecyclerViewAdapter = new ListeningRecyclerViewAdapter(getContext(), listeningQuestions);
                recyclerView.setAdapter(listeningRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    class Player extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared = false;

            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        initialStage = true;
                        playPause = false;
                        btn.setText("Launch Streaming");
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                Log.e("MyAudioStreamingApp", e.getMessage());
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (progressDialog.isShowing()) {
                progressDialog.cancel();
            }

            mediaPlayer.start();
            initialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Buffering...");
            progressDialog.show();
        }
    }
}
