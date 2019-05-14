package freedom.com.freedom_e_learning.speaking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import freedom.com.freedom_e_learning.R;

import static freedom.com.freedom_e_learning.R.id.Time_Speaking_1;

public class SpeakingFragment1 extends Fragment {
    private String path;
    private File file;
    private SeekBar seekBar;
    private ProgressDialog progressDialog;
    private TextView txtSpeakingArticle;
    private Integer topic;
    private String uid;
    private Date date;
    private long time;
    private String speakingArticle;
    private TextView txtRecordLabel;
    private Button btnRecord;
    private ImageView btnPlay;
    private Button btnDelete;
    private Button btnUpload;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private static final String LOG_TAG = "Record_log";
    private MediaPlayer mediaPlayer;
    private StorageReference storageReference;
    private Handler handler;
    private TextView audioTime;
    private int save;
    private Runnable runnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.speaking_fragment1, container, false);
        setControl(view);
        setEvents();
        return view;
    }

    public void setControl(View view) {
        speakingArticle = getArguments().getString("Speaking_article");
        topic = getArguments().getInt("Speaking_topic");
        uid = getArguments().getString("User ID"); //nhận UID
        progressDialog = new ProgressDialog(getActivity());
        // Lấy tạo đường dẫn tới node listening của topic 1, sau này sẽ set id của topic dynamic
        txtSpeakingArticle = view.findViewById(R.id.speaking_question);
        txtRecordLabel = view.findViewById(R.id.txtRecord);
        btnRecord = view.findViewById(R.id.btnRecord);
        btnPlay = view.findViewById(R.id.btnPlay_Speaking_1);
        seekBar = view.findViewById(R.id.seekBar_Speaking_1);
        audioTime = view.findViewById(R.id.Time_Speaking_1);
        handler = new Handler();
        btnDelete = view.findViewById(R.id.btnDelete_Record);
        btnUpload = view.findViewById(R.id.btnUpload_Record);
        storageReference = FirebaseStorage.getInstance().getReference();
        date = new Date();
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path += "/";
        time = date.getTime();
        outputFile = path + time + "_" + String.valueOf(topic) + "_" + uid + ".3gp";
    }

    public void setEvents() {
        txtSpeakingArticle.setText(speakingArticle);
        btnPlay.setEnabled(false);
        btnDelete.setEnabled(false);
        btnUpload.setEnabled(false);
        seekBar.setEnabled(false);

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnRecord.getText().toString().equals("start record")) {
                    startRecording();
                    btnRecord.setText("stop record");
                    txtRecordLabel.setText("Tab button for stop record ...");
                    btnPlay.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnUpload.setEnabled(false);
                } else if (btnRecord.getText().toString().equals("stop record")) {
                    stopRecording();
                    btnRecord.setText("start record");
                    txtRecordLabel.setText("Tab button for record ...");
                    btnPlay.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnUpload.setEnabled(true);
                    Audiobar(outputFile);
                    AudioPlay();
                    seekBar.setEnabled(true);
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile();
                seekBar.setMax(0);
                audioTime.setText("0:0/0:0");
                seekBar.setEnabled(false);
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAudio();
            }
        });
    }

    private void startRecording() {
        file = new File(outputFile);
        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Alert!!!");
            builder.setMessage("The recording file already exists. Do you want to delete and start recording again?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    file.delete();
                    date = null;
                    date = new Date();
                    time = date.getTime();
                    outputFile = path + time + "_" + String.valueOf(topic) + "_" + uid + ".3gp";
                    myAudioRecorder = new MediaRecorder();
                    myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    myAudioRecorder.setOutputFile(outputFile);
                    myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    try {
                        myAudioRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                    myAudioRecorder.start();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    btnRecord.setText("start record");
                    txtRecordLabel.setText("Tab button for record ...");
                    btnPlay.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnUpload.setEnabled(true);
                    return;
                }
            });
            builder.create().show();
        } else {
            myAudioRecorder = new MediaRecorder();
            myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            myAudioRecorder.setOutputFile(outputFile);
            myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                myAudioRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
            myAudioRecorder.start();
        }
    }

    private void stopRecording() {
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
        mediaPlayer = new MediaPlayer();
    }

    private void Audiobar(String path){
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    seekBar.setMax(mediaPlayer.getDuration());
                    changeseekBar();
                }
            });
            mediaPlayer.prepare();
            final String totalTimer = miliSecondsToTimer(mediaPlayer.getDuration());
            audioTime.setText("0:0/" + totalTimer);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void AudioPlay(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                    changeseekBar();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                changeseekBar();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                    btnRecord.setEnabled(true);
                    btnUpload.setEnabled(true);
                    btnDelete.setEnabled(true);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause_circle_outline_24dp);
                    changeseekBar();
                    btnRecord.setEnabled(false);
                    btnUpload.setEnabled(false);
                    btnDelete.setEnabled(false);
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnRecord.setEnabled(true);
                btnUpload.setEnabled(true);
                btnDelete.setEnabled(true);
                btnPlay.setImageResource(R.drawable.ic_play_circle_outline_24dp);
                seekBar.setMax(0);
                changeseekBar();
                final String totalTimer = miliSecondsToTimer(mediaPlayer.getDuration());
                audioTime.setText("0:0/" + totalTimer);
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
                    audioTime.setText(currentTimer + "/" + totalTimer);
                }
            };
            handler.postDelayed(runnable, 0);
        }
        // TODO: change time when audio pause
//        else{
//
//        }
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

    private void deleteFile() {
        file = new File(outputFile);
        if (file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Alert!!!");
            builder.setMessage("Do you want to delete recording file?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    file.delete();
                    btnPlay.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnUpload.setEnabled(false);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.create().show();
        }
    }


    private void uploadAudio() {
        file = new File(outputFile);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Alert!!!");
        builder.setMessage("Do you want to upload recording file?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage("Uploading Audio ...");
                progressDialog.show();
                file = new File(outputFile);
                StorageReference filePath = storageReference.child("Speaking").child("Topic " + String.valueOf(topic)).child(uid).child("Speaking_file.3gp");
                Uri uri = Uri.fromFile(file);
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        file.delete();
                        btnPlay.setEnabled(false);
                        btnDelete.setEnabled(false);
                        btnUpload.setEnabled(false);
                        txtRecordLabel.setText("Uploading Finished.");
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.create().show();
    }
}















