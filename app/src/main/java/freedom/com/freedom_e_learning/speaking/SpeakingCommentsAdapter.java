package freedom.com.freedom_e_learning.speaking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.Teacher;

public class SpeakingCommentsAdapter extends RecyclerView.Adapter<SpeakingCommentsAdapter.RecyclerViewHolder> {
    Context context;
    ArrayList<Teacher> teachers;

    public SpeakingCommentsAdapter(Context context, ArrayList<Teacher> teachers) {
        this.context = context;
        this.teachers = teachers;
    }

    @NonNull
    @Override
    public SpeakingCommentsAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.comment, viewGroup, false);
        SpeakingCommentsAdapter.RecyclerViewHolder recyclerViewHolder = new SpeakingCommentsAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SpeakingCommentsAdapter.RecyclerViewHolder recyclerViewHolder, int position) {
        recyclerViewHolder.txtTeacherName.setText(teachers.get(position).getName());
        recyclerViewHolder.txtTeacherComment.setText(teachers.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView txtTeacherName;
        private TextView txtTeacherComment;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTeacherName = itemView.findViewById(R.id.teacher_name);
            txtTeacherComment = itemView.findViewById(R.id.teacher_comment);
        }
    }
}
