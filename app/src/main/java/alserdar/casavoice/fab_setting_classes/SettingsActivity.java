package alserdar.casavoice.fab_setting_classes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;

public class SettingsActivity extends AppCompatActivity {


    Button logOut ;

    TextView ads , termsAndConditions , privacyPolicies , contactUs , aboutCasavoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        ads = findViewById(R.id.ads);
        termsAndConditions = findViewById(R.id.terms);
        privacyPolicies = findViewById(R.id.privacy);
        contactUs = findViewById(R.id.contactUs);
        aboutCasavoice= findViewById(R.id.aboutCasavoice);
        logOut= findViewById(R.id.logOut);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check if user is currently logged in
                if (AccessToken.getCurrentAccessToken() != null ){

                    LoginManager.getInstance().logOut();
                    finish();
                }
            }
        });

        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new OurToast().myToast(getBaseContext() , "under structure !!");
            }
        });

        SpannableString string = new SpannableString("Terms and Conditions");
        string.setSpan(new UnderlineSpan(), 0, string.length(), 0);
        termsAndConditions.setText(string);

        SpannableString string2 = new SpannableString("Privacy Policy");
        string2.setSpan(new UnderlineSpan(), 0, string2.length(), 0);
        privacyPolicies.setText(string2);

        SpannableString string3 = new SpannableString("Contact US");
        string3.setSpan(new UnderlineSpan(), 0, string3.length(), 0);
        contactUs.setText(string3);

        privacyPolicies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.casavoicelive.wordpress.com"));
                startActivity(intent);

            }
        });

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://casavoicelive.wordpress.com/contact-us/"));
                startActivity(intent);

            }
        });


        termsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://casavoicelive.wordpress.com/blog/"));
                startActivity(intent);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
