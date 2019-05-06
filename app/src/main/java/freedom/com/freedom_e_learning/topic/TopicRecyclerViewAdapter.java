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

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.listening.ListeningActivity;
import freedom.com.freedom_e_learning.model.topic.Topic;
import freedom.com.freedom_e_learning.reading.ReadingActivity;
import freedom.com.freedom_e_learning.speaking.SpeakingActivity;
import freedom.com.freedom_e_learning.writing.WritingActivity;

public class TopicRecyclerViewAdapter extends RecyclerView.Adapter<TopicRecyclerViewAdapter.RecyclerViewHolder> {


    Context context;
    String userId;
    ArrayList<Topic> topicList = new ArrayList<>();
    ArrayList<Integer> imageIDList;


    public TopicRecyclerViewAdapter(Context context, String mUserID) {
        this.context = context;
        imageIDList = new ArrayList<>();
        imageIDList.add(R.drawable.ic_headset_24dp);
        imageIDList.add(R.drawable.ic_description_24dp);
        imageIDList.add(R.drawable.ic_edit_24dp);
        imageIDList.add(R.drawable.ic_mic_24dp);
        this.userId = mUserID;
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
        recyclerViewHolder.tv_topic_title.setText(String.format("Topic: %s", topic.getTitle()));
        recyclerViewHolder.tv_topic_level.setText(String.format("Level: %s", topic.getLevel()));
        recyclerViewHolder.boomMenuButton.setButtonEnum(ButtonEnum.SimpleCircle);
        recyclerViewHolder.boomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.DOT_4_1);
        recyclerViewHolder.boomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.SC_4_2);
        recyclerViewHolder.boomMenuButton.setShowDelay(0);
        recyclerViewHolder.boomMenuButton.setShowDuration(1000);
        recyclerViewHolder.boomMenuButton.setRotateDegree(1080);
        recyclerViewHolder.boomMenuButton.setHideDelay(0);
        recyclerViewHolder.boomMenuButton.setHideDuration(500);
        recyclerViewHolder.boomMenuButton.setFrames(80);
        recyclerViewHolder.boomMenuButton.setUse3DTransformAnimation(true);
        recyclerViewHolder.boomMenuButton.setBoomEnum(BoomEnum.PARABOLA_3);

        for (int i = 0; i < recyclerViewHolder.boomMenuButton.getPiecePlaceEnum().pieceNumber(); i++) {
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
                                    intentListening.putExtra(Constants.USER_ID, userId);
                                    context.startActivity(intentListening);
                                    break;
                                case 1:
                                    Intent intentReading = new Intent(context, ReadingActivity.class);
                                    intentReading.putExtra(String.valueOf(R.string.TOPIC_ID), String.valueOf(topic.getId()));
                                    intentReading.putExtra(Constants.USER_ID, userId);
                                    context.startActivity(intentReading);
                                    break;
                                case 2:
                                    Intent intentWriting = new Intent(context, WritingActivity.class);
                                    intentWriting.putExtra("TOPIC", topic.getId());
                                    intentWriting.putExtra(Constants.USER_ID, userId);
                                    context.startActivity(intentWriting);
                                    break;
                                case 3:
                                    Intent intentSpeaking = new Intent(context, SpeakingActivity.class);
                                    intentSpeaking.putExtra(String.valueOf(R.string.TOPIC_ID), String.valueOf(topic.getId()));
                                    intentSpeaking.putExtra(Constants.USER_ID, userId);
                                    context.startActivity(intentSpeaking);
                                    break;
                            }
                        }
                    });
            recyclerViewHolder.boomMenuButton.addBuilder(builder);
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

        private TextView tv_topic_title;
        private TextView tv_topic_level;
        BoomMenuButton boomMenuButton;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            boomMenuButton = itemView.findViewById(R.id.BoomMenu);
            tv_topic_title = itemView.findViewById(R.id.tv_topic_title);
            tv_topic_level = itemView.findViewById(R.id.tv_topic_level);

        }
    }
}