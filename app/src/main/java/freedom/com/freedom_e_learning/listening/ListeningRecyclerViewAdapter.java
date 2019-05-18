package freedom.com.freedom_e_learning.listening;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.listening.ListeningQuestion;

public class ListeningRecyclerViewAdapter extends RecyclerView.Adapter<ListeningRecyclerViewAdapter.RecyclerViewHolder> {


    Context context;
    ArrayList<ListeningQuestion> listeningQuestions;

    public ListeningRecyclerViewAdapter(Context context, ArrayList<ListeningQuestion> listeningQuestions) {
        this.context = context;
        this.listeningQuestions = listeningQuestions;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.listening_item_fragment1, viewGroup, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    // Set questions
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        recyclerViewHolder.txtQuestion.setText(listeningQuestions.get(position).getQuestion());
        recyclerViewHolder.txtQuestion.setTextSize(15);
        recyclerViewHolder.radAnswer0.setText(listeningQuestions.get(position).getAnswers().get(0));
        recyclerViewHolder.radAnswer1.setText(listeningQuestions.get(position).getAnswers().get(1));
        recyclerViewHolder.radAnswer2.setText(listeningQuestions.get(position).getAnswers().get(2));

    }

    @Override
    public int getItemCount() {
        return listeningQuestions.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtQuestion;
        private RadioButton radAnswer0;
        private RadioButton radAnswer1;
        private RadioButton radAnswer2;
        private RadioGroup radGroup;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.txtListeningQuestion);
            radAnswer0 = itemView.findViewById(R.id.radListeningAnswer0);
            radAnswer1 = itemView.findViewById(R.id.radListeningAnswer1);
            radAnswer2 = itemView.findViewById(R.id.radListeningAnswer2);
            radGroup = itemView.findViewById(R.id.radGroup);
            radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radListeningAnswer0:
                            Log.d("Pos ", String.valueOf(getAdapterPosition()));
                            listeningQuestions.get(getAdapterPosition()).setChoseAnswer(radAnswer0.getText().toString());
                            break;
                        case R.id.radListeningAnswer1:
                            Log.d("Pos ", String.valueOf(getAdapterPosition()));
                            listeningQuestions.get(getAdapterPosition()).setChoseAnswer(radAnswer1.getText().toString());
                            break;
                        case R.id.radListeningAnswer2:
                            Log.d("Pos ", String.valueOf(getAdapterPosition()));
                            listeningQuestions.get(getAdapterPosition()).setChoseAnswer(radAnswer2.getText().toString());
                            break;
                    }
                }
            });


        }
    }
}
