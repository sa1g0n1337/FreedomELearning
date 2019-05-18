package freedom.com.freedom_e_learning.listening;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.listening.Listening;

public class ListeningActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Listening listening;
    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference listeningReference;
    private String topic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);

        // Set up cái thanh toolbar đó
        topic = getIntent().getStringExtra(String.valueOf(R.string.TOPIC_ID));


        mToolbar = findViewById(R.id.ListeningToolbar);
        mToolbar.setTitle(String.format("Topic %s: Listening", topic));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getDataFromFirebase();
    }

    public void getDataFromFirebase() {
        listeningReference = databaseService.getDatabase().child(Constants.TOPIC_NODE).child(String.valueOf(topic)).child(Constants.LISTENING_NODE);
        listeningReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Nhận data từ node listening lưu vào model listening
                listening = dataSnapshot.getValue(Listening.class);
                listening.setTopicId(topic);

                // Set mấy cái tab trong listening nè
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                tabLayout.addTab(tabLayout.newTab().setText(""));
                tabLayout.addTab(tabLayout.newTab().setText(""));
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_headset_24dp);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_speaker_notes_24dp);

                // Set fragment nè
                final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
                final ListeningFragmentAdapter adapter = new ListeningFragmentAdapter(getSupportFragmentManager(), listening);
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        Log.d("ListeningActivity", String.valueOf(tab.getPosition()));
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
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
