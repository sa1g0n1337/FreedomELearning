package freedom.com.freedom_e_learning;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import freedom.com.freedom_e_learning.model.User;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = SignInActivity.class.getSimpleName();
    public static final int RC_SIGN_IN = 9001;

    CallbackManager callbackManager;
    LoginButton loginButton;
    SignInButton signInButton;
    public GoogleSignInClient mGoogleSignInClient;
    private DatabaseService mData = DatabaseService.getInstance();
    private FirebaseAuth mFirebaseAuth;

//    UserSessionManager session = new UserSessionManager(SignInActivity.this);

    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setControl();
        setEvents();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mData.isSignIn()) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            intent.putExtra(Constants.USER_ID, mData.getUserID());
            startActivity(intent);
            finish();
        }
    }

    public void setControl() {
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void setEvents() {
        loginFacebook();
//        loginGoogle();
    }

    public void loginFacebook() {
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile", "user_friends"));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess");
                Toast.makeText(SignInActivity.this, "On Success", Toast.LENGTH_SHORT).show();
                handleFacebookResult(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
                Toast.makeText(SignInActivity.this, "On Cancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "onError");
                Toast.makeText(SignInActivity.this, "On Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void loginGoogle() {
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(session.mGoogleApiClient);
//                startActivityForResult(signInIntent, RC_SIGN_IN);
//            }
//        });
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleResult(task);
        }
    }

    private void handleFacebookResult(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth = mData.getFirebaseAuth();
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User();
                    user.setEmail(task.getResult().getUser().getEmail());
                    user.setName(task.getResult().getUser().getDisplayName());
                    user.setProfilePicture(task.getResult().getUser().getPhotoUrl().toString());
                    user.setUid(task.getResult().getUser().getUid());

                    createUserOnFirebase(user);

                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    intent.putExtra(Constants.USER_ID, user.getUid());
                    startActivity(intent);
                }
            }
        });
    }

    private void handleGoogleResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Toast.makeText(SignInActivity.this, "Signed in successfully, show authenticated UI", Toast.LENGTH_SHORT);
            Log.d(TAG, "Signed in successfully, show authenticated UI");
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                User user = new User();

                Uri personPhoto = acct.getPhotoUrl();

                user.setEmail(acct.getEmail());
                user.setName(acct.getDisplayName());
                user.setProfilePicture(String.valueOf(personPhoto));
                user.setUid(acct.getId());

                createUserOnFirebase(user);

                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.putExtra(Constants.USER_ID, user.getUid());
                Log.d(TAG, String.valueOf(mGoogleSignInClient));
                intent.putExtra("GoogleSignInOption", gso);
                startActivity(intent);

            } else {
                Log.d(TAG, "No account");
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(SignInActivity.this, "Signed in Fail", Toast.LENGTH_SHORT);
            Log.d(TAG, "Signed in failed");

        }
    }

    private void createUserOnFirebase(final User user) {
        // Create node "User/<user.uid>"
        final DatabaseReference userNode = mData.createDatabase("User").child(user.getUid());
        userNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    userNode.setValue(user);
                    Log.d(TAG, "Success");
                } else {
                    Log.d(TAG, "Failed");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
