package alserdar.casavoice.fab_setting_classes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.R;
import alserdar.casavoice.animate.UpAndDown;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadBaseCode;
import alserdar.casavoice.load_stuff.LoadUICode;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.UserModel;

public class YourProfile extends AppCompatActivity {


    String theUID;
    TextView userName , id , level , gender , house , BirthDate , car , pet ;
    TextView following , followers ;
    TextView myBalance ;
    ImageView profilePicture , carPic , petPic , housePic;
    Button upgradeProfile , blockList;
    LinearLayout drawTheVIP , letTheVIPShow ;
    ImageView contaierMyVIP ;
    ProgressBar progressLevel ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_profile);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final SoundPool sp;
        final int[] soundIds;
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

        userName = findViewById(R.id.userName);
        id = findViewById(R.id.the_id);
        level = findViewById(R.id.the_level);
        progressLevel = findViewById(R.id.the_level_progress);
        gender = findViewById(R.id.gender);
        house = findViewById(R.id.house);
        BirthDate = findViewById(R.id.age);
        car = findViewById(R.id.car);
        pet = findViewById(R.id.pet);
        drawTheVIP = findViewById(R.id.drawMyVIPHerePlease);
        letTheVIPShow = findViewById(R.id.letTheVIPGo);
        contaierMyVIP = findViewById(R.id.containMyVip);

        following = findViewById(R.id.myFollowing);
        followers = findViewById(R.id.myFollowers);

        myBalance = findViewById(R.id.myBalanceFromMyProfile);

        LoadBaseCode.readBalance(getBaseContext() , theUID , myBalance);

        blockList= findViewById(R.id.myBlockListUserFromSettings);
        blockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            Intent i = new Intent(getBaseContext() , MyBlockList.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });


        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            Intent i = new Intent(getBaseContext() , FollowingList.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            Intent i = new Intent(getBaseContext() , FollowersList.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

        whatIsYourVIP();

        profilePicture = findViewById(R.id.userProfilePicture);
        housePic = findViewById(R.id.housePic);
        petPic = findViewById(R.id.petPic);
        carPic = findViewById(R.id.carPic);

        LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext() , theUID, profilePicture);

        upgradeProfile = findViewById(R.id.upgradeYourProfile);

        upgradeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext(), UpdateYourProfile.class);
                            i.putExtra("userName" , userName.getText().toString());
                            i.putExtra("gender" , gender.getText().toString());
                            i.putExtra("birthDate" , BirthDate.getText().toString());
                            i.putExtra("house" , house.getText().toString());
                            i.putExtra("pet" , pet.getText().toString());
                            i.putExtra("car" , car.getText().toString());

                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();
            }
        });

      //  redSingleUser();

        readSingleUserCustomObject();
    }

    private void readSingleUserCustomObject() {
        doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    userName.setText(user.getUserName());
                    id.setText("ID:"+ user.getLittleID());
                    level.setText("lv:" + String.valueOf(user.getLevel()));
                    gender.setText(user.getGender());
                    BirthDate.setText(user.getBirthDate());
                    house.setText(user.getHouse());
                    car.setText(user.getCar());
                    pet.setText(user.getPet());
                    progressLevel.setProgress(user.getTheProgressOfLevelInPar());

                    LoadUICode.getCarPic(getBaseContext() , user.getCar() , carPic);
                    LoadUICode.getPetPic(getBaseContext() , user.getPet() , petPic);

                }else
                {

                }
            }
        });
    }


    private void whatIsYourVIP() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                String vip = user.getVip();
                                switch (vip) {
                                    case "LegendaryVIP":
                                        showMeTheVipLogo();
                                        Bitmap diamond = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_diamond);
                                        contaierMyVIP.setImageBitmap(diamond);
                                        break;
                                    case "RoyalVIP":
                                        showMeTheVipLogo();
                                        Bitmap platinum = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_platinum);
                                        contaierMyVIP.setImageBitmap(platinum);
                                        break;
                                    case "GoldenVIP":
                                        showMeTheVipLogo();
                                        Bitmap golden = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_golden);
                                        contaierMyVIP.setImageBitmap(golden);
                                        break;
                                    case "SilverVIP":
                                        showMeTheVipLogo();
                                        Bitmap silver = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_silver);
                                        contaierMyVIP.setImageBitmap(silver);
                                        break;
                                    case "RegularVIP":
                                        showMeTheVipLogo();
                                        Bitmap reg = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_pronze);
                                        contaierMyVIP.setImageBitmap(reg);
                                        break;
                                    case "no":
                                        contaierMyVIP.setVisibility(View.GONE);
                                        break;
                                }
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


    private void showMeTheVipLogo() {

        if (drawTheVIP.getVisibility() == View.GONE) {
            new UpAndDown().showTheLay(drawTheVIP);
            Thread timer = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(4000);
                    } catch (InterruptedException e) {
                        e.getMessage();
                    } finally {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new UpAndDown().hideTheLay(drawTheVIP);
                                Thread timer = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.getMessage();
                                        } finally {

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    drawTheVIP.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                    }
                                };
                                timer.start();
                            }
                        });
                    }

                }
            };
            timer.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
