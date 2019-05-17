package freedom.com.freedom_e_learning.chart;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import freedom.com.freedom_e_learning.R;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.RecyclerViewHolder> {


    Context context;
    HashMap<String, HashMap> listeningData, readingData;

    public ChartAdapter(Context context, HashMap<String, HashMap> listeningData, HashMap<String, HashMap> readingData) {
        this.context = context;
        this.listeningData = listeningData;
        this.readingData = readingData;
    }


    @NonNull
    @Override
    public ChartAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.chart_items, viewGroup, false);
        ChartAdapter.RecyclerViewHolder recyclerViewHolder = new ChartAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    // Set questions
    @Override
    public void onBindViewHolder(@NonNull ChartAdapter.RecyclerViewHolder recyclerViewHolder, int position) {
        String topic = String.format("Topic %d", position + 1);
        recyclerViewHolder.chartTopic.setText(topic);

        String[] readingdays = getReadingDays(topic);
        String[] listeningdays = getListeningDays(topic);
        String[] days = getTotalDays(readingdays, listeningdays);

        BarDataSet barListening = new BarDataSet(listeningentries(topic, days), "Listening");
        barListening.setColor(Color.RED);

        BarDataSet barReading = new BarDataSet(readingentries(topic, days), "Reading");
        barReading.setColor(Color.BLUE);

        BarData data = new BarData(barListening, barReading);
        recyclerViewHolder.barChart.setData(data);

        XAxis xAxis = recyclerViewHolder.barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        recyclerViewHolder.barChart.setDragEnabled(true);
        recyclerViewHolder.barChart.setVisibleXRangeMaximum(3);

        float barSpace = 0.1f;
        float groupSpace = 0.5f;
        data.setBarWidth(0.15f);
        recyclerViewHolder.barChart.getAxisLeft().setAxisMaximum(100);
        recyclerViewHolder.barChart.getAxisRight().setAxisMaximum(100);
        recyclerViewHolder.barChart.getXAxis().setAxisMinimum(0);
        recyclerViewHolder.barChart.getXAxis().setAxisMaximum(0 + recyclerViewHolder.barChart.getBarData().getGroupWidth(groupSpace, barSpace) * days.length);
        recyclerViewHolder.barChart.setDescription(null);
        recyclerViewHolder.barChart.groupBars(0, groupSpace, barSpace);
        recyclerViewHolder.barChart.invalidate();

    }

    @Override
    public int getItemCount() {
        return 10;
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
        BarChart barChart;
        TextView chartTopic;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            barChart = itemView.findViewById(R.id.chart1);
            chartTopic = itemView.findViewById(R.id.tv_chart_topic);
        }
    }

    private ArrayList<BarEntry> listeningentries(String topic, String[] days) {

        HashMap<String, HashMap<String, String>> listeningDataTopic = listeningData.get(topic);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> listeningDays = new ArrayList<>();
        if (listeningDataTopic != null) {

            int counter = 1;
            for (Map.Entry<String, HashMap<String, String>> entry : listeningDataTopic.entrySet()) {
                listeningDays.add(entry.getKey());
            }
            Collections.sort(listeningDays);

            for (String s : days) {
                if (listeningDataTopic.get(s) != null) {
                    int j = Integer.parseInt(String.valueOf(listeningDataTopic.get(s)).split("\\.")[0]);
                    entries.add(new BarEntry(counter, j));
                    counter++;
                } else {
                    entries.add(new BarEntry(counter, 0));
                    counter++;
                }
            }

        } else {
            entries.add(new BarEntry(0, 0));
        }

        return entries;
    }

    private ArrayList<BarEntry> readingentries(String topic, String[] days) {
        HashMap<String, HashMap<String, String>> readingDataTopic = readingData.get(topic);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> readingDays = new ArrayList<>();
        if (readingDataTopic != null) {
            int counter = 1;
            for (Map.Entry<String, HashMap<String, String>> entry : readingDataTopic.entrySet()) {
                readingDays.add(entry.getKey());
            }
            Collections.sort(readingDays);

            for (String s : days) {
                if (readingDataTopic.get(s) != null) {
                    int j = Integer.parseInt(String.valueOf(readingDataTopic.get(s)).split("\\.")[0]);
                    entries.add(new BarEntry(counter, j));
                    counter++;
                } else {
                    entries.add(new BarEntry(counter, 0));
                    counter++;
                }
            }
        } else {
            entries.add(new BarEntry(0, 0));
        }

        return entries;
    }

    private String[] getReadingDays(String topic) {
        HashMap<String, HashMap<String, String>> readingDataTopic = readingData.get(topic);
        ArrayList<String> result = new ArrayList<>();
        if (readingDataTopic != null) {

            for (Map.Entry<String, HashMap<String, String>> entry : readingDataTopic.entrySet()) {
                result.add(entry.getKey());

            }
        }

        return result.toArray(new String[0]);
    }

    private String[] getListeningDays(String topic) {
        HashMap<String, HashMap<String, String>> readingDataTopic = listeningData.get(topic);
        ArrayList<String> result = new ArrayList<>();
        if (readingDataTopic != null) {

            for (Map.Entry<String, HashMap<String, String>> entry : readingDataTopic.entrySet()) {
                result.add(entry.getKey());

            }
        }

        return result.toArray(new String[0]);
    }

    private String[] getTotalDays(String[] readingDays, String[] listeningDays) {
        ArrayList<String> result = new ArrayList<>();
        for (String s : readingDays) {
            if (!result.contains(s)) {
                result.add(s);
            }
        }

        for (String s : listeningDays) {
            if (!result.contains(s)) {
                result.add(s);
            }
        }
        Collections.sort(result);
        return result.toArray(new String[0]);
    }

}

