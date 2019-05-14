package freedom.com.freedom_e_learning.listening;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.MainActivity;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.Teacher;
import freedom.com.freedom_e_learning.model.User;
import freedom.com.freedom_e_learning.model.listening.ListeningQuestion;
import freedom.com.freedom_e_learning.model.topic.Topic;
import freedom.com.freedom_e_learning.teacher.TeacherActivity;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ListeningFragment1 extends Fragment {


    private ImageView btnPlay;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private TextView time;
    private Button btnSubmit;

    private DatabaseService databaseService = DatabaseService.getInstance();

    private String audioUrl;
    private int save;
    private double percent;

    ArrayList<ListeningQuestion> listeningQuestions;
    private String TopicID;

    private RecyclerView recyclerView;
    private ListeningRecyclerViewAdapter listeningRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listening_fragment1, container, false);

        setControl(view);
        setEvents();
        Audiobar();
        quizTest();
        return view;

    }

    public void setControl(View view) {


        mediaPlayer = new MediaPlayer();
        progressDialog = new ProgressDialog(getActivity());
        btnPlay = view.findViewById(R.id.btnPlay);
        seekBar = view.findViewById(R.id.seekBar);
        time = view.findViewById(R.id.Time);
        handler = new Handler();
        btnSubmit = view.findViewById(R.id.btnListenSubmit);

        recyclerView = view.findViewById(R.id.listening_fragment1_recycler);


    }

    public void setEvents() {
        getListeningData();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Tạo adaper cho recyclerview cho mấy câu trắc nghiệm của listening
        listeningRecyclerViewAdapter = new ListeningRecyclerViewAdapter(getContext(), listeningQuestions);
        recyclerView.setAdapter(listeningRecyclerViewAdapter);
    }

    public void getListeningData() {
        listeningQuestions = (ArrayList<ListeningQuestion>) getArguments().getSerializable("Listening_questions");
        TopicID = (String) getArguments().getSerializable("Topic");
        audioUrl = getArguments().getString(getString(R.string.LISTENING_AUDIO));

    }

    public void quizTest() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                int totalCorrectAnswer = 0;
                for (int i = 0; i < listeningQuestions.size(); i++) {
                    if (listeningQuestions.get(i).getChoseAnswer() == null) {
                        Toast.makeText(getActivity(), "You must answer all the question", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        int compareResult;
                        compareResult = checkAnswer(listeningQuestions.get(i).getChoseAnswer(), listeningQuestions.get(i).getCorrectAnswer());
                        if (compareResult == 1) {
                            result += "Question" + String.valueOf(i + 1) + ": Correct\n";
                            totalCorrectAnswer += 1;
                        } else {
                            result += "Question" + String.valueOf(i + 1) + ": Wrong\n";
                            result += "Correct answer: " + listeningQuestions.get(i).getCorrectAnswer() + "\n";
                        }
                    }
                }
                Log.d("Total ", String.valueOf(totalCorrectAnswer));
                percent = Float.parseFloat(String.valueOf(totalCorrectAnswer)) / Float.parseFloat(String.valueOf(listeningQuestions.size()));
                percent = percent * 100;
                Log.d("percent ", String.valueOf(percent));
                result += "Correct Rate: " + String.format("%.2f",percent) + "\n";
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Result !!");
                builder.setMessage(result);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String UID = databaseService.getFirebaseAuth().getUid();
                        UID += ":" + String.format("%.2f",percent);
                        Log.d("Final ",UID);
                        String topicid = "Topic " + TopicID;
                        FirebaseDatabase.getInstance().getReference().child("Listening Answer").child(topicid).child("uid:percent").setValue(UID);
                    }
                });
                builder.create().show();
            }
        });
    }

    public int checkAnswer(String s1, String s2) {
        if (s1.matches(s2)) {
            return 1;
        } else {
            return 0;
        }
    }

    private void Audiobar() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    changeseekBar();
                }
            });
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {

                    mediaPlayer.seekTo(i);
                    //changeseekBar();
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
//        else {
//                    mediaPlayer.pause();
//                    runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            changeseekBar();
//                            time.setText(currentTimer + "/" + totalTimer);
//                        }
//                    };
//                    handler.postDelayed(runnable,0);
//                }


    }

    public String miliSecondsToTimer(long miliseconds) {
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
