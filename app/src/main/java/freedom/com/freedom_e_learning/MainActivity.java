package freedom.com.freedom_e_learning;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;
import freedom.com.freedom_e_learning.listening.ListeningActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DatabaseService mData = DatabaseService.getInstance();
    private LoginManager loginManager = LoginManager.getInstance();

//    UserSessionManager session = new UserSessionManager(getApplicationContext());

    private CircleImageView imgAvatar;
    private TextView txtUsername;
    private TextView txtEmail;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setControl();
        setEvents();
        Log.d(TAG, String.valueOf(mData.isSignIn()));
    }

    @Override
    protected void onStart() {
        super.onStart();

//        mGoogleApiClient.connect();
    }

    private void setControl() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        imgAvatar = headerView.findViewById(R.id.imageAvatar);
        txtUsername = headerView.findViewById(R.id.txtUsername);
        txtEmail = headerView.findViewById(R.id.txtEmail);
    }

    private void setEvents() {
        getUserID();
        setUserInfo();

        Button button = findViewById(R.id.btn_listening);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListeningActivity.class);
                startActivity(intent);
            }
        });

        Button test = findViewById(R.id.btn_test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getUserID() {
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.USER_ID)) {
            String mUserID = intent.getStringExtra(Constants.USER_ID);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            Log.d(TAG, String.valueOf(mData.isSignIn()));
            Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            mData.signOut();
            if (loginManager != null) {
                loginManager.logOut();
            } else {
//                googleLogOut();
            }
            Log.d(TAG, String.valueOf(mData.isSignIn()));
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUserInfo() {
        user = mData.getFirebaseAuth().getCurrentUser();
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
