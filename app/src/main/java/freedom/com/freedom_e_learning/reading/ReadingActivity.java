package freedom.com.freedom_e_learning.reading;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import freedom.com.freedom_e_learning.Constants;
import freedom.com.freedom_e_learning.DatabaseService;
import freedom.com.freedom_e_learning.R;
import freedom.com.freedom_e_learning.model.reading.Reading;

public class ReadingActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private Reading reading;

    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference readingReference;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        topic = (String) getIntent().getStringExtra(String.valueOf(R.string.TOPIC_ID));

        // Set up cái thanh toolbar đó
        mToolbar = findViewById(R.id.ReadingToolbar);
        mToolbar.setTitle(String.format("Topic %s: Reading", topic));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromFirebase();
    }

    public void getDataFromFirebase() {


        readingReference = databaseService.getDatabase().child(Constants.TOPIC_NODE).child(topic).child(Constants.READING_NODE);
        readingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Nhận data từ node reading lưu vào model reading
                reading = dataSnapshot.getValue(Reading.class);
                reading.setTopicId(topic);
                // Set mấy cái tab trong listening nè
                TabLayout tabLayout = (TabLayout) findViewById(R.id.reading_tab_layout);
                tabLayout.addTab(tabLayout.newTab().setText(""));
                tabLayout.addTab(tabLayout.newTab().setText(""));
                tabLayout.getTabAt(0).setIcon(R.drawable.ic_description_24dp);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_adjust_24dp);

                // Set fragment nè
                final ViewPager viewPager = findViewById(R.id.reading_pager);
                ReadingFragmentAdapter adapter = new ReadingFragmentAdapter(getSupportFragmentManager(), reading);
                viewPager.setAdapter(adapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
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