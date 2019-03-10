package alserdar.casavoice;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.devsmart.android.ui.HorizontalListView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import alserdar.casavoice.animate.UpAndDown;
import alserdar.casavoice.home_watcher_stuff.HomeWatcher;
import alserdar.casavoice.home_watcher_stuff.OnHomePressedListener;
import alserdar.casavoice.models.BlockModelForRooms;
import alserdar.casavoice.models.BroadCastModel;
import alserdar.casavoice.models.ChatModel;
import alserdar.casavoice.models.FaveRoomsModel;
import alserdar.casavoice.models.NotificationsModel;
import alserdar.casavoice.models.OnlineModel;
import alserdar.casavoice.models.UserModel;
import alserdar.casavoice.room_settings_functions.AdminsUsers;
import alserdar.casavoice.room_settings_functions.BlockedForRooms;
import alserdar.casavoice.room_settings_functions.EditMyRoom;
import alserdar.casavoice.room_settings_functions.OnlineUsers;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class TheDoubleDevilLiveRoomWithAllTrash extends AppCompatActivity implements View.OnClickListener {

    LinearLayout ferrari , re7a , negma , bugatti , farawla , coctail
            , camaro , sabeka , gawhara , jeep , bosa , warda , yacht , tag , iphone , torta
            , chocolata;

    boolean okFerrari , okRe7a , okNegma , okBugatti , okFarawla , okCoctail
            , okCamaro , okSabeka , okGawhara , okJeep , okBosa , okWarda , okYacht , okTag , okIphone , okTorta
            , okChocolata;

    LinearLayout bokeh , barChocolate , bike , confetti , cupcake , diamond , engagmentRing
            , heartBallon , lipstick , goldenRing , rose , khara , threeBallons , teddy
            , starTail , scooter , coffee ;

    TextView onlineGuysInRichList , onlineGuysWithRealNameInRichList , roomBroadcastGifts ;

    Button sendMessageButt  , settingInTheRoom , showGiftsBoxes ,
            simpleGiftButt  , richGiftButt , recordMessageButt , stopRecord , cancelRecord , sendRecord , recodeMode , typeMode;
    EditText typeMessageHere ;
    TextView roomNameInRoom , roomIdInRoom ;

    LinearLayout settingLayout , giftLayout , richGiftLayout , simpleGiftLayout;
    Button admins , online , editRoom , blockList , report , exit ;

    ImageView profilePictureRoom  , whatIsTheFuckinGift;

    private String roomOwner , facebookUserName , myNickName ;
    private int balance ;
    private int recBalance ;

    Button faveThisRoom ;
    SwitchCompat switchToRecordMode ;
    ProgressBar progressBarForRecord ;

    String AudioSavePathInFireStorage = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 73;
    MediaPlayer mediaPlayer ;

    private String getTheRandom ;
    ImageView firstMic , secondMic ,  thirdMic , fourthMic , fivthMic ;

    LinearLayout introLay ;
    ImageView intoImage ;
    TextView introNickName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_devel_live_room);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();



        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                RemoveOnlineGuys();
            }
            @Override
            public void onHomeLongPressed() {
            }
        });
        mHomeWatcher.startWatch();

        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        facebookUserName = getInfo.getString("FacebookUser", "FacebookUser");
        myNickName = getInfo.getString("myUserName", "myUserName");
        roomOwner = this.getIntent().getExtras().getString("ownerRoom");

        switchToRecordMode = findViewById(R.id.switchToRecord);
        progressBarForRecord = findViewById(R.id.progressRecord);

        selectRichGifts();
        selectSimppleGifts();

        releaseTheRoomInfo();
        countTheUsersForSimpleListPlease();
        countTheUsersForRichListPlease();
        userBlocked();

        showBoxesLayoutGo();
        settingLayoutGo();
        simpleGiftGo();
        richGiftGo();

        roomOptions();
        selectGiftFromTheRichOk();

        broadcastGiftsForAll();

        theUsersOnlineInMotherFuckerPics();

        sendMessageButt = findViewById(R.id.sendMessage);
        typeMessageHere = findViewById(R.id.typeMessage);
        typeMode = findViewById(R.id.typeMode);

        introLay = findViewById(R.id.introVipLay);
        intoImage = findViewById(R.id.introVipImage);
        introNickName = findViewById(R.id.theVipNickName);

        faveThisRoom = findViewById(R.id.faveTheRoomButton);
        profilePictureRoom = findViewById(R.id.profilePictureInsideRoom);

        IsThisRoomInFaveOrNot();
        showTheRoomPic();

        micShows();

        final LinearLayout recordModeLayout = findViewById(R.id.recordModelLayout);
        final LinearLayout typeModeLayout = findViewById(R.id.typeModeLayout);


        switchToRecordMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b)
                {

                    recordModeLayout.setVisibility(View.VISIBLE);
                    typeModeLayout.setVisibility(View.GONE);
                }else
                {

                    recordModeLayout.setVisibility(View.GONE);
                    typeModeLayout.setVisibility(View.VISIBLE);
                }
            }
        });


        faveThisRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                faveThisRoom.setBackgroundResource(R.mipmap.heart);
                faveThisRoom.setEnabled(false);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(facebookUserName + " FaveRooms").push();
                myRef.setValue(new FaveRoomsModel(roomOwner , "roomOwnerID" , roomIdInRoom.getText().toString() , roomNameInRoom.getText().toString()));

            }
        });


       // final ProgressBar recBar = (ProgressBar)findViewById(R.id.progressRecord);
        recordMessageButt = findViewById(R.id.record);
      //  stopRecord = findViewById(R.id.stopRecord);
     //   cancelRecord = findViewById(R.id.cancelRecord);
        sendRecord = findViewById(R.id.sendRecord);
        recodeMode = findViewById(R.id.recordMode);

        recodeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recordModeLayout.setVisibility(View.VISIBLE);
                typeModeLayout.setVisibility(View.GONE);


            }
        });


        typeMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recordModeLayout.setVisibility(View.GONE);
                typeModeLayout.setVisibility(View.VISIBLE);

            }
        });


        random = new Random();

        recordMessageButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkAudioPermission())
                {

                    AudioSavePathInFireStorage =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    Toast.makeText(TheDoubleDevilLiveRoomWithAllTrash.this, "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

                ValueAnimator animator = ValueAnimator.ofInt(0, progressBarForRecord.getMax());
                animator.setDuration(10000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation){
                        progressBarForRecord.setProgress((Integer)animation.getAnimatedValue());

                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);

                        if (mediaRecorder == null)
                        {

                        }else
                        {
                            mediaRecorder.stop();
                        }

                        InputStream stream = null;
                        try {
                            stream = new FileInputStream(new File(AudioSavePathInFireStorage));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                        getTheRandom = CreateRandomAudioFileName(9);

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference mountainsRef = storageRef.child("voice");
                        StorageReference mountains = mountainsRef.child(facebookUserName);
                        StorageReference mount = mountains.child(getTheRandom);

                        StorageMetadata metadata = new StorageMetadata.Builder()
                                .setContentType("audio/mpeg")
                                .build();

                        assert stream != null;
                        UploadTask uploadTask = mount.putStream(stream , metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads

                                new OurToast().myToast(getBaseContext() , "Failed <3");
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                new OurToast().myToast(getBaseContext() , "Uploaded <3");
                                sendRecord.setEnabled(true);

                            }
                        });
                    }
                });
                animator.start();
            }
        });



         sendRecord.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

              //    send the saved
                 FirebaseDatabase database = FirebaseDatabase.getInstance();
                 DatabaseReference myRef = database.getReference(roomOwner + " Chat").push();
                 myRef.setValue(new ChatModel(myNickName , "","VoiceNotes" , getTheRandom , "",
                         facebookUserName , android.R.color.transparent));
                 onResume();



             }
         });

        displayChatMessage();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (typeMessageHere.getText().toString().equals(""))
                {
                    sendMessageButt.setEnabled(false);

                }else
                {
                    sendMessageButt.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        typeMessageHere.addTextChangedListener(watcher);

        sendMessageButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        String theTyperNickName = user.getUserName();
                        String theTyperVip = user.getVip();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(roomOwner + " Chat").push();

                        myRef.setValue(new ChatModel(typeMessageHere.getText().toString() , facebookUserName , "" , "",
                                "" , "" , android.R.color.transparent));
                        typeMessageHere.setText("");
                        onResume();
                    }
                });


            }
        });

    }

    private void micShows() {

        firstMic = findViewById(R.id.firstMic);
        secondMic = findViewById(R.id.secondMic);
        thirdMic = findViewById(R.id.thirdMic);
        fourthMic = findViewById(R.id.fourthMic);
        fivthMic = findViewById(R.id.fivthMic);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    final UserModel user  = documentSnapshot.toObject(UserModel.class);

                    firstMic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+ user.getUserName());
                            Glide.with(getBaseContext())
                                    .using(new FirebaseImageLoader())
                                    .load(storageReference)
                                    .asBitmap()
                                    .placeholder(R.mipmap.my_icon)
                                    .error(R.mipmap.my_icon)
                                    .dontAnimate()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(new BitmapImageViewTarget(firstMic) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getBaseContext().getResources(),
                                                    Bitmap.createScaledBitmap(resource, 100, 100, false));
                                            drawable.setCircular(true);
                                            firstMic.setImageDrawable(drawable);
                                        }
                                    });



                            //record
                            if(checkAudioPermission())
                            {

                                AudioSavePathInFireStorage =
                                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                                CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                                MediaRecorderReady();

                                try {
                                    mediaRecorder.prepare();
                                    mediaRecorder.start();
                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                                Toast.makeText(TheDoubleDevilLiveRoomWithAllTrash.this, "Recording started",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                requestPermission();
                            }

                            ValueAnimator animator = ValueAnimator.ofInt(0, progressBarForRecord.getMax());
                            animator.setDuration(10000);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation){
                                    progressBarForRecord.setProgress((Integer)animation.getAnimatedValue());

                                }
                            });
                            animator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation)
                                {
                                    super.onAnimationEnd(animation);

                                    if (mediaRecorder == null)
                                    {

                                    }else
                                    {
                                        mediaRecorder.stop();
                                    }

                                    InputStream stream = null;
                                    try {
                                        stream = new FileInputStream(new File(AudioSavePathInFireStorage));
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }

                                    getTheRandom = CreateRandomAudioFileName(9);

                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReference();
                                    StorageReference mountainsRef = storageRef.child("voice");
                                    StorageReference mountains = mountainsRef.child(user.getUserName());
                                    StorageReference mount = mountains.child(getTheRandom);

                                    StorageMetadata metadata = new StorageMetadata.Builder()
                                            .setContentType("audio/mpeg")
                                            .build();

                                    assert stream != null;
                                    UploadTask uploadTask = mount.putStream(stream , metadata);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle unsuccessful uploads

                                            new OurToast().myToast(getBaseContext() , "Failed <3");
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                            new OurToast().myToast(getBaseContext() , "Uploaded <3");
                                            sendRecord.setEnabled(true);


                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myRef = database.getReference(roomOwner + " Voice").push();
                                            myRef.setValue(new VoiceModel(getTheRandom));

                                            new OurToast().myToast(getBaseContext() , getTheRandom);



                                            new Timer().schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    // this code will be executed after 11 seconds
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                                            Query lastQuery = databaseReference.child(roomOwner + " Voice").orderByKey().limitToLast(1);
                                                            lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    for (DataSnapshot ds : dataSnapshot.getChildren())
                                                                    {
                                                                        VoiceModel model = ds.getValue(VoiceModel.class);
                                                                        FirebaseStorage storage = FirebaseStorage.getInstance();
                                                                        StorageReference storageRef = storage.getReference();
                                                                        StorageReference forestRef = storageRef.child("voice");
                                                                        StorageReference forest = forestRef.child(user.getUserName());
                                                                        StorageReference fores = forest.child(model.getVoice());

                                                                        new OurToast().myToast(getBaseContext() , model.getVoice());
                                                                        fores.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                            @Override
                                                                            public void onSuccess(Uri uri) {
                                                                                // Got the download URL for 'users/me/profile.png'
                                                                                mediaPlayer = new MediaPlayer();
                                                                                try {
                                                                                    mediaPlayer.setDataSource(getBaseContext() , uri);
                                                                                    mediaPlayer.prepare();
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                mediaPlayer.start();
                                                                                new OurToast().myToast(getBaseContext() , "Listen");
                                                                                //

                                                                                if(mediaPlayer != null){
                                                                                    mediaPlayer.stop();
                                                                                    mediaPlayer.release();
                                                                                }
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception exception) {
                                                                                // Handle any errors
                                                                            }
                                                                        });
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {
                                                                    //Handle possible errors.
                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            }, 11000);
                                        }
                                    });
                                }
                            });
                            animator.start();
                        }
                    });
                }else
                {

                }
            }
        });

    }


    private void showTheRoomPic() {


        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("roomImages/"+ roomOwner);
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .asBitmap()
                .placeholder(R.mipmap.my_icon)
                .error(R.mipmap.my_icon)
                .dontAnimate()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(new BitmapImageViewTarget(profilePictureRoom) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getBaseContext().getResources(),
                                Bitmap.createScaledBitmap(resource, 150, 150, false));
                        drawable.setCircular(true);
                        profilePictureRoom.setImageDrawable(drawable);
                    }
                });
    }

    private void IsThisRoomInFaveOrNot() {

        DatabaseReference faveOrNot = FirebaseDatabase.getInstance()
                .getReference(facebookUserName + " FaveRooms");
        faveOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    if (data.child("faveRoomOwner").getValue().equals(roomOwner))
                    {
                        faveThisRoom.setEnabled(false);
                        faveThisRoom.setBackgroundResource(R.mipmap.heart);
                    }else
                    {
                        faveThisRoom.setEnabled(true);
                        faveThisRoom.setBackgroundResource(R.mipmap.white_heart);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void releaseTheRoomInfo() {

        roomNameInRoom = findViewById(R.id.roomNameInsideRoom);
        roomIdInRoom = findViewById(R.id.roomIdInsideRoom);

        String id = this.getIntent().getExtras().getString("roomIdInTheRoom");
        String roomName = this.getIntent().getExtras().getString("roomNameInTheRoom");

        roomNameInRoom.setText(roomName);
        roomIdInRoom.setText(id);
    }


    //count the users and put them in the rich list
    private void countTheUsersForRichListPlease() {

        ListView listUsers = findViewById(R.id.list_users_for_rich_gifts);
        FirebaseListAdapter<OnlineModel> adapter =
                new FirebaseListAdapter<OnlineModel>(this, OnlineModel.class, R.layout.list_online_for_gifts,
                        FirebaseDatabase.getInstance().getReference(roomOwner + " Online")) {
                    @Override
                    protected void populateView(View v, final OnlineModel model, final int position) {

                        onlineGuysInRichList = v.findViewById(R.id.onlineGuysInRichList);
                        onlineGuysWithRealNameInRichList = v.findViewById(R.id.onlineGuysRealUserNameInRichList);
                        FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
                        DocumentReference doc = db.collection("UserInformation").document(roomOwner);
                        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists())
                                {
                                    UserModel user = documentSnapshot.toObject(UserModel.class);
                                    String nickName = user.getUserName();
                                    String realName = user.getOriginalName();
                                    onlineGuysInRichList.setText(nickName);
                                    onlineGuysWithRealNameInRichList.setText(realName);
                                }else
                                {

                                }
                            }
                        });


                    }
                };

        listUsers.setAdapter(adapter);
    }

    //count the users and put them in the simple list
    private void countTheUsersForSimpleListPlease() {

        ListView listUsers = findViewById(R.id.list_users_for_simple_gifts);
        FirebaseListAdapter<OnlineModel> adapter =
                new FirebaseListAdapter<OnlineModel>(this, OnlineModel.class, R.layout.list_online_for_gifts,
                        FirebaseDatabase.getInstance().getReference(roomOwner + " Online")) {
                    @Override
                    protected void populateView(View v, final OnlineModel model, final int position) {
                        onlineGuysInRichList = v.findViewById(R.id.onlineGuysInRichList);
                        onlineGuysInRichList.setText(model.getOnlineDude());
                    }
                };

        listUsers.setAdapter(adapter);
    }

    //simple gifts
    private void simpleGiftGo() {

        simpleGiftButt = findViewById(R.id.simple_gift_box_button);
        simpleGiftLayout = findViewById(R.id.simpleGiftLayout);

        simpleGiftButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (simpleGiftLayout.getVisibility() == View.GONE)
                {
                    if (richGiftLayout.getVisibility() == View.VISIBLE)
                    {
                        richGiftLayout.setVisibility(View.GONE);
                        new UpAndDown().showTheLay(simpleGiftLayout);
                    }else
                    {
                        new UpAndDown().showTheLay(simpleGiftLayout);
                    }


                }else if (simpleGiftLayout.getVisibility() == View.VISIBLE)
                {
                    new UpAndDown().hideTheLay(simpleGiftLayout);

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
                                        simpleGiftLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    };
                    timer.start();
                }
            }
        });
    }

    //rich gifts
    private void richGiftGo() {
        richGiftButt = findViewById(R.id.rich_gift_box_button);
        richGiftLayout = findViewById(R.id.richGiftLayout);

        richGiftButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                senderBalance();
                if (richGiftLayout.getVisibility() == View.GONE)
                {
                    if (simpleGiftLayout.getVisibility() == View.VISIBLE)
                    {
                        simpleGiftLayout.setVisibility(View.GONE);
                        new UpAndDown().showTheLay(richGiftLayout);
                    }else
                    {
                        new UpAndDown().showTheLay(richGiftLayout);
                    }

                }else if (richGiftLayout.getVisibility() == View.VISIBLE)
                {
                    new UpAndDown().hideTheLay(richGiftLayout);
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
                                        richGiftLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    };
                    timer.start();
                }
            }
        });
    }


    private void roomOptions()
    {
        admins = findViewById(R.id.roomAdmins);
        online = findViewById(R.id.roomOnline);
        editRoom = findViewById(R.id.editRoom);
        blockList = findViewById(R.id.blockListButton);
        report = findViewById(R.id.roomReport);
        exit = findViewById(R.id.roomExit);


        //admins 7arakat
        admins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i= new Intent(TheDoubleDevilLiveRoomWithAllTrash.this , AdminsUsers.class);
                i.putExtra("roomOwner" , roomOwner);
                startActivity(i);

            }
        });

        // online 7arakat
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i= new Intent(TheDoubleDevilLiveRoomWithAllTrash.this , OnlineUsers.class);
                i.putExtra( "roomOwner" , roomOwner);
                startActivity(i);
            }
        });

        editRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (facebookUserName.equals(roomOwner))
                {
                    Intent i = new Intent(getBaseContext() , EditMyRoom.class);
                    i.putExtra("roomName" , roomNameInRoom.getText().toString());
                    i.putExtra("roomId" , roomIdInRoom.getText().toString());
                    i.putExtra("roomOwner" , roomOwner);
                    startActivity(i);
                    finish();
                }else
                {
                    new OurToast().myToast(getBaseContext() , "Owner Only can access");
                }

            }
        });

        blockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i= new Intent(TheDoubleDevilLiveRoomWithAllTrash.this , BlockedForRooms.class);
                i.putExtra( "roomOwner" , roomOwner);
                startActivity(i);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RemoveOnlineGuys();
                Intent i= new Intent(TheDoubleDevilLiveRoomWithAllTrash.this , Home.class);
                startActivity(i);
                finish();

            }
        });
    }



    //setting in the room
    private void settingLayoutGo()
    {
        settingInTheRoom = findViewById(R.id.settingInsideRoom);
        settingLayout = findViewById(R.id.settingRoomLayout);

        settingInTheRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (settingLayout.getVisibility() == View.GONE)
                {
                    if (simpleGiftLayout.getVisibility() == View.VISIBLE)
                    {
                        simpleGiftLayout.setVisibility(View.GONE);
                        new UpAndDown().showTheLay(settingLayout);
                    }else if (richGiftLayout.getVisibility() == View.VISIBLE)
                    {
                        richGiftLayout.setVisibility(View.GONE);
                        new UpAndDown().showTheLay(settingLayout);
                    }else
                    {
                        new UpAndDown().showTheLay(settingLayout);
                    }

                }else if (settingLayout.getVisibility() == View.VISIBLE)
                {
                    new UpAndDown().hideTheLay(settingLayout);
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
                                        settingLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    };
                    timer.start();
                }
            }
        });
    }


    private void showBoxesLayoutGo() {
        showGiftsBoxes = findViewById(R.id.showUpGifts);
        giftLayout = findViewById(R.id.giftsLayout);

        showGiftsBoxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                senderBalance();
                if (richGiftLayout.getVisibility() == View.GONE)
                {
                    if (simpleGiftLayout.getVisibility() == View.VISIBLE)
                    {
                        simpleGiftLayout.setVisibility(View.GONE);
                        new UpAndDown().showTheLay(richGiftLayout);
                    }else
                    {
                        new UpAndDown().showTheLay(richGiftLayout);
                    }

                }else if (richGiftLayout.getVisibility() == View.VISIBLE)
                {
                    new UpAndDown().hideTheLay(richGiftLayout);
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
                                        richGiftLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    };
                    timer.start();
                }

                /*
                this is an old code ... it show 2 boxes rich and simple , and you choose from them .

if (giftLayout.getVisibility() == View.GONE)
                {
                   new UpAndDown().showTheLay(giftLayout);
                }else if (giftLayout.getVisibility() == View.VISIBLE)
                {
                    new UpAndDown().hideTheLay(giftLayout);

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
                                        giftLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    };
                    timer.start();
                }

                 */
            }
        });

    }

    private void displayChatMessage() {

        final ListView listOfMessage = findViewById(R.id.list_of_messages);
        FirebaseListAdapter<ChatModel> adapter =
                new FirebaseListAdapter<ChatModel>(this, ChatModel.class, R.layout.list_chat_message,
                        FirebaseDatabase.getInstance().getReference(roomOwner + " Chat")) {
                    @Override
                    protected void populateView(View v, final ChatModel model, int position) {
                        LinearLayout typePost = v.findViewById(R.id.typePost);
                        LinearLayout audioPost = v.findViewById(R.id.audioPost);
                        final TextView messageText = v.findViewById(R.id.message_text);
                        final TextView messageUser = v.findViewById(R.id.message_user);
                        final ImageView ppForChat = v.findViewById(R.id.profilePicForChat);
                        final ImageView userVIPPicture = v.findViewById(R.id.userVipPic);
                        final Button playMessage = v.findViewById(R.id.play_message_audio);
                        final SeekBar seekBarForPlay = v.findViewById(R.id.seek_bar_message_audio);
                        whatIsTheFuckinGift = v.findViewById(R.id.whatIsTheGift);

                        messageUser.setText(model.getMessageNickName());
                        messageText.setText(model.getMessageText());
                        whatIsTheFuckinGift.setBackgroundResource(model.getGifts());


                        if (model.getTheVip().equals("LegendaryVIP"))
                        {
                            messageUser.setTextColor(Color.RED);
                            Bitmap diamond = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_diamond);
                            userVIPPicture.setImageBitmap(diamond);
                        }else if(model.getTheVip().equals("RoyalVIP"))
                        {

                            messageUser.setTextColor(Color.WHITE);
                            Bitmap royal = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_platinum);
                            userVIPPicture.setImageBitmap(royal);
                        }else if(model.getTheVip().equals("GoldenVIP"))
                        {
                            messageUser.setTextColor(Color.YELLOW);
                            Bitmap golden = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_golden);
                            userVIPPicture.setImageBitmap(golden);

                        }else if(model.getTheVip().equals("SilverVIP"))
                        {
                            messageUser.setTextColor(Color.LTGRAY);
                            Bitmap silver = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_silver);
                            userVIPPicture.setImageBitmap(silver);
                        }else if(model.getTheVip().equals("RegularVIP"))
                        {
                            messageUser.setTextColor(Color.BLACK);
                            Bitmap reg = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_pronze);
                            userVIPPicture.setImageBitmap(reg);
                        }else if (model.getTheVip().equals("no"))
                        {
                            userVIPPicture.setVisibility(View.GONE);
                        }

                        StorageReference storageReference =
                                FirebaseStorage.getInstance().getReference().child("images/" + model.getMessageUser());
                        Glide.with(TheDoubleDevilLiveRoomWithAllTrash.this)
                                .using(new FirebaseImageLoader())
                                .load(storageReference)
                                .asBitmap()
                                .placeholder(R.mipmap.my_icon)
                                .error(R.mipmap.my_icon)
                                .dontAnimate()
                                // .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(new BitmapImageViewTarget(ppForChat) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getBaseContext().getResources(),
                                                Bitmap.createScaledBitmap(resource, 100, 100, false));
                                        drawable.setCircular(true);
                                        ppForChat.setImageDrawable(drawable);
                                    }
                                });

                        messageUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                StringTokenizer st = new StringTokenizer(model.getMessageUser(), " ");
                                final String userName = st.nextToken();
                                //  new OurToast().myToast(getBaseContext() , userName);


                                if (userName.equals(facebookUserName) ||
                                        userName.equals("Casavoice")) {

                                } else {

                                    Intent iProf = new Intent(getBaseContext(), UserProfileInformation.class);
                                    iProf.putExtra("hisOriginalName", userName);
                                    startActivity(iProf);

                                    DatabaseReference amBlocked = FirebaseDatabase.getInstance()
                                            .getReference(facebookUserName + " HisBlockList");
                                    amBlocked.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                if (data.child("blockMe").getValue().equals(userName)) {
                                                    new OurToast().myToast(getBaseContext(), String.format(" %s %s " , "you are blocked from " , userName));

                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }
                        });


                        if (model.getMessageText().trim().equals("VoiceNotes"))
                        {
                            typePost.setVisibility(View.GONE);
                            audioPost.setVisibility(View.VISIBLE);
                        }else
                        {
                            audioPost.setVisibility(View.GONE);
                            typePost.setVisibility(View.VISIBLE);
                        }


                        playMessage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                StorageReference storageRef = storage.getReference();

