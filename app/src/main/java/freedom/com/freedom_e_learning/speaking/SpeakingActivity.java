package freedom.com.freedom_e_learning.speaking;

import android.content.Intent;
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
import freedom.com.freedom_e_learning.model.speaking.Speaking;

public class SpeakingActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Speaking speaking;
    String userId;

    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference speakingReference;
    private String topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speaking);

        topic = (String) getIntent().getStringExtra(String.valueOf(R.string.TOPIC_ID));
        // Set up cái thanh toolbar đó
        mToolbar = findViewById(R.id.SpeakingToolbar);
        mToolbar.setTitle(String.format("Topic %s: Speaking", topic));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getDataFromFirebase();
    }

    public void getDataFromFirebase() {
        Intent userIdIntent = getIntent();
        if (userIdIntent.hasExtra(Constants.USER_ID)) {
            userId = userIdIntent.getStringExtra(Constants.USER_ID);
        }

        speakingReference = databaseService.getDatabase().child(Constants.TOPIC_NODE).child(topic).child(Constants.SPEAKING_NODE);
        speakingReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Nhận data từ node speaking lưu vào model speaking
                speaking = dataSnapshot.getValue(Speaking.class);
                // Set mấy cái tab trong listening nè
                TabLayout tabLayout = (TabLayout) findViewById(R.id.speaking_tab_layout);
                tabLayout.addTab(tabLayout.newTab().setText(""));
                tabLayout.addTab(tabLayout.newTab().setText(""));

                tabLayout.getTabAt(0).setIcon(R.drawable.ic_mic_24dp);
                tabLayout.getTabAt(1).setIcon(R.drawable.ic_comment_24dp);

                // Set fragment nè
                final ViewPager viewPager = findViewById(R.id.speaking_pager);
                SpeakingFragmentAdapter adapter = new SpeakingFragmentAdapter(getSupportFragmentManager(), speaking, userId);
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
