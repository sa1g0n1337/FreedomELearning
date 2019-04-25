package freedom.com.freedom_e_learning.speaking;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.Date;

import java.io.IOException;

import freedom.com.freedom_e_learning.R;

public class SpeakingFragment1 extends Fragment {
    private String path;
    private File file;
    private int n;
    private ProgressDialog progressDialog;
    private TextView txtSpeakingArticle;
    private Integer topic;
    private String uid;
    private Date date;
    private long time;
    private String speakingArticle;
    private TextView txtRecordLabel;
    private Button btnRecord;
    private Button btnPlay;
    private Button btnDelete;
    private Button btnUpload;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private static final String LOG_TAG = "Record_log";
    private MediaPlayer audioPlayer;

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
        btnPlay = view.findViewById(R.id.btnPlay_Record);
        btnDelete = view.findViewById(R.id.btnDelete_Record);
        btnUpload = view.findViewById(R.id.btnUpload_Record);
        myAudioRecorder = new MediaRecorder();
        date= new Date();
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        path += "/";
        time = date.getTime();
        outputFile = path + time +"_0"+".3gp";
        audioPlayer = new MediaPlayer();
    }

    public void setEvents() {
        txtSpeakingArticle.setText(speakingArticle);
        btnPlay.setEnabled(false);
        btnDelete.setEnabled(false);
        btnUpload.setEnabled(false);
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnRecord.getText().toString().equals("start record")){
                    n = 0;
                    file = new File(outputFile);
                    if(file.exists()){
                        file.delete();
                        time = date.getTime();
                        n++;
                        outputFile = path + time +"_"+ String.valueOf(n) + ".3gp";
                    }
                    Log.d("File path: ", outputFile);
                    startRecording();
                    btnRecord.setText("stop record");
                    txtRecordLabel.setText("Tab button for stop record ...");
                }
                else if(btnRecord.getText().toString().equals("stop record")){
                    stopRecording();
                    btnRecord.setText("start record");
                    txtRecordLabel.setText("Tab button for record ...");
                    btnPlay.setEnabled(true);
                    btnDelete.setEnabled(true);
                    btnUpload.setEnabled(true);
                    try {
                        audioPlayer.setDataSource(outputFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnPlay.getText().toString().equals("play record")){
                    try {
                        audioPlayer.prepare();
                        audioPlayer.start();
                        btnPlay.setText("stop record");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(btnPlay.getText().toString().equals("stop record")){
                    btnPlay.setText("play record");
                    audioPlayer.stop();
                }
            }
        });
    }

    private void startRecording(){
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setOutputFile(outputFile);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try{
            myAudioRecorder.prepare();
        }catch (IOException e){
            Log.e(LOG_TAG, "prepare() failed");
        }
        myAudioRecorder.start();
    }

    private  void stopRecording(){
        myAudioRecorder.stop();
        myAudioRecorder.release();
        myAudioRecorder = null;
    }
}
