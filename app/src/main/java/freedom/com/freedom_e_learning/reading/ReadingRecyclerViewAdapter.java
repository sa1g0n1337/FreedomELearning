package freedom.com.freedom_e_learning.reading;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.reading.ReadingQuestion;

public class ReadingRecyclerViewAdapter extends RecyclerView.Adapter<ReadingRecyclerViewAdapter.RecyclerViewHolder> {


    Context context;
    ArrayList<ReadingQuestion> readingQuestions;

    public ReadingRecyclerViewAdapter(Context context, ArrayList<ReadingQuestion> readingQuestions) {
        this.context = context;
        this.readingQuestions = readingQuestions;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.reading_item_frament1, viewGroup, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(v);
        return recyclerViewHolder;
    }

    // Set questions
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int position) {
        recyclerViewHolder.txtQuestion.setText(readingQuestions.get(position).getQuestion());
        recyclerViewHolder.txtQuestion.setTextSize(15);
        recyclerViewHolder.radAnswer0.setText(readingQuestions.get(position).getAnswers().get(0));
        recyclerViewHolder.radAnswer1.setText(readingQuestions.get(position).getAnswers().get(1));
        recyclerViewHolder.radAnswer2.setText(readingQuestions.get(position).getAnswers().get(2));

    }

    @Override
    public int getItemCount() {
        return readingQuestions.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView txtQuestion;
        private RadioButton radAnswer0;
        private RadioButton radAnswer1;
        private RadioButton radAnswer2;


        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            txtQuestion = itemView.findViewById(R.id.readingQuestion);
            radAnswer0 = itemView.findViewById(R.id.radReadingAnswer0);
            radAnswer1 = itemView.findViewById(R.id.radReadingAnswer1);
            radAnswer2 = itemView.findViewById(R.id.radReadingAnswer2);


        }
    }
}
