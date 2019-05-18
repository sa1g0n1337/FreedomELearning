package freedom.com.freedom_e_learning.speaking;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.Teacher;
import freedom.com.freedom_e_learning.model.speaking.SpeakingAnswer;

public class SpeakingFragment2 extends Fragment {
    private DatabaseService databaseService = DatabaseService.getInstance();
    private ImageView btnPlay;
    private MediaPlayer mediaPlayer;
    private ArrayList<Teacher> teachers = new ArrayList<>();
    private ProgressDialog progressDialog;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private TextView time;
    private int topic;
    private String uid;
    private String audioUrl;
    private int save;
    private DatabaseReference speakingReference;
    private SpeakingCommentsAdapter speakingCommentsAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.speaking_fragment2, container, false);
        setControl(view);
        setEvents();
        return view;
    }

    public void setControl(View view) {
        topic = getArguments().getInt("Speaking_topic");
        uid = getArguments().getString("User ID");
        mediaPlayer = new MediaPlayer();
        progressDialog = new ProgressDialog(getActivity());
        btnPlay = view.findViewById(R.id.btnPlay_Speaking_2);
        seekBar = view.findViewById(R.id.seekBar_Speaking_2);
        time = view.findViewById(R.id.Time_Speaking_2);
        handler = new Handler();
        recyclerView = view.findViewById(R.id.speaking_comment_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public void setEvents() {
        seekBar.setEnabled(false);
        getSpeakingData();
    }

    public void getSpeakingData() {
        speakingReference = databaseService.getDatabase().child(Constants.SPEAKING_ANSWER).child(topic + "").child(uid);

        speakingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    SpeakingAnswer speakingAnswer = dataSnapshot.getValue(SpeakingAnswer.class);
                    teachers = speakingAnswer.getTeacher();
                    if (teachers != null) {
                        speakingCommentsAdapter = new SpeakingCommentsAdapter(getContext(), teachers);
                        recyclerView.setAdapter(speakingCommentsAdapter);
                    }
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                    audioUrl = speakingAnswer.getUserAudioURL();
                    if (audioUrl != null) {
                        seekBar.setEnabled(true);
                        Audiobar(audioUrl);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void Audiobar(String url) {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    changeseekBar();
                }
            });
            mediaPlayer.prepare();
            final String totalTimer = miliSecondsToTimer(mediaPlayer.getDuration());
            time.setText("0:0/" + totalTimer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPlay.setImageResource(R.drawable.ic_pause_50dp);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play_50dp);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause_50dp);
                    changeseekBar();
                }

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlay.setImageResource(R.drawable.ic_play_50dp);
                seekBar.setMax(0);
                changeseekBar();
                final String totalTimer = miliSecondsToTimer(mediaPlayer.getDuration());
                time.setText("00:00/" + totalTimer);
                seekBar.setMax(mediaPlayer.getDuration());
            }
        });
    }

    private void changeseekBar() {
        save = mediaPlayer.getCurrentPosition();
        seekBar.setProgress(save);
        final String currentTimer = miliSecondsToTimer(mediaPlayer.getCurrentPosition());
        final String totalTimer = miliSecondsToTimer(mediaPlayer.getDuration());
        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    changeseekBar();
                    time.setText(currentTimer + "/" + totalTimer);
                }
            };
            handler.postDelayed(runnable, 0);
        }
        // TODO: change time when audio pause
//        else{
//
//        }

    }

    private String miliSecondsToTimer(long miliseconds) {
        String finalTimerString = "";
        String secondsString;
        String minutesString;

        int hours = (int) (miliseconds / (1000 * 60 * 60));
        int minutes = (int) (miliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((miliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0 && seconds < 10) {
            secondsString = "0" + seconds;
        } else if (hours > 0 && seconds > 10) {
            secondsString = "" + seconds;
        } else if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        return finalTimerString;
    }
}
