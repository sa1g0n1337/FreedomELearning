package freedom.com.freedom_e_learning.teacher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.topic.Topic;

public class TeacherTopicAdapter extends RecyclerView.Adapter<TeacherTopicAdapter.RecyclerViewHolder> {

    Context context;
    String userId;
    ArrayList<Topic> topicList = new ArrayList<>();


    public TeacherTopicAdapter(Context context, String mUserID) {
        this.context = context;
        this.userId = mUserID;
    }

    @NonNull
    @Override
    public TeacherTopicAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.teacher_topic_item, viewGroup, false);
        TeacherTopicAdapter.RecyclerViewHolder recyclerViewHolder = new TeacherTopicAdapter.RecyclerViewHolder(v);
        return recyclerViewHolder;
    }

    // Set questions
    @Override
    public void onBindViewHolder(@NonNull TeacherTopicAdapter.RecyclerViewHolder recyclerViewHolder, int position) {
        final Topic topic = topicList.get(position);
        recyclerViewHolder.tv_topic_title.setText(String.format("Topic: %s", topic.getTitle()));
        recyclerViewHolder.tv_topic_level.setText(String.format("Level: %s", topic.getLevel()));
        recyclerViewHolder.btnTeacherSpeaking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TeacherSpeakingActivity.class);
                intent.putExtra("TOPIC", topic.getId() + "");
                intent.putExtra(Constants.USER_ID, userId);
                context.startActivity(intent);
            }
        });


        recyclerViewHolder.btnTeacherWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TeacherWritingActivity.class);
                intent.putExtra("TOPIC", topic.getId() + "");
                intent.putExtra(Constants.USER_ID, userId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public void setTopicList(ArrayList<Topic> list) {
        this.topicList = list;
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public Button btnTeacherSpeaking;
        public Button btnTeacherWriting;
        private TextView tv_topic_title;
        private TextView tv_topic_level;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_topic_title = itemView.findViewById(R.id.tv_topic_title);
            tv_topic_level = itemView.findViewById(R.id.tv_topic_level);
            btnTeacherSpeaking = itemView.findViewById(R.id.btn_teacher_speaking);
            btnTeacherWriting = itemView.findViewById(R.id.btn_teacher_writing);
        }
    }
}
