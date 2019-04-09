package freedom.com.freedom_e_learning.topic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.topic.Topic;

public class TopicRecyclerViewAdapter extends RecyclerView.Adapter<TopicRecyclerViewAdapter.RecyclerViewHolder> {


    Context context;
    ArrayList<Topic> topicList = new ArrayList<>();

    public TopicRecyclerViewAdapter(Context context) {
        this.context = context;

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
        recyclerViewHolder.tvTopicTitle.setText("Topic: " + topicList.get(position).getTitle());
        recyclerViewHolder.tvTopicLevel.setText("Level: " + topicList.get(position).getLevel());
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

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicTitle = itemView.findViewById(R.id.tv_topic_title);
            tvTopicLevel = itemView.findViewById(R.id.tv_topic_level);

        }
    }
}
