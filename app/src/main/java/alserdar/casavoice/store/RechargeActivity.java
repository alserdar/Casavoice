package alserdar.casavoice.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.R;
import alserdar.casavoice.load_stuff.LoadBaseCode;
import alserdar.casavoice.models.UserModel;

public class RechargeActivity extends AppCompatActivity {

    TextView balance ;
    String theUID;

    Button coins100 , coins1000 , coins6500 , coins15000 , coins50000 , coins90000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

        balance = findViewById(R.id.yourBalanceFromRechargeActivity);

        theUID = DetectUserInfo.theUID(getBaseContext());
        LoadBaseCode.readBalance(getBaseContext() , theUID , balance);

        coins100 = findViewById(R.id.Coins100);
        coins1000 = findViewById(R.id.Coins1000);
        coins6500 = findViewById(R.id.Coins6500);
        coins15000 = findViewById(R.id.Coins15000);
        coins50000 = findViewById(R.id.Coins50000);
        coins90000 = findViewById(R.id.Coins90000);

        coins100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new OurToast().myToast(getBaseContext() , "Recharge and Payment Method Coming Soon");
            }
        });

        coins1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new OurToast().myToast(getBaseContext() , "Recharge and Payment Method Coming Soon");
            }
        });

        coins6500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new OurToast().myToast(getBaseContext() , "Recharge and Payment Method Coming Soon");
            }
        });

        coins15000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new OurToast().myToast(getBaseContext() , "Recharge and Payment Method Coming Soon");
            }
        });

        coins50000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new OurToast().myToast(getBaseContext() , "Recharge and Payment Method Coming Soon");
            }
        });


        coins90000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new OurToast().myToast(getBaseContext() , "Recharge and Payment Method Coming Soon");
            }
        });

    }

    private void readBalance()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                balance.setText(String.format("%s %s", getString(R.string.your_balance), String.valueOf(user.getBalanceOfCoins())));

                            }else
                            {

                            }

                        }
                    });
                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }).start();

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext() , Store.class);
        startActivity(i);
        finish();
    }
}
