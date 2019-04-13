package freedom.com.freedom_e_learning.topic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nightonke.boommenu.Animation.BoomEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.listening.ListeningActivity;
import freedom.com.freedom_e_learning.model.topic.Topic;
import freedom.com.freedom_e_learning.reading.ReadingActivity;
import freedom.com.freedom_e_learning.speaking.SpeakingActivity;

public class TopicRecyclerViewAdapter extends RecyclerView.Adapter<TopicRecyclerViewAdapter.RecyclerViewHolder> {


    Context context;
    ArrayList<Topic> topicList = new ArrayList<>();
    ArrayList<Integer> imageIDList;


    public TopicRecyclerViewAdapter(Context context) {
        this.context = context;
        imageIDList = new ArrayList<>();
        imageIDList.add(R.drawable.ic_headset_24dp);
        imageIDList.add(R.drawable.ic_description_24dp);
        imageIDList.add(R.drawable.ic_edit_24dp);
        imageIDList.add(R.drawable.ic_mic_24dp);
    }

    @NonNull
    @Override
    public TopicRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.topic_item, viewGroup, false);
        TopicRecyclerViewAdapter.RecyclerViewHolder recyclerViewHolder = new TopicRecyclerViewAdapter.RecyclerViewHolder(v);
        return recyclerViewHolder;
    }

    // Set questions
    @Override
    public void onBindViewHolder(@NonNull TopicRecyclerViewAdapter.RecyclerViewHolder recyclerViewHolder, int position) {
        final Topic topic = topicList.get(position);
        recyclerViewHolder.tvTopicTitle.setText("Topic: " + topic.getTitle());
        recyclerViewHolder.tvTopicLevel.setText("Level: " + topic.getLevel());
        recyclerViewHolder.bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        recyclerViewHolder.bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_1);
        recyclerViewHolder.bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_2);
        recyclerViewHolder.bmb.setShowDelay(0);
        recyclerViewHolder.bmb.setShowDuration(1000);
        recyclerViewHolder.bmb.setRotateDegree(1080);
        recyclerViewHolder.bmb.setHideDelay(0);
        recyclerViewHolder.bmb.setHideDuration(500);
        recyclerViewHolder.bmb.setFrames(80);
        recyclerViewHolder.bmb.setUse3DTransformAnimation(true);
        recyclerViewHolder.bmb.setBoomEnum(BoomEnum.PARABOLA_3);

        for (int i = 0; i < recyclerViewHolder.bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            SimpleCircleButton.Builder builder = new SimpleCircleButton.Builder()
                    .normalImageRes(imageIDList.get(i))
                    .highlightedImageRes(imageIDList.get(i))
                    .highlightedColor(Color.WHITE)
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int i) {
                            switch (i) {
                                case 0:
                                    Intent intentListening = new Intent(context, ListeningActivity.class);
                                    intentListening.putExtra(String.valueOf(R.string.TOPIC_ID), String.valueOf(topic.getId()));
                                    context.startActivity(intentListening);
                                    break;
                                case 1:
                                    Intent intentReading = new Intent(context, ReadingActivity.class);
                                    intentReading.putExtra(String.valueOf(R.string.TOPIC_ID), String.valueOf(topic.getId()));
                                    context.startActivity(intentReading);
                                    break;
                                case 2:

                                    break;
                                case 3:
                                    Intent intentSpeaking = new Intent(context, SpeakingActivity.class);
                                    intentSpeaking.putExtra(String.valueOf(R.string.TOPIC_ID), String.valueOf(topic.getId()));
                                    context.startActivity(intentSpeaking);
                                    break;
                            }
                        }
                    });
            recyclerViewHolder.bmb.addBuilder(builder);
        }
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public void setTopicList(ArrayList<Topic> list) {
        this.topicList = list;
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTopicTitle;
        private TextView tvTopicLevel;
        BoomMenuButton bmb;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            bmb = itemView.findViewById(R.id.BoomMenu);
            tvTopicTitle = itemView.findViewById(R.id.tv_topic_title);
            tvTopicLevel = itemView.findViewById(R.id.tv_topic_level);

        }
    }
}