// Get reference to the file
                                StorageReference forestRef = storageRef.child("voice");
                                StorageReference forest = forestRef.child(model.getMessageUser());
                                StorageReference fores = forest.child(model.getTheFuckinPath());

                                fores.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // Got the download URL for 'users/me/profile.png'
                                        mediaPlayer = new MediaPlayer();
                                        try {
                                            mediaPlayer.setDataSource(getBaseContext() , uri);
                                            mediaPlayer.prepare();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        //
                                        ValueAnimator animator = ValueAnimator.ofInt(0, seekBarForPlay.getMax());
                                        animator.setDuration(10000);
                                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                            @Override
                                            public void onAnimationUpdate(ValueAnimator animation){
                                                seekBarForPlay.setProgress((Integer)animation.getAnimatedValue());

                                                mediaPlayer.start();
                                            }
                                        });
                                        animator.addListener(new AnimatorListenerAdapter() {
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                super.onAnimationEnd(animation);


                                                // stop the media player

                                                if(mediaPlayer != null){
                                                    mediaPlayer.stop();
                                                    mediaPlayer.release();
                                                }
                                            }
                                        });
                                        animator.start();



                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                    }
                                });

                            }
                        });
                    }
                };

        listOfMessage.setAdapter(adapter);

    }


    private void levelUpDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(TheDoubleDevilLiveRoomWithAllTrash.this , R.style.levelUpDialog);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        final View decorView = alert
                .getWindow()
                .getDecorView();
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(decorView,
                PropertyValuesHolder.ofFloat("scaleX", 0.0f, 1.0f),
                PropertyValuesHolder.ofFloat("scaleY", 0.0f, 1.0f),
                PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f));
        scaleDown.setDuration(1000);
        scaleDown.start();
        alert.show();
        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //  finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RemoveOnlineGuys();
    }

    private void userBlocked()
    {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(roomOwner + " BlockedForRooms").orderByChild("blockedForRooms").equalTo(facebookUserName);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef();

                    // Get Post object and use the values to update the UI
                    BlockModelForRooms post = appleSnapshot.getValue(BlockModelForRooms.class);

                    assert post != null;
                    if (post.getBlockedForRooms().equals(facebookUserName))
                    {
                        new OurToast().myToast(getBaseContext() , "You are blocked From this Room");
                        typeMessageHere.setVisibility(View.GONE);
                        RemoveOnlineGuys();
                    }else
                    {

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        userBlocked();
    }

    @Override
    protected void onPause() {
        super.onPause();
        userBlocked();
    }

    private void RemoveOnlineGuys()
    {
        // we will put this code in the Exit button tho .

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(roomOwner + " Online").orderByChild("onlineDude").equalTo(facebookUserName);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void selectRichGifts()
    {
        ferrari = findViewById(R.id.ferrariLay);
        re7a = findViewById(R.id.re7aLay);
        negma = findViewById(R.id.negmaLay);
        bugatti = findViewById(R.id.bugattiLay);
        farawla = findViewById(R.id.farawlaLay);
        coctail= findViewById(R.id.coctailLay);
        camaro = findViewById(R.id.camaroLay);
        sabeka = findViewById(R.id.sabekaLay);
        gawhara = findViewById(R.id.gawharaLay);
        jeep = findViewById(R.id.jeepLay);
        bosa = findViewById(R.id.bosaLay);
        warda = findViewById(R.id.wardaLay);
        yacht = findViewById(R.id.yachLay);
        tag = findViewById(R.id.tagLay);
        iphone = findViewById(R.id.iphoneLay);
        torta= findViewById(R.id.tortaLay);
        chocolata= findViewById(R.id.chocolataLay);


        ferrari.setOnClickListener(this);
        re7a.setOnClickListener(this);
        negma.setOnClickListener(this);
        bugatti.setOnClickListener(this);
        farawla.setOnClickListener(this);
        coctail.setOnClickListener(this);
        camaro.setOnClickListener(this);
        sabeka.setOnClickListener(this);
        gawhara.setOnClickListener(this);
        jeep.setOnClickListener(this);
        bosa.setOnClickListener(this);
        warda.setOnClickListener(this);
        yacht.setOnClickListener(this);
        tag.setOnClickListener(this);
        iphone.setOnClickListener(this);
        torta.setOnClickListener(this);
        chocolata.setOnClickListener(this);

    }


    private void selectSimppleGifts()
    {
        bokeh = findViewById(R.id.bokehLay);
        barChocolate = findViewById(R.id.barChoclateLay);
        bike = findViewById(R.id.bikeLay);
        confetti = findViewById(R.id.confettiLay);
        cupcake = findViewById(R.id.cupcakeLay);
        diamond = findViewById(R.id.diamondLay);
        engagmentRing = findViewById(R.id.engagement_ringLay);
        heartBallon = findViewById(R.id.heartBallonLay);
        lipstick = findViewById(R.id.lipstickLay);
        goldenRing = findViewById(R.id.goldRingLay);
        rose = findViewById(R.id.roseLay);
        khara = findViewById(R.id.kharaLay);
        threeBallons = findViewById(R.id.threeBallonsLay);
        teddy= findViewById(R.id.teddyLay);
        starTail = findViewById(R.id.starTailLay);
        scooter = findViewById(R.id.scotterLay);
        coffee = findViewById(R.id.coffeLay);

        bokeh.setOnClickListener(this);
        barChocolate .setOnClickListener(this);
        bike.setOnClickListener(this);
        confetti.setOnClickListener(this);
        cupcake .setOnClickListener(this);
        diamond .setOnClickListener(this);
        engagmentRing.setOnClickListener(this);
        heartBallon .setOnClickListener(this);
        lipstick .setOnClickListener(this);
        goldenRing .setOnClickListener(this);
        rose .setOnClickListener(this);
        khara .setOnClickListener(this);
        threeBallons.setOnClickListener(this);
        teddy.setOnClickListener(this);
        starTail .setOnClickListener(this);
        scooter .setOnClickListener(this);
        coffee.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId())
                {
                    case R.id.ferrariLay :
                        ferrari.setBackgroundResource(R.drawable.selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);


                        okFerrari = true ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;

                        senderBalance();

                        break;
                    case R.id.re7aLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = true ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;

                        senderBalance();

                        break;

                    case R.id.negmaLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = true ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.bugattiLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = true ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.farawlaLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = true ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.coctailLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = true ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.camaroLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = true ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.sabekaLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = true ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.gawharaLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = true ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.jeepLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = true ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.bosaLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = true ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.wardaLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = true ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.yachLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);


                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = true ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.tagLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = true ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.iphoneLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = true ;
                        okTorta= false ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.tortaLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= true ;
                        okChocolata= false ;
                        senderBalance();
                        break;

                    case R.id.chocolataLay :
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla .setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone .setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.selector);

                        okFerrari = false ;
                        okRe7a = false ;
                        okNegma = false ;
                        okBugatti = false ;
                        okFarawla = false ;
                        okCoctail = false ;
                        okCamaro = false ;
                        okSabeka = false ;
                        okGawhara = false ;
                        okJeep = false ;
                        okBosa = false ;
                        okWarda = false ;
                        okYacht = false ;
                        okTag = false ;
                        okIphone = false ;
                        okTorta= false ;
                        okChocolata= true ;
                        senderBalance();
                        break;


                    //simple gifts : ===========

                    case R.id.bokehLay :
                        bokeh.setBackgroundResource(R.drawable.selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;

                    case R.id.barChoclateLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);

                        senderBalance();
                        break;


                    case R.id.bikeLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();

                        break;


                    case R.id.confettiLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);

                        senderBalance();
                        break;


                    case R.id.cupcakeLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;



                    case R.id.diamondLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;



                    case R.id.engagement_ringLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;


                    case R.id.heartBallonLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;



                    case R.id.lipstickLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;


                    case R.id.goldRingLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;


                    case R.id.roseLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;


                    case R.id.kharaLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;



                    case R.id.threeBallonsLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;


                    case R.id.teddyLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;

                    case R.id.starTailLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;


                    case R.id.scotterLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.selector);
                        coffee.setBackgroundResource(R.drawable.gift_selector);
                        senderBalance();
                        break;


                    case R.id.coffeLay :
                        bokeh.setBackgroundResource(R.drawable.gift_selector);
                        barChocolate.setBackgroundResource(R.drawable.gift_selector);
                        bike.setBackgroundResource(R.drawable.gift_selector);
                        confetti.setBackgroundResource(R.drawable.gift_selector);
                        cupcake.setBackgroundResource(R.drawable.gift_selector);
                        diamond.setBackgroundResource(R.drawable.gift_selector);
                        engagmentRing.setBackgroundResource(R.drawable.gift_selector);
                        heartBallon.setBackgroundResource(R.drawable.gift_selector);
                        lipstick.setBackgroundResource(R.drawable.gift_selector);
                        goldenRing.setBackgroundResource(R.drawable.gift_selector);
                        rose.setBackgroundResource(R.drawable.gift_selector);
                        khara.setBackgroundResource(R.drawable.gift_selector);
                        threeBallons.setBackgroundResource(R.drawable.gift_selector);
                        teddy.setBackgroundResource(R.drawable.gift_selector);
                        starTail.setBackgroundResource(R.drawable.gift_selector);
                        scooter.setBackgroundResource(R.drawable.gift_selector);
                        coffee.setBackgroundResource(R.drawable.selector);
                        senderBalance();
                        break;



                }
            }
        });
    }

    private int senderBalance ()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    balance = user.getBalanceOfCoins();
                }else
                {

                }
            }
        });

        return balance ;
    }



    private void selectGiftFromTheRichOk()
    {

        senderBalance();
        Button okRich = findViewById(R.id.okSelectedWithRichOne);
        okRich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                senderBalance();

                int exact = 0;
                final EditText howMany = findViewById(R.id.howManyRichGifts);
                String value = howMany.getText().toString();
                if (value.equals(""))
                {
                    howMany.setText("1");
                }else
                {
                    exact = Integer.parseInt(value);
                }

                if (okFerrari)
                {
                    if (balance >= (1000* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Ferrari" ,exact , balance , 1000*exact
                                ,onlineGuysInRichList.getText().toString() , onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }else if (okRe7a)
                {
                    if (balance >= (800* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("re7a" , exact , balance , 800*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }else if (okNegma)
                {
                    if (balance >= (60* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Negma" , exact , balance , 60*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }else if (okBugatti)
                {

                    if (balance >= (5000* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Bugatti" , exact , balance , 5000*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }


                }else if (okFarawla)
                {

                    if (balance >= (600* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Farawla" , exact , balance , 600*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }


                }else if (okCoctail)
                {

                    if (balance >= (10* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Coctail" , exact , balance , 10*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }


                }else if (okCamaro)
                {

                    if (balance >= (4000* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Camaro" , exact , balance , 4000*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }


                }else if (okSabeka)
                {

                    if (balance >= (700* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Sabeka" , exact , balance , 700*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }


                }else if (okGawhara)
                {

                    if (balance >= (800* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Gawhara" , exact , balance , 800*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }


                }else if (okJeep)
                {
                    if (balance >= (2000* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Jeep" , exact , balance , 2000*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }else if (okBosa)
                {
                    if (balance >= (100* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Bosa" , exact , balance , 100*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }else if (okWarda)
                {
                    if (balance >= (6* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Warda" , exact ,
                                balance , 6*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }else if (okYacht)
                {

                    if (balance >= (2000* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("yacht" , exact , balance , 2000*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }


                }else if (okTag)
                {
                    if (balance >= (1200* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Tag" , exact , balance , 1200*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }else if (okIphone)
                {
                    if (balance >= (400* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Iphone" , exact , balance , 400*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }


                }else if (okTorta)
                {
                    if (balance >= (40* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Torta" , exact , balance , 40*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }else if (okChocolata)
                {
                    if (balance >= (20* exact) && exact > 0)
                    {
                        dialogForMakeSureRichGift("Chocolata" , exact , balance , 20*exact , onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    }else
                    {
                        new OurToast().myToast(getBaseContext() , "Error Sending Gifts !!");
                    }

                }

            }
        });

    }

    private void dialogForMakeSureRichGift(final String gift , final int exact ,
                                           final int balance , final int cost ,
                                           final String userName , final String originalName)
    {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("your Balance is : " + String.valueOf(balance)
                + '\n' + "gift Cost : " + String.valueOf(cost) + '\n' + "for " + userName)
        .setIcon(R.mipmap.my_icon)
        .setTitle("Send !");
                alertDialogBuilder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference(roomOwner + " Chat").push();

                                FirebaseDatabase myNotifications = FirebaseDatabase.getInstance();
                                DatabaseReference myNoti = myNotifications.getReference(facebookUserName + " Notifications").push();

                                FirebaseDatabase hisNotifications = FirebaseDatabase.getInstance();
                                DatabaseReference hisNoti = hisNotifications.getReference(originalName + " Notifications").push();

                                FirebaseFirestore mydb = FirebaseFirestore.getInstance();
                                DocumentReference mydoc = mydb.collection("UserInformation").document(facebookUserName);
                                FirebaseFirestore hisdb = FirebaseFirestore.getInstance();
                                DocumentReference hisdoc = hisdb.collection("UserInformation").document(originalName);

                                switch (gift)
                                {

                                    case "Ferrari":

                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.ferrari));
                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Ferrari"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Ferrari"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        break;

                                    case "re7a":
                                        myRef.setValue(new ChatModel("Casavoice" , "no" , ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.re7a));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName+ "  X " + exact , "re7a"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "re7a"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        break;
                                    case "Negma":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.ngma));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Negma"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Negma"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;

                                    case "Bugatti":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.bugatti));
                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Bugatti"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Bugatti"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;

                                    case "Farawla":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , ""  , R.mipmap.farawla));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Farawla"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Farawla"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        break;

                                    case "Coctail":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.coctail));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Coctail"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Coctail"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;
                                    case "Camaro":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.camaro));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Camaro"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Camaro"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;

                                    case "Sabeka":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , ""  , R.mipmap.sabeka));
                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Sabeka"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Sabeka"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;

                                    case "Gawhara":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.gawhara));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Gawhara"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Gawhara"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        break;
                                    case "Jeep":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , ""  , R.mipmap.jeep));
                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Jeep"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Jeep"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;

                                    case "Bosa":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.bosa));
                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Bosa"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Bosa"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;
                                    case "Warda":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.warda));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Warda"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Warda"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;
                                    case "yacht":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.yacht));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "yacht"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "yacht"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;

                                    case "Tag":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , ""  , R.mipmap.tagg));
                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Tag"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Tag"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;
                                    case "Iphone":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , ""  , R.mipmap.iphones));
                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Iphone"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Iphone"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;

                                    case "Torta":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.torta));
                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Torta"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Torta"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;
                                    case "Chocolata":
                                        myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                                , myNickName + " Send to " + userName + String.valueOf(" X " + exact)  ,"" , "" , R.mipmap.chocalata));

                                        myNoti.setValue(new NotificationsModel("you send to " + userName + "  X " + exact , "Chocolata"));
                                        hisNoti.setValue(new NotificationsModel(myNickName + " Send you X " + exact , "Chocolata"));


                                        mydoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });

                                        hisdoc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            }
                                        });
                                        break;
                                }

                                final int senderNewBalance = (balance - cost) ;


                                //broadcast need to be public to saw from all the users <3
                                FirebaseDatabase databaseBroadCast = FirebaseDatabase.getInstance();
                                DatabaseReference refBroadcast = databaseBroadCast.getReference("GiftsBroadcast").push();
                                refBroadcast.setValue(new BroadCastModel(myNickName , userName , gift , exact ,roomIdInRoom.getText().toString()));


                                /*
                                 roomBroadcastGifts.setText("        " + facebookUserName + " Send to " + userName +
                                        String.valueOf(" " + exact) + " "+ gift + "        ");

                                 */



                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                DocumentReference doc = db.collection("UserInformation").document(facebookUserName);

                                doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        recNewBalanceAcc(cost , originalName);
                                        howMuchYouSend(cost);
                                        happinessRoom(cost);
                                    }
                                });

                                new UpAndDown().hideTheLay(richGiftLayout);
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
                                                    richGiftLayout.setVisibility(View.GONE);
                                                    senderBalance();
                                                }
                                            });
                                        }
                                    }
                                };
                                timer.start();
                            }
                        });

        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {

        if (giftLayout.getVisibility() == View.VISIBLE)
        {
            new UpAndDown().hideTheLay(giftLayout);
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
                                giftLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            };
            timer.start();
        }else if (settingLayout.getVisibility() == View.VISIBLE)
        {
            new UpAndDown().hideTheLay(settingLayout);
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
                                settingLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            };
            timer.start();
        }else if (simpleGiftLayout.getVisibility() == View.VISIBLE)
        {
            new UpAndDown().hideTheLay(simpleGiftLayout);
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
                                simpleGiftLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            };
            timer.start();
        }else if (richGiftLayout.getVisibility() == View.VISIBLE)
        {
            new UpAndDown().hideTheLay(richGiftLayout);
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
                                richGiftLayout.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            };
            timer.start();
        }

    }


    private void happinessRoom (final int muchSendInTheRoom)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("RoomInformation").document(roomOwner);
        doc.update("happiness" , muchSendInTheRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // nees to reset the happenss every two days <3
            }
        });

    }

    private void howMuchYouSend (final int muchSend)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    final UserModel user = documentSnapshot.toObject(UserModel.class);
                    final int dailySendBalance = user.getDailySendBalance();
                    int weeklySendBalance = user.getWeeklySendBalance();
                    int monthlySendBalance = user.getMonthlySendBalance();
                    final int howMuchLevel = user.getLevel();
                    final int howMuchLevelProgress = user.getTheProgressOfLevel();


                    doc.update("dailySendBalance" , dailySendBalance + muchSend).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (howMuchLevel <= 10) {
                                if (muchSend >= 10000 && dailySendBalance >= 20000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                                    doc.update("level", howMuchLevel + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int min = 5;
                                            int max = 95;
                                            Random r = new Random();
                                            int progressLev = r.nextInt(max - min + 1) + min;
                                            doc.update("theProgressOfLevelInPar", progressLev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }else if (howMuchLevel > 10 && howMuchLevel <= 25)
                            {
                                if (muchSend >= 20000 && dailySendBalance >= 30000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                                    doc.update("level", howMuchLevel + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int min = 5;
                                            int max = 95;
                                            Random r = new Random();
                                            int progressLev = r.nextInt(max - min + 1) + min;
                                            doc.update("theProgressOfLevelInPar", progressLev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }else if (howMuchLevel > 25 && howMuchLevel <=50)
                            {
                                if (muchSend >= 30000 && dailySendBalance >= 50000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                                    doc.update("level", howMuchLevel + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int min = 5;
                                            int max = 95;
                                            Random r = new Random();
                                            int progressLev = r.nextInt(max - min + 1) + min;
                                            doc.update("theProgressOfLevelInPar", progressLev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }else if (howMuchLevel > 50)
                            {
                                if (muchSend >= 50000 && dailySendBalance >= 200000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                                    doc.update("level", howMuchLevel + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int min = 5;
                                            int max = 95;
                                            Random r = new Random();
                                            int progressLev = r.nextInt(max - min + 1) + min;
                                            doc.update("theProgressOfLevelInPar", progressLev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }
                        }
                    });
                    doc.update("weeklySendBalance" , weeklySendBalance + muchSend).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    doc.update("monthlySendBalance" , monthlySendBalance + muchSend).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }else
                {

                }

            }
        });


    }

    private void howMuchYouRec (final int muchRec , final String userName)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(userName);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    final int dailyRecBalance = user.getDailyRecBalance();
                    int weeklyRecBalance = user.getWeeklyRecBalance();
                    int monthlyRecBalance = user.getMonthlyRecBalance();
                    final int howMuchLevel = user.getLevel();
                    final int howMuchLevelProgress = user.getTheProgressOfLevel();



                    doc.update("dailyRecBalance" , dailyRecBalance + muchRec).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            if (howMuchLevel <= 10) {
                                if (muchRec >= 10000 && dailyRecBalance >= 20000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                                    doc.update("level", howMuchLevel + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int min = 5;
                                            int max = 95;
                                            Random r = new Random();
                                            int progressLev = r.nextInt(max - min + 1) + min;
                                            doc.update("theProgressOfLevelInPar", progressLev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }else if (howMuchLevel > 10 && howMuchLevel <= 25)
                            {
                                if (muchRec >= 20000 && dailyRecBalance >= 30000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                                    doc.update("level", howMuchLevel + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int min = 5;
                                            int max = 95;
                                            Random r = new Random();
                                            int progressLev = r.nextInt(max - min + 1) + min;
                                            doc.update("theProgressOfLevelInPar", progressLev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }else if (howMuchLevel > 25 && howMuchLevel <=50)
                            {
                                if (muchRec >= 30000 && dailyRecBalance >= 50000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                                    doc.update("level", howMuchLevel + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int min = 5;
                                            int max = 95;
                                            Random r = new Random();
                                            int progressLev = r.nextInt(max - min + 1) + min;
                                            doc.update("theProgressOfLevelInPar", progressLev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }else if (howMuchLevel > 50)
                            {
                                if (muchRec >= 50000 && dailyRecBalance >= 200000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
                                    doc.update("level", howMuchLevel + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            int min = 5;
                                            int max = 95;
                                            Random r = new Random();
                                            int progressLev = r.nextInt(max - min + 1) + min;
                                            doc.update("theProgressOfLevelInPar", progressLev).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });

                                        }
                                    });
                                }
                            }


                        }
                    });

                    doc.update("weeklyRecBalance" , weeklyRecBalance + muchRec).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

                    doc.update("monthlyRecBalance" , monthlyRecBalance + muchRec).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                }else
                {

                }

            }
        });

    }

    private void recNewBalanceAcc(final int cost, final String userName)
    {


            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference doc = db.collection("UserInformation").document(userName);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists())
                    {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        recBalance = user.getBalanceOfCoins();
                        final int earn = (cost*40/100) ;
                        final int newCost = recBalance + earn;
                        DocumentReference doc = db.collection("UserInformation").document(userName);
                        doc.update("balanceOfCoins" , newCost).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                howMuchYouRec(earn  , userName);
                            }
                        });
                    }else
                    {

                    }
                }
            });

    }

    private void broadcastGiftsForAll()
    {
        roomBroadcastGifts = findViewById(R.id.roomBroadcast);
        FirebaseDatabase base = FirebaseDatabase.getInstance();
        DatabaseReference reference = base.getReference("GiftsBroadcast");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists())
                {
                    BroadCastModel model = dataSnapshot.getValue(BroadCastModel.class);
                    roomBroadcastGifts.setText("        " + model.getSender() + " Send to " + model.getRec() +
                            String.valueOf(" " + model.getExact()) + " "+ model.getGift() + "        " + "room : " + model.getRoomIdForBroadcasting());

                    TranslateAnimation animation = new TranslateAnimation(0,roomBroadcastGifts.getWidth()+150, 0, 0); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
                    animation.setDuration(5000); // animation duration
                    animation.setRepeatCount(2); // animation repeat count
                    roomBroadcastGifts.startAnimation(animation);
                }else
                {

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
         reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    BroadCastModel model = dataSnapshot.getValue(BroadCastModel.class);
                    roomBroadcastGifts.setText("        " + model.getSender() + " Send to " + model.getRec() +
                            String.valueOf(" " + model.getExact()) + " "+ model.getGift() + "        " + "room : " + model.getRoomIdForBroadcasting());

                }else
                {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
         */

    }

    // audio stuff
    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInFireStorage);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(TheDoubleDevilLiveRoomWithAllTrash.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(TheDoubleDevilLiveRoomWithAllTrash.this, "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                    }
                }
                break;
        }
    }

    public boolean checkAudioPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }


    private void theUsersOnlineInMotherFuckerPics()
    {
        HorizontalListView listUsers = findViewById(R.id.hlistview);
        FirebaseListAdapter<OnlineModel> adapter =
                new FirebaseListAdapter<OnlineModel>(this, OnlineModel.class, R.layout.list_online_user_in_bar,
                        FirebaseDatabase.getInstance().getReference(roomOwner + " Online")) {
                    @Override
                    protected void populateView(View v, final OnlineModel model, final int position) {

                        theVIPIntro(model.getOnlineDude());

                        final ImageView userInBar = v.findViewById(R.id.usersOnLineInBar);
                        StorageReference storageReference =
                                FirebaseStorage.getInstance().getReference().child("images/" + model.getOnlineDude());
                        Glide.with(TheDoubleDevilLiveRoomWithAllTrash.this)
                                .using(new FirebaseImageLoader())
                                .load(storageReference)
                                .asBitmap()
                                .placeholder(R.mipmap.my_icon)
                                .error(R.mipmap.my_icon)
                                .dontAnimate()
                                // .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(new BitmapImageViewTarget(userInBar) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getBaseContext().getResources(),
                                                Bitmap.createScaledBitmap(resource, 100, 100, false));
                                        drawable.setCircular(true);
                                        userInBar.setImageDrawable(drawable);
                                    }
                                });
                    }
                };

        listUsers.setAdapter(adapter);
    }

    private void theVIPIntro(String theOnlineDude)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theOnlineDude);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    boolean hasVip = user.isHasVip();
                    if (hasVip)
                    {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(roomOwner + " Chat").push();
                        String vip = user.getVip();
                        String theUser = user.getUserName();


                        switch (vip) {
                            case "LegendaryVIP":

                                myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                        , theUser + " Join The Room"  ,"" , "" , R.mipmap.vip_car));

                                showMeTheVipLogoIntro();
                                introNickName.setText(theUser);
                                Bitmap diamond = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_diamond);
                                intoImage.setImageBitmap(diamond);
                                /*
                                final FireworkView firework = new FireworkView(getBaseContext());
                                final LinearLayout surface = findViewById(R.id.surface);
                                surface.setVisibility(View.VISIBLE);
                                surface.addView(firework);
                                Thread timer = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            sleep(3500);
                                        } catch (InterruptedException e) {
                                            e.getMessage();
                                        } finally {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            surface.setVisibility(View.GONE);
                                                            firework.clearAnimation();
                                                        }
                                                    });
                                                }
                                            }).start();
                                        }
                                    }
                                };
                                timer.start();
                                 */

                                break;
                            case "RoyalVIP":
                                myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                        , theUser + " Join The Room"  ,"" , ""  , R.mipmap.bugatti));
                                showMeTheVipLogoIntro();
                                introNickName.setText(theUser);
                                Bitmap royal = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_platinum);
                                intoImage.setImageBitmap(royal);

                                break;
                            case "GoldenVIP":
                                myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                        , theUser + " Join The Room"  ,"" , ""  ,  R.mipmap.vip_golden));
                                showMeTheVipLogoIntro();
                                introNickName.setText(theUser);
                                Bitmap golden = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_golden);
                                intoImage.setImageBitmap(golden);
                                break;
                            case "SilverVIP":
                                myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                        , theUser + " Join The Room"  ,"" , "" ,  R.mipmap.vip_silver));
                                break;
                            case "RegularVIP":
                                myRef.setValue(new ChatModel("Casavoice" , "no", ""
                                        , theUser + " Join The Room"  ,"" , ""  , R.mipmap.vip_pronze));
                                break;
                            case "no" :
                                break;
                        }
                    }else
                    {

                    }
                }else
                {

                }
            }
        });
    }

    private void theVIP(final String user , final TextView messageUser , final ImageView userVIPPicture)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(user);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                UserModel user = documentSnapshot.toObject(UserModel.class);
                String theVip = user.getVip();
                switch (theVip) {
                    case "LegendaryVIP":
                        messageUser.setTextColor(Color.RED);
                        Bitmap diamond = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_diamond);
                        userVIPPicture.setImageBitmap(diamond);
                        break;
                    case "RoyalVIP":
                        messageUser.setTextColor(Color.WHITE);
                        Bitmap royal = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_platinum);
                        userVIPPicture.setImageBitmap(royal);
                        break;
                    case "GoldenVIP":
                        messageUser.setTextColor(Color.YELLOW);
                        Bitmap golden = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_golden);
                        userVIPPicture.setImageBitmap(golden);
                        break;
                    case "SilverVIP":
                        messageUser.setTextColor(Color.LTGRAY);
                        Bitmap silver = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_silver);
                        userVIPPicture.setImageBitmap(silver);
                        break;
                    case "RegularVIP":
                        messageUser.setTextColor(Color.BLACK);
                        Bitmap reg = BitmapFactory.decodeResource(getResources() , R.mipmap.vip_pronze);
                        userVIPPicture.setImageBitmap(reg);
                        break;
                    case "no" :
                        userVIPPicture.setVisibility(View.GONE);
                        break;
                }
            }
        });

    }

    private void showMeTheVipLogoIntro() {

        if (introLay.getVisibility() == View.GONE) {
            new UpAndDown().introVip(introLay);
            Thread timer = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(6000);
                    } catch (InterruptedException e) {
                        e.getMessage();
                    } finally {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                introLay.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            };
            timer.start();
        }
    }
}