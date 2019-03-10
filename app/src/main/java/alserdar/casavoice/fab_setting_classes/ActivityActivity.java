package alserdar.casavoice.fab_setting_classes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.Home;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.models.UserModel;

public class ActivityActivity extends AppCompatActivity {

    private int count = 0;
    Button dailyCoins ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity);
        dailyCoins = findViewById(R.id.dailyCoins);

        Timer timer = new Timer();
        long day = 1000*60*60*10;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        count++;
                        dailyCoins.setVisibility(View.VISIBLE);
                    }
                });
            }
        }, 0, 20000);

        dailyCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkMyBalanceAndIncreaseIt(getBaseContext());
                dailyCoins.setVisibility(View.GONE);
            }
        });

    }

    private void checkMyBalanceAndIncreaseIt(final Context context)
    {

        String facebookUserName = DetectUserInfo.faceBookUser(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel user = documentSnapshot.toObject(UserModel.class);

                int balance = user.getBalanceOfCoins();

                doc.update("balanceOfCoins" , balance + 500).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new OurToast().myToast(context , "moneeeeeey");
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext() , Home.class);
        startActivity(i);
        finish();
    }
}
