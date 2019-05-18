package freedom.com.freedom_e_learning.speaking;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.speaking.SpeakingAnswer;


public class SpeakingFragment1 extends Fragment {
    private DatabaseService mData = DatabaseService.getInstance();

    private String path;
    private File file;
    private SeekBar seekBar;
    private String audioUrl;
    private ProgressDialog progressDialog;
    private TextView txtSpeakingArticle;
    private Integer topic;
    private String uid;
    private Date date;
    private long time;
    private String speakingArticle;
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
    private SpeakingAnswer speakingAnswer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.speaking_fragment1, container, false);
        setControl(view);
        setEvents();
        return view;
    }

    public void setControl(View view) {
        speakingAnswer = new SpeakingAnswer();
        speakingArticle = getArguments().getString("Speaking_article");
        topic = getArguments().getInt("Speaking_topic");
        uid = getArguments().getString("User ID"); //nhận UID
        progressDialog = new ProgressDialog(getActivity());
        // Lấy tạo đường dẫn tới node listening của topic 1, sau này sẽ set id của topic dynamic
        txtSpeakingArticle = view.findViewById(R.id.speaking_question);
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
                    btnRecord.setBackgroundResource(R.drawable.recorded_button);
                    btnPlay.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnUpload.setEnabled(false);
                    btnDelete.setBackgroundResource(R.drawable.delete_disable_button);
                    btnUpload.setBackgroundResource(R.drawable.submit_disable_button);
                } else if (btnRecord.getText().toString().equals("stop record")) {
                    stopRecording();
                    btnRecord.setText("start record");
                    btnPlay.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnUpload.setEnabled(true);
                    Audiobar(outputFile);
                    AudioPlay();
                    seekBar.setEnabled(true);
                    btnRecord.setBackgroundResource(R.drawable.record_button);
                    btnDelete.setBackgroundResource(R.drawable.delete_button);
                    btnUpload.setBackgroundResource(R.drawable.submit_button);
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile();
                seekBar.setMax(0);
                audioTime.setText("00:00/00:00");
                seekBar.setEnabled(false);
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadAudio();
                seekBar.setMax(0);
                audioTime.setText("00:00/00:00");
                seekBar.setEnabled(false);
                mediaPlayer.release();
                mediaPlayer = null;
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
                    btnPlay.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnUpload.setEnabled(true);
                    btnDelete.setBackgroundResource(R.drawable.delete_button);
                    btnUpload.setBackgroundResource(R.drawable.submit_button);
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

    private void Audiobar(String path) {
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
            audioTime.setText("00:00/" + totalTimer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void AudioPlay() {
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
                btnPlay.setImageResource(R.drawable.ic_pause_50dp);
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play_50dp);
                    btnRecord.setEnabled(true);
                    btnUpload.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnUpload.setBackgroundResource(R.drawable.submit_button);
                    btnDelete.setBackgroundResource(R.drawable.delete_button);
                } else {
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause_50dp);
                    changeseekBar();
                    btnRecord.setEnabled(false);
                    btnUpload.setEnabled(false);
                    btnDelete.setEnabled(false);
                    btnUpload.setBackgroundResource(R.drawable.submit_disable_button);
                    btnDelete.setBackgroundResource(R.drawable.delete_disable_button);
                }
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnRecord.setEnabled(true);
                btnUpload.setEnabled(true);
                btnDelete.setEnabled(true);
                btnUpload.setBackgroundResource(R.drawable.submit_button);
                btnDelete.setBackgroundResource(R.drawable.delete_button);
                btnPlay.setImageResource(R.drawable.ic_play_50dp);
                seekBar.setMax(0);
                changeseekBar();
                final String totalTimer = miliSecondsToTimer(mediaPlayer.getDuration());
                audioTime.setText("00:00/" + totalTimer);
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
                    btnUpload.setBackgroundResource(R.drawable.submit_disable_button);
                    btnDelete.setBackgroundResource(R.drawable.delete_disable_button);
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
        final AnimationDrawable[] sucessAnimation = new AnimationDrawable[1];
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
                final StorageReference filePath = storageReference.child("Speaking").child("Topic_" + String.valueOf(topic)).child(uid).child("Speaking_file.3gp");
                Uri uri = Uri.fromFile(file);
                filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        file.delete();
                        btnPlay.setEnabled(false);
                        btnDelete.setEnabled(false);
                        btnUpload.setEnabled(false);
                        btnUpload.setBackgroundResource(R.drawable.upload_onsucess);
                        btnDelete.setBackgroundResource(R.drawable.delete_disable_button);
                        filePath.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        audioUrl = uri.toString();
                                        speakingAnswer.setUserAudioURL(audioUrl);
                                        audioUrl = null;
                                        speakingAnswer.setUserID(uid);
                                        speakingAnswer.setTopic(topic);
                                        uploadSpeakingAnswer(speakingAnswer);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                        sucessAnimation[0] = (AnimationDrawable) btnUpload.getBackground();
                        sucessAnimation[0].start();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

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


    private void uploadSpeakingAnswer(final SpeakingAnswer answer) {
        final DatabaseReference speakingNode = mData.createDatabase(Constants.SPEAKING_ANSWER).child(String.valueOf(answer.getTopic())).child(answer.getUserID());
        speakingNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    speakingNode.setValue(answer);
                    Toasty.success(getContext(), "Success!", Toast.LENGTH_SHORT, true).show();
                } else {
                    speakingNode.setValue(answer);
                    Toasty.info(getContext(), "Updated!", Toast.LENGTH_SHORT, true).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}














