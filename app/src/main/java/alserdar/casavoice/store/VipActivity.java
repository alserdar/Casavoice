package alserdar.casavoice.store;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.models.UserModel;

public class VipActivity extends AppCompatActivity implements View.OnClickListener {


    LinearLayout legendaryLay , royalLay , goldenLay , silverLay , vipLay ;
    Button okVip ;

    TextView legendary , royal , golden , silver , vip ;
    TextView legCash , royalCash , goldenCash , silverCash , vipCash ;

    boolean legSelected , royalSelected , goldenSelected , silverSelected , vipSelected ;

    private int balance ;
    String facebookUserName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        facebookUserName = getInfo.getString("FacebookUser", "FacebookUser");

        theBalance();


        legendaryLay = findViewById(R.id.legendaryLay);
        royalLay = findViewById(R.id.royalLay);
        goldenLay = findViewById(R.id.goldenLay);
        silverLay = findViewById(R.id.silverLay);
        vipLay = findViewById(R.id.vipLay);
        okVip = findViewById(R.id.okVIP);

        legendary = findViewById(R.id.legendary);
        royal = findViewById(R.id.royal);
        golden = findViewById(R.id.golden);
        silver = findViewById(R.id.silver);
        vip = findViewById(R.id.vip);


        legCash = findViewById(R.id.legCash);
        royalCash = findViewById(R.id.royalCash);
        goldenCash = findViewById(R.id.goldenCash);
        silverCash = findViewById(R.id.silverCash);
        vipCash = findViewById(R.id.vipCash);



        legendaryLay.setOnClickListener(this);
        royalLay.setOnClickListener(this);
        goldenLay.setOnClickListener(this);
        silverLay.setOnClickListener(this);
        vipLay.setOnClickListener(this);

        okVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (legSelected)
                {
                    theBalance();
                    if (balance >= 49999)
                    {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                        doc.update("vip", "LegendaryVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final int senderNewBalance = (balance - 49999) ;
                                doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new OurToast().myToast(getBaseContext(), "You are a LEGENDARY");
                                    }
                                });
                            }
                        });

                        Intent i = new Intent(getBaseContext() , Store.class);
                        startActivity(i);
                        finish();

                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Recharge your balance");
                    }

                }else if (royalSelected)
                {
                    theBalance();
                    if (balance >= 39999)
                    {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);

                        doc.update("vip", "RoyalVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                final int senderNewBalance = (balance - 39999) ;
                                doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new OurToast().myToast(getBaseContext(), "You are a Royal Now");
                                    }
                                });
                            }
                        });
                        Intent i = new Intent(getBaseContext() , Store.class);
                        startActivity(i);
                        finish();
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Recharge your balance");
                    }

                }else if(goldenSelected)
                {
                    theBalance();
                    if (balance >= 29999)
                    {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);

                        doc.update("vip", "GoldenVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                final int senderNewBalance = (balance - 29999) ;
                                doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new OurToast().myToast(getBaseContext(), "You got a Golden VIP");
                                    }
                                });


                            }
                        });

                        Intent i = new Intent(getBaseContext() , Store.class);
                        startActivity(i);
                        finish();

                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Recharge your balance");
                    }

                }else if (silverSelected)
                {
                    theBalance();
                    if (balance >= 19999)
                    {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);

                        doc.update("vip", "SilverVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                final int senderNewBalance = (balance - 19999) ;
                                doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new OurToast().myToast(getBaseContext(), "Silver VIP For You");
                                    }
                                });
                            }
                        });

                        Intent i = new Intent(getBaseContext() , Store.class);
                        startActivity(i);
                        finish();
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Recharge your balance");
                    }
                }else if (vipSelected)
                {
                    theBalance();
                    if (balance >= 4999)
                    {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                        doc.update("vip", "RegularVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                final int senderNewBalance = (balance - 4999) ;
                                doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        new OurToast().myToast(getBaseContext(), "You are a VIP");
                                    }
                                });

                            }
                        });
                        Intent i = new Intent(getBaseContext() , Store.class);
                        startActivity(i);
                        finish();
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Recharge your balance");
                    }

                }

            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.legendaryLay :
                legendaryLay.setBackgroundResource(R.drawable.selector);
                royalLay.setBackgroundResource(R.drawable.gift_selector);
                goldenLay.setBackgroundResource(R.drawable.gift_selector);
                silverLay.setBackgroundResource(R.drawable.gift_selector);
                vipLay.setBackgroundResource(R.drawable.gift_selector);

                legSelected = true ;
                royalSelected = false ;
                goldenSelected = false ;
                silverSelected = false ;
                vipSelected = false ;

                new OurToast().myToast(getBaseContext() , "Become Legendary of Casavoice");
                theBalance();
                break;

            case R.id.royalLay :
                legendaryLay.setBackgroundResource(R.drawable.gift_selector);
                royalLay.setBackgroundResource(R.drawable.selector);
                goldenLay.setBackgroundResource(R.drawable.gift_selector);
                silverLay.setBackgroundResource(R.drawable.gift_selector);
                vipLay.setBackgroundResource(R.drawable.gift_selector);

                legSelected = false ;
                royalSelected = true ;
                goldenSelected = false ;
                silverSelected = false ;
                vipSelected = false ;
                new OurToast().myToast(getBaseContext() , "Go with Royality");
                theBalance();
                break;

            case R.id.goldenLay :
                legendaryLay.setBackgroundResource(R.drawable.gift_selector);
                royalLay.setBackgroundResource(R.drawable.gift_selector);
                goldenLay.setBackgroundResource(R.drawable.selector);
                silverLay.setBackgroundResource(R.drawable.gift_selector);
                vipLay.setBackgroundResource(R.drawable.gift_selector);

                legSelected = false ;
                royalSelected = false ;
                goldenSelected = true ;
                silverSelected = false ;
                vipSelected = false ;
                new OurToast().myToast(getBaseContext() , "Are you Goldy !");
                theBalance();
                break;

            case R.id.silverLay :
                legendaryLay.setBackgroundResource(R.drawable.gift_selector);
                royalLay.setBackgroundResource(R.drawable.gift_selector);
                goldenLay.setBackgroundResource(R.drawable.gift_selector);
                silverLay.setBackgroundResource(R.drawable.selector);
                vipLay.setBackgroundResource(R.drawable.gift_selector);

                legSelected = false ;
                royalSelected = false ;
                goldenSelected = false ;
                silverSelected = true ;
                vipSelected = false ;

                new OurToast().myToast(getBaseContext() , "Silver Is Cool");
                theBalance();
                break;

            case R.id.vipLay :
                legendaryLay.setBackgroundResource(R.drawable.gift_selector);
                royalLay.setBackgroundResource(R.drawable.gift_selector);
                goldenLay.setBackgroundResource(R.drawable.gift_selector);
                silverLay.setBackgroundResource(R.drawable.gift_selector);
                vipLay.setBackgroundResource(R.drawable.selector);

                legSelected = false ;
                royalSelected = false ;
                goldenSelected = false ;
                silverSelected = false ;
                vipSelected = true ;
                new OurToast().myToast(getBaseContext() , "VIP Why not ?");
                theBalance();
                break;

        }
    }

    private int theBalance ()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel user = documentSnapshot.toObject(UserModel.class);

                balance = user.getBalanceOfCoins();

            }
        });

        return balance ;
    }


}
