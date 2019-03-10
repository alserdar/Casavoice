package alserdar.casavoice.login_package;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import alserdar.casavoice.Home;
import alserdar.casavoice.Launcher;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;

public class ContinueWithFacebook extends AppCompatActivity {

    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    LoginButton loginButton ;
    private FirebaseUser firebaseUser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        setContentView(R.layout.activity_continue_with_facebook);
        mAuth = FirebaseAuth.getInstance();


        final LinearLayout avlLay = findViewById(R.id.aviLay);
        avlLay.setVisibility(View.VISIBLE);
        loginButton = findViewById(R.id.continueWithFacebook);
        loginButton.setReadPermissions("email", "public_profile");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {

                handleFacebookAccessToken(loginResult.getAccessToken());

                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            Log.v("facebook - profile", profile2.getFirstName());
                            Log.v("facebook - id", profile2.getId());
                            SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(ContinueWithFacebook.this);
                            final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                            editor.putString("FacebookUser" , profile2.getFirstName());
                            editor.putString("user_id" , profile2.getId());
                            editor.apply();
                            mProfileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                    Log.v("facebook - id", profile.getId());
                    SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(ContinueWithFacebook.this);
                    final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                    editor.putString("FacebookUser" , profile.getFirstName());
                    editor.putString("user_id" , profile.getId());
                    editor.apply();


                }
            }

            @Override
            public void onCancel()
            {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }
        });


    }



    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        loginButton.setVisibility(View.GONE);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(ContinueWithFacebook.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            firebaseUser = mAuth.getCurrentUser();
                            SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(ContinueWithFacebook.this);
                            final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                            editor.putString("uid" , mAuth.getCurrentUser().getUid());
                            editor.apply();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(ContinueWithFacebook.this);
                            String facebookId = getInfo.getString("user_id" , "user_id");
                            final String facebookUserName = getInfo.getString("FacebookUser" , "FacebookUser");
                            db.collection("UserInformation").whereEqualTo("theUID", mAuth.getUid())
                                    .limit(1).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                boolean isEmpty = task.getResult().isEmpty();
                                                if(isEmpty)
                                                {
                                                    Intent i = new Intent(getBaseContext() , RegisterForCasavoice.class);
                                                    startActivity(i);
                                                    finish();
                                                }else
                                                {
                                                    new OurToast().myToast(getBaseContext() ,String.format(" %s %s " , getString(R.string.logging_as) , facebookUserName));
                                                    Intent i = new Intent(getBaseContext() , Home.class);
                                                    startActivity(i);
                                                    finish();
                                                }

                                            } else {
                                            }
                                        }
                                    });
                        } else {

                            new OurToast().myToast(getBaseContext() , getString(R.string.Authentication_failed));
                            Intent back = new Intent(getBaseContext() , Launcher.class);
                            startActivity(back);
                            finish();
                        }
                    }
                });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public void onBackPressed() {
        new OurToast().myToast(getBaseContext() , getString(R.string.you_must_login_first));
    }
}
