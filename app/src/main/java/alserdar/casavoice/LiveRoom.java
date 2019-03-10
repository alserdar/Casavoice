package alserdar.casavoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.octiplex.android.rtmp.AACAudioFrame;
import com.octiplex.android.rtmp.AACAudioHeader;
import com.octiplex.android.rtmp.H264VideoFrame;
import com.octiplex.android.rtmp.RtmpConnectionListener;
import com.octiplex.android.rtmp.RtmpMuxer;
import com.octiplex.android.rtmp.Time;

import org.jetbrains.annotations.NotNull;
import org.jitsi.meet.sdk.JitsiMeetActivity;

import java.io.IOException;
import java.util.StringTokenizer;

import alserdar.casavoice.animate.UpAndDown;
import alserdar.casavoice.check_internet.NetworkUtil;
import alserdar.casavoice.handle_easy_stuff.RemoveOnlineGuysClass;
import alserdar.casavoice.home_watcher_stuff.HomeWatcher;
import alserdar.casavoice.home_watcher_stuff.OnHomePressedListener;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadBaseCode;
import alserdar.casavoice.load_stuff.LoadBaseCodeForLiveRoom;
import alserdar.casavoice.load_stuff.LoadUIForLiveRoom;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.BlockModelForRooms;
import alserdar.casavoice.models.BroadCastModel;
import alserdar.casavoice.models.ChatModel;
import alserdar.casavoice.models.NotificationsModel;
import alserdar.casavoice.models.OnlineModel;
import alserdar.casavoice.models.UserModel;
import alserdar.casavoice.room_settings_functions.AdminsUsers;
import alserdar.casavoice.room_settings_functions.BlockedForRooms;
import alserdar.casavoice.room_settings_functions.EditMyRoom;
import alserdar.casavoice.room_settings_functions.OnlineUsers;
import alserdar.casavoice.room_settings_functions.ReportRoom;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class LiveRoom extends AppCompatActivity implements View.OnClickListener
      , AudioStreaming2.ScreenRecordListener
        , RtmpConnectionListener
{

    LinearLayout ferrari, re7a, negma, bugatti,
            farawla, coctail, camaro, sabeka,
            gawhara, jeep, bosa, warda, yacht,
            tag, iphone, torta, chocolata, introLay,
            settingLayout, richGiftLayout , clickLay , micLay;
    boolean okFerrari, okRe7a, okNegma, okBugatti,
            okFarawla, okCoctail, okCamaro, okSabeka,
            okGawhara, okJeep, okBosa, okWarda, okYacht, okTag, okIphone, okTorta, okChocolata;
    public static final int RequestPermissionCode = 1;
    private TextView onlineGuysInRichList, onlineGuysWithRealNameInRichList, roomBroadcastGifts,
            roomNameInRoom, roomIdInRoom, introNickName, txtMicOne, txtMicTwo, txtMicThree, txtMicFour, txtMicFive;
    Button sendMessageButt, settingInTheRoom, showGiftsBoxes,
            admins, online, editRoom, blockList, report, exit, faveThisRoom, unFaveThisRoom;
    EditText typeMessageHere;
    ImageView profilePictureRoom, firstMic, secondMic, thirdMic, fourthMic, fivthMic,
            firstMicMuted, secondMicMuted, thirdMicMuted, fourthMicMuted, fivthMicMuted,
            firstMicDisable, secondMicDisable, thirdMicDisable, fourthMicDisable, fivthMicDisable, intoImage;
    private int balance, recBalance;
    private String facebookUserName, facebookID, theUID, myNickName, roomOwner,
            roomInfo, roomOwnerUID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference doc = null;
    private ImageView whatIsTheFuckinGift;
    private FirebaseDatabase base = FirebaseDatabase.getInstance();
    private DatabaseReference reference = null;
    private String streamName = null;
    private String micId = null ;
    private SimpleExoPlayer exo1, exo2, exo3, exo4, exo5;
    private SoundPool sp;
    private int[] soundIds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inhanse_live_room);
        ActionBar actionBar = getSupportActionBar();
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

        facebookUserName = DetectUserInfo.faceBookUser(this);
        facebookID = DetectUserInfo.faceBookId(this);
        myNickName = DetectUserInfo.myUserName(this);
        theUID = DetectUserInfo.theUID(this);
        roomOwner = this.getIntent().getExtras().getString("ownerRoom");
        roomOwnerUID = this.getIntent().getExtras().getString("roomOwnerUID");
        roomInfo = this.getIntent().getExtras().getString("roomInfo");
        reference = base.getReference(roomOwnerUID + " voice");

        try {
            HomeWatcher mHomeWatcher = new HomeWatcher(this);
            mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
                @Override
                public void onHomePressed() {
                    RemoveOnlineGuys();
                    finish();
                }

                @Override
                public void onHomeLongPressed() {
                }
            });
            mHomeWatcher.startWatch();

        } catch (Exception e) {
            e.getMessage();
        }

        if (NetworkUtil.getConnectivityStatusString(getBaseContext()).equals("Internet enabled")) {

        } else {
            Looper.prepare();
            RemoveOnlineGuys();
        }


        preferencesTheId();
        selectRichGifts();
        releaseTheRoomInfo();
        LoadBaseCodeForLiveRoom.userBlocked(roomOwnerUID, theUID, typeMessageHere, getBaseContext() , micLay);
        showBoxesLayoutGo();
        settingLayoutGo();
        roomOptions();
        selectGiftFromTheRichOk();
        LoadBaseCodeForLiveRoom.broadcastGiftsForAll(getBaseContext() , roomBroadcastGifts);
        theUsersOnlineInMotherFuckerPics();
        LoadBaseCodeForLiveRoom.IsThisRoomInFaveOrNot(theUID , roomOwnerUID , faveThisRoom , unFaveThisRoom);
        LoadingPics.loadPicForRooms(getBaseContext().getApplicationContext(), roomOwnerUID, profilePictureRoom);
        countTheUsersForRichListPlease();
        LoadBaseCodeForLiveRoom.theLastOnlineVIPNewOne(roomOwnerUID, introLay, introNickName, intoImage, getBaseContext());


        firstMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                if (checkPermission()) {

                    /*
                     micId = "mic one";
                    txtMicOne.setText(myNickName);
                    initMuxer();
                    streamName = createStreamURI(roomIdInRoom.getText().toString(), micId, roomOwnerUID);
                    LoadBaseCodeForLiveRoom.micsEnabled(getBaseContext() , reference,micId, myNickName, roomIdInRoom.getText().toString(),
                            "pending", facebookUserName, streamName, theUID, firstMicDisable);
                    LoadUIForLiveRoom.micsUI(firstMic, View.GONE, secondMic, false,
                            thirdMic, false, fourthMic, false, fivthMic, false,
                            firstMicDisable, View.VISIBLE);
                     */
                    new OurToast().myToast(getBaseContext() , getString(R.string.UnderOptimization));
                } else {
                    requestPermission();
                }
            }
        });



        firstMicDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                if (txtMicOne.getText().toString().equals(myNickName))
                {
                    stopLiveMic();
                    LoadUIForLiveRoom.off(getBaseContext() , txtMicOne,firstMicDisable , View.GONE , secondMic , true , thirdMic , true
                        , fourthMic , true , fivthMic , true , firstMic , View.VISIBLE);

                }else
                {
                    try
                    {
                        LoadUIForLiveRoom.micButtonsControllersVisibility(firstMicDisable, View.GONE, firstMic, View.GONE, firstMicMuted, View.VISIBLE);
                        exo1.setPlayWhenReady(false);
                    }catch (Exception e)
                    {
                        e.getMessage();
                    }

                }

            }
        });


        firstMicMuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                LoadUIForLiveRoom.removeMuteDetails(txtMicOne, firstMicDisable, firstMicMuted, firstMic);
                exo1.setPlayWhenReady(true);
            }
        });

        // second mic

        secondMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                if (checkPermission()) {

                    /*
                        micId = "mic two";
                    txtMicTwo.setText(myNickName);
                    initMuxer();
                    streamName = createStreamURI(roomIdInRoom.getText().toString(), micId, roomOwnerUID);
                    LoadBaseCodeForLiveRoom.micsEnabled(getBaseContext() , reference , micId, myNickName, roomIdInRoom.getText().toString(),
                            "pending", facebookUserName, streamName, theUID, secondMicDisable);
                    LoadUIForLiveRoom.micsUI(secondMic, View.GONE, firstMic, false,
                            thirdMic, false, fourthMic, false, fivthMic, false,
                            secondMicDisable, View.VISIBLE);

                     */
                    new OurToast().myToast(getBaseContext() , getString(R.string.UnderOptimization));
                } else {
                    requestPermission();
                }
            }
        });

        secondMicDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                if (txtMicTwo.getText().toString().equals(myNickName))
                {
                    stopLiveMic();
                    LoadUIForLiveRoom.off(getBaseContext() , txtMicTwo, secondMicDisable , View.GONE , firstMic , true , thirdMic , true
                            , fourthMic , true , fivthMic , true , secondMic , View.VISIBLE);

                }else
                {
                    try
                    {

                        LoadUIForLiveRoom.micButtonsControllersVisibility(secondMicDisable, View.GONE, secondMicMuted, View.VISIBLE, secondMic, View.GONE);
                        exo2.setPlayWhenReady(false);
                    }catch (Exception e)
                    {
                        e.getMessage();
                    }

                }
            }
        });


        secondMicMuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                LoadUIForLiveRoom.removeMuteDetails(txtMicTwo, secondMicDisable, secondMicMuted, secondMic);
                exo2.setPlayWhenReady(true);
            }
        });

        thirdMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                if (checkPermission()) {

                    new OurToast().myToast(getBaseContext() , getString(R.string.UnderOptimization));
                    /*
                    micId = "mic three";
                    txtMicThree.setText(myNickName);
                    initMuxer();

                    micsUI(thirdMic, View.GONE, firstMic, false,
                            secondMic, false,
                            fourthMic, false, fivthMic, false,
                            thirdMicDisable, View.VISIBLE);

                    streamName = createStreamURI(roomIdInRoom.getText().toString(), micId, roomOwnerUID);


                    micsEnabled("mic three", myNickName, roomIdInRoom.getText().toString(),
                            "pending", facebookUserName, streamName, theUID, thirdMicDisable);

                     */



                } else {
                    requestPermission();
                }


            }
        });

        thirdMicDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                /*
                 micsDisable(thirdMicDisable , View.GONE , secondMic , true , firstMic , true
                        , fourthMic , true , fivthMic , true , thirdMic , View.VISIBLE);

                 */

                LoadUIForLiveRoom.goOffOrMute(getBaseContext() , txtMicThree, myNickName, thirdMicDisable , View.GONE , secondMic , true , firstMic , true
                        , fourthMic , true , fivthMic , true , thirdMic , View.VISIBLE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        exo3.stop();
                        LoadUIForLiveRoom.micButtonsControllersVisibility(thirdMicDisable, View.GONE,
                                thirdMicMuted, View.VISIBLE, thirdMic, View.GONE);
                    }
                });


            }
        });


        thirdMicMuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                LoadUIForLiveRoom.removeMute(getBaseContext() , txtMicThree.getText().toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LoadUIForLiveRoom.removeMuteDetails(txtMicThree, thirdMicDisable, thirdMicMuted, thirdMic );
                        exo3.setPlayWhenReady(true);
                    }
                });
            }
        });

        // fourth mic
        fourthMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);


                if (checkPermission()) {

                    new OurToast().myToast(getBaseContext() , getString(R.string.UnderOptimization));
                    /*
                     micId = "mic four";
                    txtMicFour.setText(myNickName);
                    initMuxer();

                    micsUI(fourthMic, View.GONE, firstMic, false,
                            secondMic, false,
                            thirdMic, false, fivthMic, false,
                            fourthMicDisable, View.VISIBLE);

                    streamName = createStreamURI(roomIdInRoom.getText().toString(), micId, roomOwnerUID);


                    micsEnabled("mic four", myNickName, roomIdInRoom.getText().toString(),
                            "pending", facebookUserName, streamName, theUID, fourthMicDisable);

                     */


                } else {
                    requestPermission();
                }


            }
        });

        fourthMicDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                /*
                  micsDisable(fourthMicDisable , View.GONE , secondMic , true , thirdMic , true
                        , firstMic , true , fivthMic , true , fourthMic , View.VISIBLE);


                 */

                LoadUIForLiveRoom.goOffOrMute(getBaseContext() , txtMicFour, myNickName, fourthMicDisable , View.GONE , secondMic , true , thirdMic , true
                        , firstMic , true , fivthMic , true , fourthMic , View.VISIBLE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exo4.stop();
                        LoadUIForLiveRoom.  micButtonsControllersVisibility(fourthMicDisable, View.GONE, fourthMicMuted, View.VISIBLE
                                , fourthMic, View.GONE);
                    }
                });
            }
        });

        fourthMicMuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadUIForLiveRoom.removeMute(getBaseContext() , txtMicFour.getText().toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LoadUIForLiveRoom. removeMuteDetails(txtMicFour, fourthMicDisable, fourthMicMuted, fourthMic);
                        exo4.setPlayWhenReady(true);
                    }
                });
            }
        });

        // fivth mic

        fivthMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                if (checkPermission()) {


                    new OurToast().myToast(getBaseContext() , getString(R.string.UnderOptimization));
                    /*
                     micId = "mic five";
                    txtMicFive.setText(myNickName);
                    initMuxer();

                    micsUI(fivthMic, View.GONE, firstMic, false,
                            secondMic, false,
                            thirdMic, false, fourthMic, false,
                            fivthMicDisable, View.VISIBLE);

                    streamName = createStreamURI(roomIdInRoom.getText().toString(), micId, roomOwnerUID);


                    micsEnabled("mic five", myNickName, roomIdInRoom.getText().toString(),
                            "pending", facebookUserName, streamName, theUID, fivthMicDisable);

                     */

                } else {
                    requestPermission();
                }


            }
        });

        fivthMicDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                /*
                  micsDisable(fivthMicDisable , View.GONE , secondMic , true , thirdMic , true
                        , fourthMic , true , firstMic , true , fivthMic , View.VISIBLE);
                 */

                LoadUIForLiveRoom.goOffOrMute(getBaseContext() , txtMicFive, myNickName,fivthMicDisable , View.GONE , secondMic , true , thirdMic , true
                        , fourthMic , true , firstMic , true , fivthMic , View.VISIBLE, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        exo5.stop();
                        LoadUIForLiveRoom.micButtonsControllersVisibility(fivthMicDisable, View.GONE,
                                fivthMicMuted, View.VISIBLE, fivthMic, View.GONE);
                    }
                });
            }
        });

        fivthMicMuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadUIForLiveRoom.removeMute(getBaseContext() , txtMicFive.getText().toString(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        LoadUIForLiveRoom.removeMuteDetails(txtMicFive, fivthMicDisable, fivthMicMuted, fivthMic);
                        exo5.setPlayWhenReady(true);
                    }
                });
            }
        });


        faveThisRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadBaseCodeForLiveRoom.faveThisRoom(getBaseContext() , theUID  , roomOwner ,
                        roomOwnerUID , roomIdInRoom.getText().toString() , roomNameInRoom.getText().toString() ,
                        R.string.followed_your_room , faveThisRoom , unFaveThisRoom );
            }
        });


        unFaveThisRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadBaseCodeForLiveRoom.unFaveThisRoom(faveThisRoom ,
                        unFaveThisRoom , theUID , roomOwner);

            }
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (typeMessageHere.getText().toString().equals("")) {
                    sendMessageButt.setEnabled(false);

                } else {
                    sendMessageButt.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        typeMessageHere.addTextChangedListener(watcher);

        doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    final UserModel user = documentSnapshot.toObject(UserModel.class);
                    final String userName = user.getUserName();
                    final String userUID = user.getTheUID();
                    final String vip = user.getVip();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                sendMessageButt.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                        DatabaseReference myRef = base.getReference(roomOwnerUID + " Chat").push();
                                        myRef.setValue(new ChatModel(typeMessageHere.getText().toString(), facebookUserName, userUID, userName,
                                                "", vip, android.R.color.transparent));
                                        typeMessageHere.setText("");
                                        onResume();
                                    }
                                });

                            } catch (Exception e) {
                                e.getMessage();
                            }
                        }
                    }).start();
                }
            }
        });

        displayChatMessage();
        LoadBaseCodeForLiveRoom.getAllMics
                (getBaseContext() , reference , theUID
                        , firstMicDisable , firstMic , txtMicOne , exo1
                        , secondMicDisable , secondMic , txtMicTwo , exo2
                        , thirdMicDisable , thirdMic , txtMicThree , exo3
                        , fourthMicDisable , fourthMic , txtMicFour, exo4
                        , fivthMicDisable , fivthMic , txtMicFive , exo5);

    }

    private AudioStreaming2 audioStreaming;
    private void prepareAudioStreamer() {
        int audioSampleRate = 44100;
        int audioBitrate = 32000;
        audioStreaming = new AudioStreaming2(audioSampleRate, audioBitrate, this,this.getBaseContext());
        audioStreaming.start();
    }


    private void releaseTheRoomInfo() {

        String id = this.getIntent().getExtras().getString("roomIdInTheRoom");
        String roomName = this.getIntent().getExtras().getString("roomNameInTheRoom");

        roomNameInRoom.setText(roomName);
        roomIdInRoom.setText(id);
    }


    //count the users and put them in the rich list
    private void countTheUsersForRichListPlease() {

        final ListView listUsers = findViewById(R.id.list_users_for_rich_gifts);

        FirebaseListAdapter<OnlineModel> adapter =
                new FirebaseListAdapter<OnlineModel>(getBaseContext(), OnlineModel.class, R.layout.list_online_for_gifts,
                        FirebaseDatabase.getInstance().getReference(roomOwnerUID + " Online")) {


                    @Override
                    public long getItemId(int i) {
                        return super.getItemId(i);
                    }

                    @Override
                    public OnlineModel getItem(int position) {
                        return super.getItem(position);
                    }

                    @Override
                    public View getView(int position, View view, ViewGroup viewGroup) {
                        return super.getView(position, view, viewGroup);
                    }

                    @Override
                    protected void populateView(final View v, final OnlineModel model, final int position) {


                        onlineGuysInRichList = v.findViewById(R.id.onlineGuysInRichList);
                        onlineGuysWithRealNameInRichList = v.findViewById(R.id.onlineGuysRealUserNameInRichList);

                        LoadBaseCodeForLiveRoom.countTheUserForRichList(model.getOnlineDudeUID() ,
                                onlineGuysInRichList , onlineGuysWithRealNameInRichList);
                    }
                };

        listUsers.setAdapter(adapter);

    }


    private String createStreamName(String roomId, String micId) {

        return roomId + micId.replace(" ", "") + SystemClock.currentThreadTimeMillis();
    }

    private String createStreamURI(String roomId, String micId, String roomOwnerUID) {
        String streamName = createStreamName(roomId, micId);
        String streamURI = streamName + "?roomId=" + roomOwnerUID + "&micId=" + micId;
        Log.d("StreamURI", streamURI);
        return streamURI;
    }


    private void roomOptions() {

        admins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                Intent i = new Intent(LiveRoom.this, AdminsUsers.class);
                i.putExtra("roomOwner", roomOwner);
                i.putExtra("roomOwnerUID", roomOwnerUID);
                startActivity(i);

            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                Intent i = new Intent(LiveRoom.this, OnlineUsers.class);
                i.putExtra("roomOwner", roomOwner);
                i.putExtra("roomOwnerUID", roomOwnerUID);
                startActivity(i);
            }
        });

        editRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                if (facebookUserName.equals(roomOwner)) {

                    Intent i = new Intent(getBaseContext(), EditMyRoom.class);
                    i.putExtra("roomName", roomNameInRoom.getText().toString());
                    i.putExtra("roomId", roomIdInRoom.getText().toString());
                    i.putExtra("roomOwner", roomOwner);
                    i.putExtra("roomDetails", roomInfo);
                    i.putExtra("roomOwnerUID", roomOwnerUID);
                    startActivity(i);
                } else {
                    new OurToast().myToast(getBaseContext(), getString(R.string.only_owner_can_access));
                }

            }
        });

        blockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                Intent i = new Intent(LiveRoom.this, BlockedForRooms.class);
                i.putExtra("roomOwner", roomOwner);
                i.putExtra("roomOwnerUID", roomOwnerUID);
                startActivity(i);
            }
        });


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                Intent i = new Intent(LiveRoom.this, ReportRoom.class);
                i.putExtra("roomName", roomNameInRoom.getText().toString());
                i.putExtra("roomID", roomIdInRoom.getText().toString());
                i.putExtra("roomOwner", roomOwner);
                i.putExtra("reportSenderName", facebookUserName);
                i.putExtra("reportSenderId", facebookID);
                i.putExtra("roomOwnerUID", roomOwnerUID);
                startActivity(i);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RemoveOnlineGuys();


                        if (exo1 != null && exo1.getPlayWhenReady() == true) {
                            exo1.stop();
                            exo1 = null;
                        }

                        if (exo2 != null && exo2.getPlayWhenReady() == true) {
                            exo2.stop();
                            exo2 = null;
                        }

                        if (exo3 != null && exo3.getPlayWhenReady() == true) {
                            exo3.stop();
                            exo3 = null;
                        }
                        if (exo4 != null && exo4.getPlayWhenReady() == true) {
                            exo4.stop();
                            exo4 = null;
                        }

                        if (exo5 != null && exo5.getPlayWhenReady() == true) {
                            exo5.stop();
                            exo5 = null;
                        }


                         if (rtmpMuxer != null && rtmpMuxer.isStarted()) {
                            try {
                                rtmpMuxer.deleteStream();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            rtmpMuxer.stop();
                            rtmpMuxer = null;
                        }
                        stopLiveMic();
                        LoadBaseCodeForLiveRoom.removeChildListener(reference);
                        //reference.removeEventListener(childListener);
                        Intent i = new Intent(LiveRoom.this, Home.class);
                        startActivity(i);
                        finish();
                    }
                }).start();
            }
        });
    }


    //setting in the room
    private void settingLayoutGo() {
        settingInTheRoom = findViewById(R.id.settingInsideRoom);
        settingLayout = findViewById(R.id.settingRoomLayout);

        settingInTheRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                if (settingLayout.getVisibility() == View.GONE) {
                    new UpAndDown().showTheLay(settingLayout);
                    if (richGiftLayout.getVisibility() == View.VISIBLE) {
                        richGiftLayout.setVisibility(View.GONE);
                        new UpAndDown().showTheLay(settingLayout);
                    } else {
                        new UpAndDown().showTheLay(settingLayout);
                    }

                } else if (settingLayout.getVisibility() == View.VISIBLE) {
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


    private void showBoxesLayoutGoClicked ()
    {
        senderBalance();
        if (richGiftLayout.getVisibility() == View.GONE) {
            new UpAndDown().showTheLay(richGiftLayout);

        } else if (richGiftLayout.getVisibility() == View.VISIBLE) {
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

    private void showBoxesLayoutGo() {
        showGiftsBoxes = findViewById(R.id.showUpGifts);

        showGiftsBoxes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                showBoxesLayoutGoClicked();
            }
        });

    }

    private void displayChatMessage() {

        final ListView listOfMessage = findViewById(R.id.list_of_messages);
        FirebaseListAdapter<ChatModel> adapter =
                new FirebaseListAdapter<ChatModel>(this, ChatModel.class, R.layout.list_chat_message,
                        FirebaseDatabase.getInstance().getReference(roomOwnerUID + " Chat")) {
                    @Override
                    protected void populateView(View v, final ChatModel model, int position) {

                        clickLay = findViewById(R.id.legClickLayout);
                        final TextView clickName = findViewById(R.id.clickName);
                        final TextView clickId = findViewById(R.id.clickId);
                        final ImageView clickImage = findViewById(R.id.clickImage);
                        final ImageView clickChat = findViewById(R.id.clickChat);
                        final ImageView clickMic = findViewById(R.id.clickMic);
                        final ImageView clickGift =findViewById(R.id.clickGift);
                        final ImageView clickBlock = findViewById(R.id.clickBlock);

                        clickGift.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                showBoxesLayoutGoClicked();
                            }
                        });

                        clickMic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                if (theUID.equals(roomOwnerUID))
                                {
                                    new OurToast().myToast(getBaseContext(), "under structure");

                                }else
                                {
                                    new OurToast().myToast(getBaseContext(), getString(R.string.only_owner_can_access));
                                }

                            }
                        });

                        clickChat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                LoadBaseCode.letUsChat(getBaseContext() , model.getMessageUserUID() , theUID , facebookUserName);
                            }
                        });


                        clickBlock.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                if (theUID.equals(roomOwnerUID))
                                {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myBlockList = database.getReference(roomOwnerUID + " BlockedForRooms").push();
                                    myBlockList.setValue(new BlockModelForRooms(model.getMessageUser() , model.getMessageUserUID()));
                                }else
                                {
                                    new OurToast().myToast(getBaseContext() , getString(R.string.only_owner_can_access));
                                }
                            }
                        });


                        LinearLayout legLay = v.findViewById(R.id.legLayout);
                        LinearLayout royalLay = v.findViewById(R.id.royalLayout);
                        LinearLayout goldenLay = v.findViewById(R.id.goldenLayout);
                        LinearLayout silverLay = v.findViewById(R.id.silverLayout);
                        LinearLayout pronzeLay = v.findViewById(R.id.pronzeLayout);
                        LinearLayout noLay = v.findViewById(R.id.UsernameLayout);


                        TextView messageText = v.findViewById(R.id.message_text);
                        whatIsTheFuckinGift = v.findViewById(R.id.whatIsTheGift);


                        TextView messageUser = v.findViewById(R.id.message_user);
                        ImageView ppForChat = v.findViewById(R.id.profilePicForChat);
                        ImageView userVIPPicture = v.findViewById(R.id.userVipPic);
                        TextView LegUSer = v.findViewById(R.id.legUser);
                        ImageView ppForChatLeg = v.findViewById(R.id.legPPForChat);

                        TextView RoyalUser = v.findViewById(R.id.royalUser);
                        ImageView ppForChatRoyal = v.findViewById(R.id.royalPPForChat);

                        TextView GoldenUser = v.findViewById(R.id.goldenUser);
                        ImageView ppForChatGolden = v.findViewById(R.id.goldenPPForChat);

                        TextView SilverUser = v.findViewById(R.id.silverUser);
                        ImageView ppForChatSilver = v.findViewById(R.id.silverPPForChat);

                        TextView PronzeUser = v.findViewById(R.id.pronzeUser);
                        ImageView ppForChatPronze = v.findViewById(R.id.pronzePPForChat);


                        switch (model.getTheVip()) {
                            case "LegendaryVIP":
                                legLay.setVisibility(View.VISIBLE);
                                royalLay.setVisibility(View.GONE);
                                goldenLay.setVisibility(View.GONE);
                                silverLay.setVisibility(View.GONE);
                                pronzeLay.setVisibility(View.GONE);
                                noLay.setVisibility(View.GONE);
                                LegUSer.setText(model.getMessageNickName());
                                messageText.setText(model.getMessageText());
                                whatIsTheFuckinGift.setBackgroundResource(model.getGifts());
                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext(), model.getMessageUserUID(), ppForChatLeg);
                                break;
                            case "RoyalVIP":
                                legLay.setVisibility(View.GONE);
                                royalLay.setVisibility(View.VISIBLE);
                                goldenLay.setVisibility(View.GONE);
                                silverLay.setVisibility(View.GONE);
                                pronzeLay.setVisibility(View.GONE);
                                noLay.setVisibility(View.GONE);
                                RoyalUser.setText(model.getMessageNickName());
                                messageText.setText(model.getMessageText());
                                whatIsTheFuckinGift.setBackgroundResource(model.getGifts());
                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext(), model.getMessageUserUID(), ppForChatRoyal);
                                break;

                            case "GoldenVIP":
                                legLay.setVisibility(View.GONE);
                                royalLay.setVisibility(View.GONE);
                                goldenLay.setVisibility(View.VISIBLE);
                                silverLay.setVisibility(View.GONE);
                                pronzeLay.setVisibility(View.GONE);
                                noLay.setVisibility(View.GONE);
                                GoldenUser.setText(model.getMessageNickName());
                                messageText.setText(model.getMessageText());
                                whatIsTheFuckinGift.setBackgroundResource(model.getGifts());
                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext(), model.getMessageUserUID(), ppForChatGolden);

                                break;
                            case "SilverVIP":
                                legLay.setVisibility(View.GONE);
                                royalLay.setVisibility(View.GONE);
                                goldenLay.setVisibility(View.GONE);
                                silverLay.setVisibility(View.VISIBLE);
                                pronzeLay.setVisibility(View.GONE);
                                noLay.setVisibility(View.GONE);
                                SilverUser.setText(model.getMessageNickName());
                                messageText.setText(model.getMessageText());
                                whatIsTheFuckinGift.setBackgroundResource(model.getGifts());
                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext(), model.getMessageUserUID(), ppForChatSilver);

                                break;
                            case "RegularVIP":
                                legLay.setVisibility(View.GONE);
                                royalLay.setVisibility(View.GONE);
                                goldenLay.setVisibility(View.GONE);
                                silverLay.setVisibility(View.GONE);
                                pronzeLay.setVisibility(View.VISIBLE);
                                noLay.setVisibility(View.GONE);
                                PronzeUser.setText(model.getMessageNickName());
                                messageText.setText(model.getMessageText());
                                whatIsTheFuckinGift.setBackgroundResource(model.getGifts());
                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext(), model.getMessageUserUID(), ppForChatPronze);

                                break;

                            case "no":
                                legLay.setVisibility(View.GONE);
                                royalLay.setVisibility(View.GONE);
                                goldenLay.setVisibility(View.GONE);
                                silverLay.setVisibility(View.GONE);
                                pronzeLay.setVisibility(View.GONE);
                                noLay.setVisibility(View.VISIBLE);
                                messageUser.setText(model.getMessageNickName());
                                messageText.setText(model.getMessageText());
                                whatIsTheFuckinGift.setBackgroundResource(model.getGifts());
                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext(), model.getMessageUserUID(), ppForChat);
                                userVIPPicture.setVisibility(View.GONE);
                                break;
                        }

                        messageUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {



                                     layClickTheUser(clickLay , clickName ,
                                        clickImage , model.getMessageNickName(),  model.getMessageUserUID() , model.getMessageUser());


                            }
                        });

                        LegUSer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                layClickTheUser(clickLay , clickName ,
                                        clickImage , model.getMessageNickName(),  model.getMessageUserUID() , model.getMessageUser());
                            }
                        });

                        RoyalUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                     layClickTheUser(clickLay , clickName ,
                                        clickImage , model.getMessageNickName(),  model.getMessageUserUID() , model.getMessageUser());


                            }
                        });

                        GoldenUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                     layClickTheUser(clickLay , clickName ,
                                        clickImage , model.getMessageNickName(),  model.getMessageUserUID() , model.getMessageUser());


                            }
                        });

                        SilverUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                     layClickTheUser(clickLay , clickName ,
                                        clickImage , model.getMessageNickName(),  model.getMessageUserUID() , model.getMessageUser());


                            }
                        });

                        PronzeUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                     layClickTheUser(clickLay , clickName ,
                                        clickImage , model.getMessageNickName(),  model.getMessageUserUID() , model.getMessageUser());
                            }
                        });
                    }
                };

        listOfMessage.setAdapter(adapter);

    }

    private void clickTheUser(String getUserName, String getTheUID, String getNickname) {
        StringTokenizer st = new StringTokenizer(getUserName, " ");
        final String userName = st.nextToken();

        if (userName.equals(facebookUserName) ||
                userName.equals("Casavoice") || userName.equals(myNickName) || getNickname.equals(myNickName)) {

            new OurToast().myToast(getBaseContext(), "try another profile");

        } else {

            Intent iProf = new Intent(getBaseContext(), UserProfileInformation.class);
            iProf.putExtra("hisOriginalName", userName);
            iProf.putExtra("hisOriginalUID", getTheUID);
            startActivity(iProf);

            DatabaseReference amBlocked = FirebaseDatabase.getInstance()
                    .getReference(theUID + " HisBlockList");
            amBlocked.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists())
                    {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            if (data.child("blockMe").getValue().equals(userName)) {
                                new OurToast().myToast(getBaseContext(), getString(R.string.you_are_bovked_from) + userName);

                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void layClickTheUser(LinearLayout clickLay , TextView clickName  , ImageView clickIamge ,
                                  final String userNickName , final String userUID , final String userName)
    {
        sp.play(soundIds[0], 1, 1, 1, 0, 1);
        if (clickLay.getVisibility()== View.GONE)
        {
            clickLay.setVisibility(View.VISIBLE);
            clickName.setText(userNickName);
            LoadingPics.loadPicsForHome(getBaseContext() , userUID , clickIamge);

            clickIamge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickTheUser(userName , userUID , userNickName);
                }
            });

        }else
        {
            clickLay.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RemoveOnlineGuys();
    }


    @Override
    protected void onResume() {
        super.onResume();
        LoadBaseCodeForLiveRoom.userBlocked(roomOwnerUID, theUID, typeMessageHere, getBaseContext() , micLay);

    }

    @Override
    protected void onPause() {
        super.onPause();
        LoadBaseCodeForLiveRoom.userBlocked(roomOwnerUID, theUID, typeMessageHere, getBaseContext() , micLay);

    }

    private void RemoveOnlineGuys() {
        RemoveOnlineGuysClass.removeGuy(roomOwnerUID, theUID);
        // RemoveOnlineGuysClass.removeGuyFromMic(roomOwnerUID , myNickName , iamOnlineOnMic , theUID);
    }

    private void selectRichGifts() {
        ferrari = findViewById(R.id.ferrariLay);
        re7a = findViewById(R.id.re7aLay);
        negma = findViewById(R.id.negmaLay);
        bugatti = findViewById(R.id.bugattiLay);
        farawla = findViewById(R.id.farawlaLay);
        coctail = findViewById(R.id.coctailLay);
        camaro = findViewById(R.id.camaroLay);
        sabeka = findViewById(R.id.sabekaLay);
        gawhara = findViewById(R.id.gawharaLay);
        jeep = findViewById(R.id.jeepLay);
        bosa = findViewById(R.id.bosaLay);
        warda = findViewById(R.id.wardaLay);
        yacht = findViewById(R.id.yachLay);
        tag = findViewById(R.id.tagLay);
        iphone = findViewById(R.id.iphoneLay);
        torta = findViewById(R.id.tortaLay);
        chocolata = findViewById(R.id.chocolataLay);


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


    @Override
    public void onClick(View view) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                switch (view.getId()) {
                    case R.id.ferrariLay:
                        ferrari.setBackgroundResource(R.drawable.selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);


                        okFerrari = true;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;

                        senderBalance();

                        break;
                    case R.id.re7aLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = true;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;

                        senderBalance();

                        break;

                    case R.id.negmaLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = true;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.bugattiLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = true;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.farawlaLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = true;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.coctailLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = true;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.camaroLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = true;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.sabekaLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = true;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.gawharaLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = true;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.jeepLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = true;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.bosaLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = true;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.wardaLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = true;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.yachLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);


                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = true;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.tagLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = true;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.iphoneLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = true;
                        okTorta = false;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.tortaLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.selector);
                        chocolata.setBackgroundResource(R.drawable.gift_selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = true;
                        okChocolata = false;
                        senderBalance();
                        break;

                    case R.id.chocolataLay:
                        ferrari.setBackgroundResource(R.drawable.gift_selector);
                        re7a.setBackgroundResource(R.drawable.gift_selector);
                        negma.setBackgroundResource(R.drawable.gift_selector);
                        bugatti.setBackgroundResource(R.drawable.gift_selector);
                        farawla.setBackgroundResource(R.drawable.gift_selector);
                        coctail.setBackgroundResource(R.drawable.gift_selector);
                        camaro.setBackgroundResource(R.drawable.gift_selector);
                        sabeka.setBackgroundResource(R.drawable.gift_selector);
                        gawhara.setBackgroundResource(R.drawable.gift_selector);
                        jeep.setBackgroundResource(R.drawable.gift_selector);
                        bosa.setBackgroundResource(R.drawable.gift_selector);
                        warda.setBackgroundResource(R.drawable.gift_selector);
                        yacht.setBackgroundResource(R.drawable.gift_selector);
                        tag.setBackgroundResource(R.drawable.gift_selector);
                        iphone.setBackgroundResource(R.drawable.gift_selector);
                        torta.setBackgroundResource(R.drawable.gift_selector);
                        chocolata.setBackgroundResource(R.drawable.selector);

                        okFerrari = false;
                        okRe7a = false;
                        okNegma = false;
                        okBugatti = false;
                        okFarawla = false;
                        okCoctail = false;
                        okCamaro = false;
                        okSabeka = false;
                        okGawhara = false;
                        okJeep = false;
                        okBosa = false;
                        okWarda = false;
                        okYacht = false;
                        okTag = false;
                        okIphone = false;
                        okTorta = false;
                        okChocolata = true;
                        senderBalance();
                        break;
                }
            }
        });
    }

    private int senderBalance() {
        doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    balance = user.getBalanceOfCoins();
                } else {

                }
            }
        });

        return balance;
    }


    private void selectGiftFromTheRichOk() {

        senderBalance();

        Button okRich = findViewById(R.id.okSelectedWithRichOne);
        okRich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                senderBalance();

                int exact = 0;
                final EditText howMany = findViewById(R.id.howManyRichGifts);
                String value = howMany.getText().toString();
                if (value.equals("")) {
                    howMany.setText("1");
                } else {
                    exact = Integer.parseInt(value);
                }

                if (okFerrari) {
                    if (balance >= (1000 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Ferrari", exact, balance,
                                1000 * exact, onlineGuysInRichList.getText().toString(),
                                onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                } else if (okRe7a) {
                    if (balance >= (800 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("re7a", exact, balance, 800 * exact,
                                onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                } else if (okNegma) {
                    if (balance >= (60 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Negma", exact, balance, 60 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                } else if (okBugatti) {

                    if (balance >= (5000 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Bugatti", exact, balance, 5000 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }


                } else if (okFarawla) {

                    if (balance >= (600 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Farawla", exact, balance, 600 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }


                } else if (okCoctail) {

                    if (balance >= (10 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Coctail", exact, balance, 10 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }


                } else if (okCamaro) {

                    if (balance >= (4000 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Camaro", exact, balance, 4000 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }


                } else if (okSabeka) {

                    if (balance >= (700 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Sabeka", exact, balance, 700 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }


                } else if (okGawhara) {

                    if (balance >= (800 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Gawhara", exact, balance, 800 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }


                } else if (okJeep) {
                    if (balance >= (2000 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Jeep", exact, balance, 2000 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                } else if (okBosa) {
                    if (balance >= (100 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Bosa", exact, balance, 100 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                } else if (okWarda) {
                    if (balance >= (6 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Warda", exact,
                                balance, 6 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                } else if (okYacht) {

                    if (balance >= (2000 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("yacht", exact, balance, 2000 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }


                } else if (okTag) {
                    if (balance >= (1200 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Tag", exact, balance, 1200 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                } else if (okIphone) {
                    if (balance >= (400 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Iphone", exact, balance, 400 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }


                } else if (okTorta) {
                    if (balance >= (40 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Torta", exact, balance, 40 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                } else if (okChocolata) {
                    if (balance >= (20 * exact) && exact > 0) {
                        dialogForMakeSureRichGift("Chocolata", exact, balance, 20 * exact, onlineGuysInRichList.getText().toString(), onlineGuysWithRealNameInRichList.getText().toString());
                    } else {
                        new OurToast().myToast(getBaseContext(), getString(R.string.error_sending_gifts));
                    }

                }

            }
        });

    }

    private void dialogForMakeSureRichGift(final String gift, final int exact,
                                           final int balance, final int cost,
                                           final String onlineName, final String onlineUID) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.your_balance_is) + String.valueOf(balance)
                + '\n' + getString(R.string.gift_cost) + String.valueOf(cost) + '\n' + getString(R.string.to) + onlineName)
                .setIcon(R.mipmap.my_icon)
                .setTitle(R.string.send);
        alertDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        sp.play(soundIds[0], 1, 1, 1, 0, 1);

                        DatabaseReference myRef = base.getReference(roomOwnerUID + " Chat").push();


                        DatabaseReference hisNoti = base.getReference(onlineUID + " Notifications").push();

                        FirebaseFirestore hisdb = FirebaseFirestore.getInstance();
                        DocumentReference hisdoc = hisdb.collection("UserInformation").document(onlineUID);

                        switch (gift) {

                            case "Ferrari":

                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(getString(R.string.x) + exact)
                                        , "no", ""
                                        , "Casavoice", "", "no", R.mipmap.ferrari));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Ferrari"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });

                                break;

                            case "re7a":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact),
                                        "no", ""
                                        , "Casavoice", "", "no", R.mipmap.re7a));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "re7a"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });

                                break;
                            case "Negma":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.ngma));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Negma"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;

                            case "Bugatti":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.bugatti));
                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Bugatti"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;

                            case "Farawla":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.farawla));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Farawla"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });

                                break;

                            case "Coctail":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.coctail));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Coctail"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;
                            case "Camaro":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.camaro));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Camaro"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;

                            case "Sabeka":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.sabeka));
                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Sabeka"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;

                            case "Gawhara":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.gawhara));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Gawhara"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });

                                break;
                            case "Jeep":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.jeep));
                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Jeep"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;

                            case "Bosa":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.bosa));
                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Bosa"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;
                            case "Warda":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.warda));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Warda"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;
                            case "yacht":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.yacht));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "yacht"));

                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;

                            case "Tag":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.tagg));
                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Tag"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;
                            case "Iphone":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.iphones));
                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Iphone"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;

                            case "Torta":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.torta));
                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Torta"));


                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;
                            case "Chocolata":
                                myRef.setValue(new ChatModel(myNickName + getString(R.string.send_to) + onlineName + String.valueOf(" X " + exact), "no", ""
                                        , "Casavoice", "", "no", R.mipmap.chocalata));

                                hisNoti.setValue(new NotificationsModel(myNickName + getString(R.string.send_you_x) + exact, "Chocolata"));

                                hisdoc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                    }
                                });
                                break;
                        }

                        final int senderNewBalance = (balance - cost);


                        //broadcast need to be public to saw from all the users <3
                        DatabaseReference refBroadcast = base.getReference("GiftsBroadcast").push();
                        refBroadcast.setValue(new BroadCastModel(myNickName, onlineName, gift, exact, roomIdInRoom.getText().toString()));

                        doc.update("balanceOfCoins", senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                LoadBaseCodeForLiveRoom.recNewBalanceAcc(cost, onlineUID);
                                LoadBaseCodeForLiveRoom.howMuchYouSend(cost , theUID );
                                LoadBaseCodeForLiveRoom.happinessRoom(cost , roomOwnerUID);
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

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        sp.play(soundIds[0], 1, 1, 1, 0, 1);
       if (settingLayout.getVisibility() == View.VISIBLE) {
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
        } else if (richGiftLayout.getVisibility() == View.VISIBLE) {
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
        } else if (clickLay.getVisibility()== View.VISIBLE)
        {
            clickLay.setVisibility(View.GONE);
        }
        else
            {
            new OurToast().myToast(getBaseContext(), getString(R.string.exit_from_setting));
            }
    }


    private void theUsersOnlineInMotherFuckerPics() {
        HorizontalListView listUsers = findViewById(R.id.hlistview);
        FirebaseListAdapter<OnlineModel> adapter =
                new FirebaseListAdapter<OnlineModel>(this, OnlineModel.class, R.layout.list_online_user_in_bar,
                        FirebaseDatabase.getInstance().getReference(roomOwnerUID + " Online")) {
                    @Override
                    protected void populateView(View v, final OnlineModel model, final int position) {

                        final ImageView userInBar = v.findViewById(R.id.usersOnLineInBar);
                        LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext(), model.getOnlineDudeUID(), userInBar);
                    }
                };

        listUsers.setAdapter(adapter);
    }

    private void preferencesTheId() {

        admins = findViewById(R.id.roomAdmins);
        online = findViewById(R.id.roomOnline);
        editRoom = findViewById(R.id.editRoom);
        blockList = findViewById(R.id.blockListButton);
        report = findViewById(R.id.roomReport);
        exit = findViewById(R.id.roomExit);


        roomNameInRoom = findViewById(R.id.roomNameInsideRoom);
        roomIdInRoom = findViewById(R.id.roomIdInsideRoom);
        richGiftLayout = findViewById(R.id.richGiftLayout);

        firstMic = findViewById(R.id.firstMic);
        secondMic = findViewById(R.id.secondMic);
        thirdMic = findViewById(R.id.thirdMic);
        fourthMic = findViewById(R.id.fourthMic);
        fivthMic = findViewById(R.id.fivthMic);

        firstMicDisable = findViewById(R.id.firstMicDisable);
        secondMicDisable = findViewById(R.id.secondMicDisable);
        thirdMicDisable = findViewById(R.id.thirdMicDisable);
        fourthMicDisable = findViewById(R.id.fourthMicDisable);
        fivthMicDisable = findViewById(R.id.fivthMicDisable);

        firstMicMuted = findViewById(R.id.firstMicMuted);
        secondMicMuted = findViewById(R.id.secondMicMuted);
        thirdMicMuted = findViewById(R.id.thirdMicMuted);
        fourthMicMuted = findViewById(R.id.fourthMicMuted);
        fivthMicMuted = findViewById(R.id.fivthMicMuted);

        txtMicOne = findViewById(R.id.textMicOne);
        txtMicTwo = findViewById(R.id.textMicTwo);
        txtMicThree = findViewById(R.id.textMicThree);
        txtMicFour = findViewById(R.id.textMicFour);
        txtMicFive = findViewById(R.id.textMicFive);

        sendMessageButt = findViewById(R.id.sendMessage);
        typeMessageHere = findViewById(R.id.typeMessage);

        introLay = findViewById(R.id.introVipLay);
        intoImage = findViewById(R.id.introVipImage);
        introNickName = findViewById(R.id.theVipNickName);

        faveThisRoom = findViewById(R.id.faveTheRoomButton);
        unFaveThisRoom = findViewById(R.id.unfaveTheRoomButton);
        profilePictureRoom = findViewById(R.id.profilePictureInsideRoom);

        roomBroadcastGifts = findViewById(R.id.roomBroadcast);
        micLay = findViewById(R.id.micLay);

    }

    // audio stuff ============

    private void requestPermission() {
        ActivityCompat.requestPermissions(LiveRoom.this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(LiveRoom.this, getString(R.string.permission_granted),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LiveRoom.this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private RtmpMuxer rtmpMuxer;
    private void initMuxer() {
        rtmpMuxer = new RtmpMuxer("www.casavoice.live", 1935, new Time() {
            @Override
            public long getCurrentTimestamp() {
                return System.currentTimeMillis();
            }
        });
        rtmpMuxer.setConnectTimeout(10000);
        // Always call start method from a background thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                rtmpMuxer.start(LiveRoom.this, "live", "rtmp://www.casavoice.live:1935", null);
                return null;
            }
        }.execute();
    }

    private void stopLiveMic() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (audioStreaming != null) {
                        audioStreaming.forceQuit();
                    }
                    audioStreaming = null;
                    if (rtmpMuxer != null && rtmpMuxer.isStarted()) {
                        try {
                            rtmpMuxer.deleteStream();
                        } catch (IOException | IllegalStateException e) {
                            e.printStackTrace();
                        }
                        rtmpMuxer.stop();
                    }
                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        });
    }

    @Override
    public void onConnected() {
        if (roomOwnerUID == null || micId == null) {
            throw new RuntimeException("roomId cannot be null or micId cannot be null. expected value but found roomOwner" + roomOwnerUID + " micId" + micId);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                  Toast.makeText(LiveRoom.this, "Connected To server", Toast.LENGTH_SHORT).show();
            }
        });
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.d("Init connection", "initConnection" + Thread.currentThread().getName());
                    rtmpMuxer.createStream(streamName);
                    //   rtmpMuxer.createStream(stream+"?roomId=" + roomOwner+ "&micId="+ micId);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    public void onReadyToPublish() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                  Toast.makeText(LiveRoom.this, "Prepare mic to publish", Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("Init connection", "initConnection" + Thread.currentThread().getName());
        prepareAudioStreamer();

    }

    @Override
    public void onConnectionError(@NonNull final IOException e) {
        e.printStackTrace();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LiveRoom.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnReceiveAudioRecordData(@NotNull MediaCodec.BufferInfo bufferInfo, boolean isHeader, final long timestamp, @NotNull final byte[] data, final int numberOfChannel, final int sampleSizeIndex) {


        if (isHeader) {
            rtmpMuxer.setAudioHeader(new AACAudioHeader() {
                @NonNull
                @Override
                public byte[] getData() {
                    return data;
                }

                @Override
                public int getNumberOfChannels() {
                    return numberOfChannel;
                }

                @Override
                public int getSampleSizeIndex() {
                    return sampleSizeIndex;
                }


            });
        } else {
            try {
                rtmpMuxer.postAudio(new AACAudioFrame() {
                    @Override
                    public long getTimestamp() {
                        return timestamp;
                    }

                    @NonNull
                    @Override
                    public byte[] getData() {
                        return data;
                    }


                });
            } catch (IOException e) {
                e.printStackTrace();
                // An error occured while sending the audio frame to the server
            }
        }
    }

    @Override
    public void OnReceiveVideoRecordData(@NotNull MediaCodec.BufferInfo bufferInfo, final boolean isHeader, final long timestamp, @NotNull final byte[] data, final boolean iKeyFrane) {
        try {
            Log.d("video data",""+data);
            rtmpMuxer.postVideo(new H264VideoFrame() {
                @Override
                public boolean isHeader() {
                    return isHeader;
                }

                @Override
                public long getTimestamp() {
                    return timestamp;
                }

                @NonNull
                @Override
                public byte[] getData() {
                    return data;
                }

                @Override
                public boolean isKeyframe() {
                    return iKeyFrane;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private class WebRTC extends JitsiMeetActivity
    {

    }
}