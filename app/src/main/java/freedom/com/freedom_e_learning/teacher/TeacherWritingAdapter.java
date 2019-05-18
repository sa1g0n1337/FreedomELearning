package freedom.com.freedom_e_learning.teacher;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.User;

public class TeacherWritingAdapter extends RecyclerView.Adapter<TeacherWritingAdapter.RecyclerViewHolder> {

    Context context;
    String topic;
    ArrayList<User> users = new ArrayList<>();


    public TeacherWritingAdapter(Context context, String mUserID) {
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherWritingAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.user_list_item, viewGroup, false);
        TeacherWritingAdapter.RecyclerViewHolder recyclerViewHolder = new TeacherWritingAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    // Set questions
    @Override
    public void onBindViewHolder(@NonNull TeacherWritingAdapter.RecyclerViewHolder recyclerViewHolder, final int position) {
        recyclerViewHolder.tvUsername.setText(users.get(position).getName());
        recyclerViewHolder.tvUserID.setText(String.format("User ID: %s", users.get(position).getUid()));
        Glide.with(recyclerViewHolder.ivUserAva).load(users.get(position).getProfilePicture()).into(recyclerViewHolder.ivUserAva);
        recyclerViewHolder.cv_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TeacherWritingDetailActivity.class);
                intent.putExtra("USER_ID", users.get(position).getUid());
                intent.putExtra("TOPIC", topic);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView tvUsername;
        TextView tvUserID;
        ImageView ivUserAva;
        CardView cv_User;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_user_name);
            tvUserID = itemView.findViewById(R.id.tv_user_id);
            ivUserAva = itemView.findViewById(R.id.img_user_avatar);
            cv_User = itemView.findViewById(R.id.cv_user);
        }
    }
}
