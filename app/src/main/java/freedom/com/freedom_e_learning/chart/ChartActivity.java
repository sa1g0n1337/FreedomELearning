package freedom.com.freedom_e_learning.chart;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;

public class ChartActivity extends AppCompatActivity {
    private Toolbar Ctoolbar;
    DatabaseService databaseService = DatabaseService.getInstance();
    String UserId;
    DatabaseReference listeningRef, readingRef;
    private RecyclerView rvChart;
    private ChartAdapter chartAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        setControl();
        getListeningFromFirebase();
    }

    private void setControl() {
        Ctoolbar = findViewById(R.id.ChartToolbar);
        Ctoolbar.setTitle(String.format("Rating"));
        setSupportActionBar(Ctoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getListeningFromFirebase() {
        UserId = databaseService.getFirebaseAuth().getUid();
        final HashMap<String, HashMap> listeningHashmap = new HashMap();
        listeningRef = databaseService.getDatabase().child("Listening Answer").child(UserId);
        listeningRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        listeningHashmap.put(ds.getKey(), (HashMap) ds.getValue());
                    }

                    getReadingFromFirebase(listeningHashmap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getReadingFromFirebase(final HashMap<String, HashMap> listeningHashmap) {
        final HashMap<String, HashMap> readingHashmap = new HashMap();
        readingRef = databaseService.getDatabase().child("Reading Answer").child(UserId);
        readingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        readingHashmap.put(ds.getKey(), (HashMap) ds.getValue());
                    }

                    setTopicToAdapter(listeningHashmap, readingHashmap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setTopicToAdapter(HashMap<String, HashMap> listeningHashmap, HashMap<String, HashMap> readingHashmap) {
        rvChart = findViewById(R.id.rv_chart);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChart.setLayoutManager(linearLayoutManager);

        chartAdapter = new ChartAdapter(this, listeningHashmap, readingHashmap);
        rvChart.setAdapter(chartAdapter);
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
