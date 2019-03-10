package alserdar.casavoice.store;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.store.profile_functions.GetNiceCar;
import alserdar.casavoice.store.profile_functions.GetNiceHouse;
import alserdar.casavoice.store.profile_functions.GetNicePet;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.UserModel;

public class Store extends AppCompatActivity {

    LinearLayout recharge , vip , lockRoom , houses , cars , animals , specialOrders ;
    String theUID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference doc;
    SoundPool sp;
    int[] soundIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

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
        theUID = DetectUserInfo.theUID(getBaseContext());

        hasRoomOrNot();
        hasVIPOrNo();

        specialOrders = findViewById(R.id.specialOrdersLayout);
        vip = findViewById(R.id.vipLayout);
        lockRoom = findViewById(R.id.lockRoomLayout);
        houses = findViewById(R.id.housesLayout);
        cars = findViewById(R.id.carsLayout);
        animals = findViewById(R.id.animalsLayout);
        recharge = findViewById(R.id.rechargeLayout);


        recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , RechargeActivity.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });


        specialOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto","casavoice.app@gmail.com", null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                            startActivity(Intent.createChooser(emailIntent, "Send email..."));
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

        houses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , GetNiceHouse.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

        animals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , GetNicePet.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

        cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , GetNiceCar.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });
    }

    private void hasVIPOrNo()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);

                                final String hasVip = user.getVip();

                                vip.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sp.play(soundIds[0], 1, 1, 1, 0, 1);

                                        Intent i = new Intent(getBaseContext() , VipTabActivity.class);
                                        i.putExtra("vipStatus" , hasVip);
                                        startActivity(i);
                                    }
                                });
                            }else
                            {

                            }

                        }
                    });
                }catch (Exception e )
                {
                    e.getMessage();
                }
            }
        }).start();

    }

    private void hasRoomOrNot() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                final boolean hasRoomOrNo = user.isHasRoom();

                                lockRoom.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        sp.play(soundIds[0], 1, 1, 1, 0, 1);

                                        if (hasRoomOrNo)
                                        {
                                            Intent i = new Intent(getBaseContext() , LockRoom.class);
                                            startActivity(i);
                                        }else
                                        {
                                            new OurToast().myToast(getBaseContext() , getString(R.string.create_your_room_first));
                                        }

                                    }
                                });

                            }else
                            {

                            }

                        }
                    });
                }catch (Exception e )
                {
                    e.getMessage();
                }
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
