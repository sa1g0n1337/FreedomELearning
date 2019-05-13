package freedom.com.freedom_e_learning.listening;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.listening.ListeningQuestion;


public class ListeningFragment1 extends Fragment {

    private ImageView btnPlay;
    private MediaPlayer mediaPlayer;
    private ProgressDialog progressDialog;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private TextView time;
    private Button btnSubmit;

    private String audioUrl;
    private int save;

    ArrayList<ListeningQuestion> listeningQuestions;
    ArrayList<Integer> WrongAnswer;

    private RecyclerView recyclerView;
    private ListeningRecyclerViewAdapter listeningRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listening_fragment1, container, false);

        setControl(view);
        setEvents();
        Audiobar();
        return view;

    }

    public void setControl(View view) {


        mediaPlayer = new MediaPlayer();
        progressDialog = new ProgressDialog(getActivity());
        btnPlay = view.findViewById(R.id.btnPlay);
        seekBar = view.findViewById(R.id.seekBar);
        time = view.findViewById(R.id.Time);
        btnSubmit = view.findViewById(R.id.btn_submit);

        handler = new Handler();


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
        audioUrl = getArguments().getString(getString(R.string.LISTENING_AUDIO));
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                int count;
                for (int j = 0;j<listeningQuestions.size();j++){
                    if (listeningQuestions.get(j).getChoseAnswer() == null){
                        Toast.makeText(getActivity(),"Bạn chưa trả lời hết các câu hỏi",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                for(int i =0; i<listeningQuestions.size();i++){
                    if (Check(listeningQuestions.get(i).getChoseAnswer(),listeningQuestions.get(i).getCorrectAnswer())==1){
                        result += "Câu " + String.valueOf(i+1) + ": Đúng\n";
                    }
                    else{
                        result += "Câu " + String.valueOf(i+1) + ": Sai\n";
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(result);
                builder.setTitle("Kết quả bài test !!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                builder.create().show();
            }
        });

    }

    public int Check(String s1,String s2){
        if (s1.matches(s2)){
            return 1;
        }
        else{
            return 0;
        }
    }

    private void Audiobar() {
        mediaPlayer = MediaPlayer.create(this.getContext(), R.raw.dancin);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                seekBar.setMax(mediaPlayer.getDuration());
                changeseekBar();

            }
        });

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
                btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
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

        int hours = (int) (miliseconds / (1000 * 60 * 60));
        int minutes = (int) (miliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((miliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }
}
