package freedom.com.freedom_e_learning.speaking;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.devlomi.record_view.OnBasketAnimationEnd;
import com.devlomi.record_view.OnRecordListener;
import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;

import java.util.concurrent.TimeUnit;

import freedom.com.freedom_e_learning.R;

public class SpeakingFragment1 extends Fragment {
    private ProgressDialog progressDialog;
    private TextView txtSpeakingArticle;
    private String speakingArticle;
    RecordView recordView;
    RecordButton recordButton;


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
        progressDialog = new ProgressDialog(getActivity());
        // Lấy tạo đường dẫn tới node listening của topic 1, sau này sẽ set id của topic dynamic
        txtSpeakingArticle = view.findViewById(R.id.speaking_question);
        recordView = (RecordView) view.findViewById(R.id.record_view);
        recordButton = (RecordButton) view.findViewById(R.id.record_button);
    }

    public void setEvents() {
        txtSpeakingArticle.setText(speakingArticle);
        //IMPORTANT
        recordButton.setRecordView(recordView);
        recordButton.setListenForRecord(true);
        //Cancel Bounds is when the Slide To Cancel text gets before the timer . default is 8
        recordView.setCancelBounds(8);


        recordView.setSmallMicColor(Color.parseColor("#c2185b"));

        //prevent recording under one Second
        recordView.setLessThanSecondAllowed(false);


        recordView.setSlideToCancelText("Slide To Cancel");


        recordView.setCustomSounds(R.raw.record_start, R.raw.record_finished, 0);
        recordView.setOnRecordListener(new OnRecordListener() {
            @Override
            public void onStart() {
                Log.d("RecordView", "onStart");
//                Toast.makeText(SpeakingActivity.this, "OnStartRecord", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                Log.d("RecordView", "onCancel");
            }

            @Override
            public void onFinish(long recordTime) {
                String time = getHumanTimeText(recordTime);
//                Toast.makeText(SpeakingActivity.this, "onFinishRecord - Recorded Time is: " + time, Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onFinish");

                Log.d("RecordTime", time);
            }

            @Override
            public void onLessThanSecond() {
//                Toast.makeText(SpeakingActivity.this, "OnLessThanSecond", Toast.LENGTH_SHORT).show();
                Log.d("RecordView", "onLessThanSecond");
            }
        });
        recordView.setOnBasketAnimationEndListener(new OnBasketAnimationEnd() {
            @Override
            public void onAnimationEnd() {
                Log.d("RecordView", "Basket Animation Finished");
            }
        });
    }
    private String getHumanTimeText(long milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));
    }
}
