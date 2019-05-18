package freedom.com.freedom_e_learning;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import freedom.com.freedom_e_learning.chart.ChartActivity;
import freedom.com.freedom_e_learning.model.topic.Topic;
import freedom.com.freedom_e_learning.topic.TopicRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    private LoginManager loginManager = LoginManager.getInstance();


//    UserSessionManager session = new UserSessionManager(getApplicationContext());


    private CircleImageView imgAvatar;
    private TextView txtUsername;
    private TextView txtEmail;
    private RecyclerView rv_topics;
    private String mUserID;
    private ArrayList<Topic> topicList = new ArrayList<>();
    private TopicRecyclerViewAdapter topicRecyclerViewAdapter;

    //    Firebase
    FirebaseUser user;
    DatabaseService databaseService = DatabaseService.getInstance();
    DatabaseReference topicReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();
        setEvents();
        new LoadDataTask().execute();


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setControl() {
        getUserID();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        imgAvatar = headerView.findViewById(R.id.imageAvatar);
        txtUsername = headerView.findViewById(R.id.txtUsername);
        txtEmail = headerView.findViewById(R.id.txtEmail);
        rv_topics = findViewById(R.id.topic_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_topics.setLayoutManager(linearLayoutManager);

        topicRecyclerViewAdapter = new TopicRecyclerViewAdapter(this, mUserID);
        topicRecyclerViewAdapter.setTopicList(topicList);
        rv_topics.setAdapter(topicRecyclerViewAdapter);

    }

    private void setEvents() {
        setUserInfo();
    }

    public class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getTopicsFromFireBase();
            return null;
        }
    }

    public void getTopicsFromFireBase() {
//        Lấy đường dẫn tới node topic trong firebase
        topicReference = databaseService.getDatabase().child(Constants.TOPIC_NODE);
        topicReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getData(DataSnapshot dataSnapshot) {
//        Lấy hết tất cả topics thêm vào topic list
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Topic topic = ds.getValue(Topic.class);
            topicList.add(topic);
        }
        if (topicList.size() > 0)
            topicRecyclerViewAdapter.notifyDataSetChanged();
    }


    private void getUserID() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USER_ID)) {
            mUserID = intent.getStringExtra(Constants.USER_ID);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_aboutus) {
            if (item.isChecked()){
                item.setChecked(false);
            }
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_rating) {
            if (item.isChecked()){
                item.setChecked(false);
            }
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            Log.d(TAG, String.valueOf(databaseService.isSignIn()));
            Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            databaseService.signOut();
            if (loginManager != null) {
                loginManager.logOut();
            } else {
//                googleLogOut();
            }
            Log.d(TAG, String.valueOf(databaseService.isSignIn()));
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUserInfo() {
        user = databaseService.getFirebaseAuth().getCurrentUser();
        String email = user.getEmail();
        String userName = user.getDisplayName();
        Uri linkPhoto = user.getPhotoUrl();
        txtUsername.setText(userName);
        txtEmail.setText(email);
        Glide.with(imgAvatar).load(linkPhoto).into(imgAvatar);
    }


//    private void googleLogOut() {
//        Auth.GoogleSignInApi.signOut(session.mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        Toast.makeText(MainActivity.this, "G+ loggedOut", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}
