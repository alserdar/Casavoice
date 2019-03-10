package alserdar.casavoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.animate.UpAndDown;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadBaseCode;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.UserModel;

public class UserProfileInformation extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference doc;
    String hisOriginalName , hisOriginalUID ;
    String myOriginalName ;
    TextView userNameProfileInfo , idProfileInfo , levelProfileInfo , userPRofileRoomId  ,
    genderProfileInfo , homerProfileInfo  , ageProfileInfo, carProfileInfo , petProfileInfo;
    Button follow , chat , block , unfollow , userProfileHasRoom;
    ImageView proPProfileInfo , petPic , carPic , homePic;
    TextView txtFollow , txtBlock , txtChat ;
    LinearLayout drawTheVIP ;
    ImageView continerVIP ;
    private String theUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_information);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

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

        userNameProfileInfo = findViewById(R.id.usernameProfileInfo);
        idProfileInfo = findViewById(R.id.idUserProfileInfo);
        levelProfileInfo = findViewById(R.id.levelUserProfileInfo);
        genderProfileInfo = findViewById(R.id.genderUserProfileInfo);
        homerProfileInfo = findViewById(R.id.homeUserProfileInfo);
        ageProfileInfo = findViewById(R.id.ageUserProfileInfo);
        carProfileInfo = findViewById(R.id.carUserProfileInfo);
        petProfileInfo = findViewById(R.id.petUserProfileInfo);
        drawTheVIP = findViewById(R.id.drawHisVIPHerePlease);

        txtFollow = findViewById(R.id.followTextView);
        txtChat = findViewById(R.id.messageTextView);
        txtBlock = findViewById(R.id.blockTextView);

        userPRofileRoomId = findViewById(R.id.userProfileRoomId);

        follow = findViewById(R.id.followUserProfileInfo);
        unfollow = findViewById(R.id.unfollowUserProfileInfo);
        chat = findViewById(R.id.privateChatUserProfileInfo);
        block = findViewById(R.id.blockUserProfileInfo);
        proPProfileInfo = findViewById(R.id.profilPictureProfileInfo);
        carPic = findViewById(R.id.carPicture);
        petPic = findViewById(R.id.petPicture);
        homePic = findViewById(R.id.homePicture);
        continerVIP = findViewById(R.id.containHisVip);
        userProfileHasRoom = findViewById(R.id.userProfileHasRoomOrNo);


        myOriginalName = DetectUserInfo.faceBookUser(getBaseContext());
        theUID = DetectUserInfo.theUID(getBaseContext());
        hisOriginalName = this.getIntent().getExtras().getString("hisOriginalName");
        hisOriginalUID = this.getIntent().getExtras().getString("hisOriginalUID");


        LoadBaseCode.readSingleUserCustomObject(getBaseContext() , theUID , hisOriginalUID , userNameProfileInfo , idProfileInfo ,
                levelProfileInfo , genderProfileInfo , ageProfileInfo , homerProfileInfo ,
                carProfileInfo , petProfileInfo , follow , unfollow , block , txtFollow , chat , txtBlock , carPic ,petPic );

        whatIsHisVIP();
        LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext() , hisOriginalUID , proPProfileInfo);

        LoadBaseCode.hasRoom(hisOriginalUID , userProfileHasRoom , userPRofileRoomId);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                LoadBaseCode.followFun(getBaseContext() , theUID , hisOriginalUID , hisOriginalName ,
                        myOriginalName , userNameProfileInfo.getText().toString() , R.string.followed ,
                        follow , unfollow , txtFollow , R.string.unfollow , R.string.followed_you);
            }
        });

        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                unfollow.setVisibility(View.GONE);
                follow.setVisibility(View.VISIBLE);

                AlertDialog alertDialog = new AlertDialog.Builder(UserProfileInformation.this).create();
                alertDialog.setTitle(getString(R.string.remove));
                alertDialog.setMessage(getString(R.string.are_you_sure));
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try{
                                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                            Query applesQuery = ref.child(theUID + " FollowingList")
                                                    .orderByChild("theFollowingDudesUID").equalTo(hisOriginalUID);

                                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists())
                                                    {
                                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                            appleSnapshot.getRef().removeValue();
                                                            new OurToast().myToast(getBaseContext() , getString(R.string.removed));
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });
                                        }catch (Exception e)
                                        {
                                            e.getMessage();
                                        }
                                    }
                                }).start();


                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                LoadBaseCode.letUsChat(getBaseContext() , hisOriginalUID , theUID , myOriginalName);
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                LoadBaseCode.blockFun(getBaseContext() , hisOriginalUID , theUID , hisOriginalName , myOriginalName ,
                        userNameProfileInfo.getText().toString() , follow , chat , txtBlock);

            }
        });

        userProfileHasRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                Intent i = new Intent(getBaseContext() , SearchActivity.class);
                i.putExtra("roomIdFromUserProfileInformation" , userPRofileRoomId.getText().toString());
                startActivity(i);
                finish();
            }
        });
    }

    private void whatIsHisVIP() {

        db = FirebaseFirestore.getInstance();
        doc = db.collection("UserInformation").document(hisOriginalName);
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
                            continerVIP.setImageBitmap(diamond);
                            break;
                        case "RoyalVIP":
                            showMeTheVipLogo();
                            Bitmap royal = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_platinum);
                            continerVIP.setImageBitmap(royal);
                            break;
                        case "GoldenVIP":
                            showMeTheVipLogo();
                            Bitmap gold = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_golden);
                            continerVIP.setImageBitmap(gold);
                            break;
                        case "SilverVIP":
                            showMeTheVipLogo();
                            Bitmap silver = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_silver);
                            continerVIP.setImageBitmap(silver);
                            break;
                        case "RegularVIP":
                            showMeTheVipLogo();
                            Bitmap pronz = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_pronze);
                            continerVIP.setImageBitmap(pronz);
                            break;
                        case "no":
                            drawTheVIP.setVisibility(View.GONE);
                            break;
                    }
                }else
                {

                }
            }
        });
    }

    private void showMeTheVipLogo() {

        if (drawTheVIP.getVisibility() == View.GONE) {
            new UpAndDown().showTheLay(drawTheVIP);
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

