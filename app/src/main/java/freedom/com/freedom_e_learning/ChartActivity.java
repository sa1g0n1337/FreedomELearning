package freedom.com.freedom_e_learning;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;


import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    private Toolbar Ctoolbar;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setControl();
        chartdata();
    }

    private void setControl() {
        Ctoolbar = findViewById(R.id.ChartToolbar);
        Ctoolbar.setTitle(String.format("Rating"));
        setSupportActionBar(Ctoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barChart = findViewById(R.id.ratingChart);
    }

    private void chartdata(){

        BarDataSet barListening = new BarDataSet(listeningentries(),"Listening");
        barListening.setColor(Color.RED);

        BarDataSet barReading = new BarDataSet(readingentries(),"Reading");
        barReading.setColor(Color.BLUE);

        BarData data = new BarData(barListening,barReading);
        barChart.setData(data);

        String[]days = new String[]{"1/5","2/5","3/5","4/5"};

        XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
            xAxis.setCenterAxisLabels(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setGranularityEnabled(true);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(3);

        float barSpace =0.1f;
        float groupSpace = 0.5f;
        data.setBarWidth(0.15f);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupSpace,barSpace)*4);
        //barChart.getAxisLeft().setAxisMaximum(0);
        barChart.setDescription(null);
        barChart.groupBars(0,groupSpace,barSpace);
        barChart.invalidate();

    }

    private ArrayList<BarEntry> listeningentries(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1,30));
        entries.add(new BarEntry(2,20));
        entries.add(new BarEntry(3,90));
        entries.add(new BarEntry(4,80));
        return entries;
    }
    private ArrayList<BarEntry> readingentries(){
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1,60));
        entries.add(new BarEntry(2,90));
        entries.add(new BarEntry(3,30));
        entries.add(new BarEntry(4,70));
        return entries;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
