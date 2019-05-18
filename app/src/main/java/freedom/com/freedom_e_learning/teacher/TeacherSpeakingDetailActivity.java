package freedom.com.freedom_e_learning.teacher;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.Teacher;
import freedom.com.freedom_e_learning.model.speaking.SpeakingAnswer;

public class TeacherSpeakingDetailActivity extends AppCompatActivity {
    String topic, userID, teacherID, teacherName, teacherComment;
    private ImageView btnPlay;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Runnable runnable;
    private Handler handler;
    private TextView time;
    private int save;
    private SpeakingAnswer speakingAnswer;
    private EditText edtTeacherComment;
    private Button btnSubmit;
    private DatabaseService databaseService = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking_detail);
        setControls();
        setEvents();
        getUserAnswer();
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Void> {
        private String comment;

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            submitComment(this.comment);
            return null;
        }
    }

    public void setControls() {
        mediaPlayer = new MediaPlayer();
//        progressDialog = new ProgressDialog();
        btnPlay = findViewById(R.id.btnPlay_Speaking_comment);
        seekBar = findViewById(R.id.seekBar_Speaking_comment);
        time = findViewById(R.id.Time_Speaking_comment);
        handler = new Handler();
        edtTeacherComment = findViewById(R.id.edt_teacher_comment_speaking);
        btnSubmit = findViewById(R.id.teacher_speaking_submit);
        teacherID = databaseService.getFirebaseAuth().getCurrentUser().getUid();

    }

    public void setEvents() {
        seekBar.setEnabled(false);
        getSpeakingData();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teacherComment = edtTeacherComment.getText().toString();
                TeacherSpeakingDetailActivity.LoadDataTask loadDataTask = new TeacherSpeakingDetailActivity.LoadDataTask();
                loadDataTask.setComment(teacherComment);
                loadDataTask.execute();
                return;
            }
        });
    }

    public void getSpeakingData() {
        Intent intent = getIntent();
        topic = intent.getStringExtra("TOPIC");
        userID = intent.getStringExtra("USER_ID");
        final DatabaseReference speakingAnsRef = databaseService.getDatabase().child("SPEAKING ANSWER").child(topic).child(userID);
        speakingAnsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                seekBar.setEnabled(true);
                speakingAnswer = dataSnapshot.getValue(SpeakingAnswer.class);
                mediaPlayer.release();
                Audiobar(speakingAnswer.getUserAudioURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Audiobar(String url) {
        mediaPlayer = new MediaPlayer();
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
            time.setText("00:00/" + totalTimer);
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
//                changeseekBar();
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
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlay.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                seekBar.setMax(0);
                changeseekBar();
                final String totalTimer = miliSecondsToTimer(mediaPlayer.getDuration());
                time.setText("0:0/" + totalTimer);
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

    public void getUserAnswer() {
        Intent intent = getIntent();
        topic = intent.getStringExtra("TOPIC");
        userID = intent.getStringExtra("USER_ID");
    }

    public void submitComment(String comment) {
        final DatabaseReference teacherRef = databaseService.createDatabase(Constants.SPEAKING_ANSWER).child(topic).child(userID).child("teacher");
        final Teacher teacher = new Teacher();
        teacherName = databaseService.getFirebaseAuth().getCurrentUser().getDisplayName();
        teacher.setName(teacherName);
        teacher.setComment(comment);
        teacher.setId(teacherID);
        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int counter = 0;
                if (dataSnapshot != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Teacher temp = ds.getValue(Teacher.class);
                        if (temp.getId().equals(teacherID)) {
                            teacherRef.child(ds.getKey()).setValue(teacher);
                            Toasty.success(TeacherSpeakingDetailActivity.this, "Success!", Toast.LENGTH_SHORT, true).show();
                            return;
                        }
                        counter++;
                    }

                    teacherRef.child(counter + "").setValue(teacher);
                } else {
                    teacherRef.child("0").setValue(teacher);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
