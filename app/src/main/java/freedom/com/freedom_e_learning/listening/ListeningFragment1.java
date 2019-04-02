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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.listening.Listening;
import freedom.com.freedom_e_learning.model.listening.ListeningQuestion;


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
        View view = inflater.inflate(R.layout.fragment1_listening, container, false);

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
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Set cho nút audio
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!playPause) {
                    btn.setText("Pause Streaming");

                    if (initialStage) {
                        new Player().execute("https://r2---sn-npoeener.googlevideo.com/videoplayback?key=yt6&ratebypass=yes&ipbits=0&clen=19285316&itag=18&c=WEB&initcwndbps=2255000&dur=1482.036&fvip=2&signature=2BA91A6BA163ED1ADA3A76C8A7FE4083F6DC5277.DB4DF0BD6A9428D69452E0581CD8D0C5D178925C&gir=yes&sparams=clen,dur,ei,gir,id,initcwndbps,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,pl,ratebypass,requiressl,source,expire&lmt=1513829634107727&ei=TB-bXPbxIKWkz7sPt9qQuAs&requiressl=yes&mime=video/mp4&ip=47.74.148.194&mm=31,26&source=youtube&pl=19&mn=sn-npoeener,sn-i3belnez&id=o-AJMQwCGgh4KLPkVbe_YYr1vugS4-m90xx3bTRdvYPXTb&ms=au,onr&mt=1553669847&mv=m&expire=1553691564&signature=");
                    } else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }

                    playPause = true;

                } else {
                    btn.setText("Launch Streaming");

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }

                    playPause = false;
                }
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
