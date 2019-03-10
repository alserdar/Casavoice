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

public class GetNiceCar extends AppCompatActivity {


    private static int getBalance;
    private static int minusGetHappiness;
    private static int plusGetHappiness;
    Button toyota , mitsubishi , ford , audi , bmw , mercedes , jaguar , cadillac , ferrarii , rolsRoyce ;
    SoundPool sp;
    int[] soundIds;
    String theUID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_nice_car);
        ActionBar actionBar =getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
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

        theUID = DetectUserInfo.theUID(GetNiceCar.this);
        theBalance(theUID);
        minusTheHappiness(theUID);
        plusTheHappiness(theUID);


        toyota = findViewById(R.id.toyota);
        mitsubishi = findViewById(R.id.mitsubishi);
        audi = findViewById(R.id.audi);
        ford = findViewById(R.id.ford);
        bmw = findViewById(R.id.bmw);
        mercedes = findViewById(R.id.mercedes);
        jaguar = findViewById(R.id.jaguar);
        cadillac = findViewById(R.id.cadillac);
        ferrarii = findViewById(R.id.ferrarii);
        rolsRoyce = findViewById(R.id.rolsrois);


        toyota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 2000 , getString(R.string.Toyota) , 60);

            }
        });

        mitsubishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 4000 , getString(R.string.Mitsubishi) , 100);
            }
        });
        ford.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 8000 , getString(R.string.Ford) , 150);
            }
        });


        audi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 16000 , getString(R.string.Audi) , 200);
            }
        });



        bmw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 32000 , getString(R.string.BMW) , 250);
            }
        });

        mercedes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 64000 , getString(R.string.Mercedes) , 300);
            }
        });

        jaguar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 128000 , getString(R.string.Jaguar) , 350);
            }
        });

        cadillac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 256000 , getString(R.string.Cadillac) , 400);
            }
        });

        ferrarii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 500000 , getString(R.string.Ferrari) , 450);
            }
        });

        rolsRoyce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                buyItem(theUID , 1000000 , getString(R.string.Rolls_royce) , 500);
            }
        });


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

                    minusGetHappiness = user.getHappinessPerson() - user.getHappinessCar();

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

                    plusGetHappiness = user.getHappinessPerson() + user.getHappinessCar();

                }else
                {

                }

            }
        });

        return plusGetHappiness;
    }


    private void buyItem(String UID , final int itemPrice , final String carModel , final int happinessCar)
    {
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
                        doc.update("car" , carModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                int newBalance = getBalance - itemPrice ;
                                doc.update("balanceOfCoins" , newBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        doc.update("happinessPerson" , minusGetHappiness).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                doc.update("happinessCar" , happinessCar).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
