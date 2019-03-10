package alserdar.casavoice.store.profile_functions;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.UserModel;

public class GetNicePet extends AppCompatActivity {

    private static int getBalance;
    private static int minusGetHappiness;
    private static int plusGetHappiness;
    String theUID ;
    Button dog , rabbit ,  hmama , ghazal , camel , tiger , lion , tawoos , whiteHorse ;
    SoundPool sp;
    int[] soundIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_nice_pet);
        ActionBar actionBar =getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        theUID = DetectUserInfo.theUID(getBaseContext());

        AudioAttributes attrs = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attrs)
                    .build();
            soundIds = new int[10];
            soundIds[0] = sp.load(getBaseContext(), R.raw.ring, 1);
        }else
        {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundIds = new int[10];
            soundIds[0] = sp.load(getBaseContext(), R.raw.ring, 1);

        }


        dog = findViewById(R.id.dogPet);
        rabbit = findViewById(R.id.rabbitPet);
        hmama = findViewById(R.id.hmamaPet);
        ghazal = findViewById(R.id.ghazalPet);
        camel = findViewById(R.id.camelPet);
        tiger = findViewById(R.id.tigerPet);
        lion = findViewById(R.id.lionPet);
        tawoos = findViewById(R.id.tawoosPet);
        whiteHorse = findViewById(R.id.whiteHorsePet);



        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 2000 , getString(R.string.Dog) , 60);
            }
        });

        rabbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 4000 , getString(R.string.Rabbit) , 100);
            }
        });

        hmama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 8000 , getString(R.string.Dove) , 150);
            }
        });


        ghazal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 16000 , getString(R.string.Deer) , 200);
            }
        });


        camel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 32000 , getString(R.string.Camel) , 250);
            }
        });


        tiger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 64000 , getString(R.string.Tiger) , 300);
            }
        });

        lion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 128000 , getString(R.string.Lion) , 350);
            }
        });

        tawoos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 256000 , getString(R.string.Peacock) , 400);
            }
        });

        whiteHorse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 500000 , getString(R.string.Horse) , 450);
            }
        });

    }

    private void buyItem(String UID, final int itemPrice, final String thePet, final int happinessPet) {
        theBalance(UID);
        minusTheHappiness(UID);
        plusTheHappiness(UID);


        if (getBalance >= itemPrice)
        {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference doc = db.collection("UserInformation").document(UID);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists())
                    {
                        // UserModel model = documentSnapshot.toObject(UserModel.class);
                        doc.update("pet" , thePet).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                int newBalance = getBalance - itemPrice ;
                                doc.update("balanceOfCoins" , newBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        doc.update("happinessPerson" , minusGetHappiness).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                doc.update("happinessPet" , happinessPet).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        doc.update("happinessPerson" , plusGetHappiness).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                new OurToast().myToast(getBaseContext() , getString(R.string.ok));

                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                }
            });
        }else
        {
            new OurToast().myToast(getBaseContext() , getString(R.string.not_enough_cash));
        }

    }

    private static int theBalance(String theUID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);

                    getBalance = user.getBalanceOfCoins();
                }else
                {

                }


            }
        });

        return getBalance;
    }



    private static int minusTheHappiness(String theUID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);

                    minusGetHappiness = user.getHappinessPerson() - user.getHappinessPet();

                }else
                {

                }

            }
        });

        return minusGetHappiness;
    }

    private static int plusTheHappiness(String theUID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);

                    plusGetHappiness = user.getHappinessPerson() + user.getHappinessPet();

                }else
                {

                }

            }
        });

        return plusGetHappiness;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
