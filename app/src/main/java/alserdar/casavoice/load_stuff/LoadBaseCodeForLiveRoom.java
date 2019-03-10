package alserdar.casavoice.load_stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import alserdar.casavoice.ExoRadioPlayerFactory;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.animate.UpAndDown;
import alserdar.casavoice.handle_easy_stuff.RemoveOnlineGuysClass;
import alserdar.casavoice.models.BlockModelForRooms;
import alserdar.casavoice.models.BroadCastModel;
import alserdar.casavoice.models.ChatModel;
import alserdar.casavoice.models.FaveRoomsModel;
import alserdar.casavoice.models.NotificationsModel;
import alserdar.casavoice.models.OnlineModel;
import alserdar.casavoice.models.UserModel;
import alserdar.casavoice.models.VoiceModel;

import static com.google.android.gms.internal.zzahn.runOnUiThread;


public class LoadBaseCodeForLiveRoom  {


    private static ChildEventListener childEventListener ;


    public static void micsEnabled(Context context , DatabaseReference reference  ,String micId, String userName, String roomId
            , String status, String facebookName, String streamName, String userUID, ImageView userPicButton) {
        LoadingPics.loadPicsForHome(context, userUID, userPicButton);


        final Map<String, Object> updateMicOne = new HashMap<>();
        updateMicOne.put("mics", micId);
        updateMicOne.put("nickName", userName);
        updateMicOne.put("roomId", roomId);
        updateMicOne.put("status", status);
        updateMicOne.put("userName", facebookName);
        updateMicOne.put("theUri", streamName);
        updateMicOne.put("userUID", userUID);

        reference.child(micId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                // return null;
                VoiceModel voice = mutableData.getValue(VoiceModel.class);

                if (voice.getStatus().equals("Offline") || voice.getStatus().equals("off")) {
                    mutableData.setValue(updateMicOne);
                    return Transaction.success(mutableData);
                } else {
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {


            }
        });

    }

    public static void removeChildListener(DatabaseReference reference)
    {
        reference.removeEventListener(childEventListener);
    }


    // stuff =====================================

    public static void broadcastGiftsForAll(final Context context, final TextView roomBroadcastGifts) {

        FirebaseDatabase base = FirebaseDatabase.getInstance();
        DatabaseReference reference = base.getReference("GiftsBroadcast");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    BroadCastModel model = dataSnapshot.getValue(BroadCastModel.class);
                    roomBroadcastGifts.setText(String.format(" %s %s %s %s %s %s %s ", model.getSender(),
                            context.getString(R.string.send_to), model.getRec(),
                            String.valueOf(" " + model.getExact()), model.getGift(),
                            context.getString(R.string.room), model.getRoomIdForBroadcasting()));

                    TranslateAnimation animation = new TranslateAnimation(0, roomBroadcastGifts.getWidth() + 150, 0, 0); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
                    animation.setDuration(5000); // animation duration
                    animation.setRepeatCount(2); // animation repeat count
                    roomBroadcastGifts.startAnimation(animation);
                } else {

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
    }


    public static void countTheUserForRichList(final String getOnlineDudeUID,
                                               final TextView onlineGuysInRichList,
                                               final TextView onlineGuysWithRealNameInRichList) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(getOnlineDudeUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    UserModel user = documentSnapshot.toObject(UserModel.class);

                    LoadUIForLiveRoom.countUserForRichList(onlineGuysInRichList, onlineGuysWithRealNameInRichList, user);

                }
            }
        });

    }

    public static void IsThisRoomInFaveOrNot
            (final String theUID, final String roomOwnerUID,
             final Button faveThisRoom, final Button unFaveThisRoom) {

        DatabaseReference faveOrNot = FirebaseDatabase.getInstance()
                .getReference(theUID + " FaveRooms");
        faveOrNot.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        if (data.child("faveRoomOwnerUID").getValue().equals(roomOwnerUID)) {

                            LoadUIForLiveRoom.faveThisRoomUI(faveThisRoom, View.GONE, unFaveThisRoom, View.VISIBLE);

                        } else {
                            LoadUIForLiveRoom.faveThisRoomUI(faveThisRoom, View.VISIBLE, unFaveThisRoom, View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void faveThisRoom
            (final Context context, final String theUID, final String roomOwner,
             final String roomOwnerUID, final String roomIdInRoom,
             final String roomNameInRoom, final int followedYourRoom, Button faveThisRoom, Button unFaveThisRoom) {

        final FirebaseDatabase base = FirebaseDatabase.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        faveThisRoom.setVisibility(View.GONE);
        unFaveThisRoom.setVisibility(View.VISIBLE);

        LoadUIForLiveRoom.faveThisRoomUI(faveThisRoom, View.GONE, unFaveThisRoom, View.VISIBLE);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(theUID + " FaveRooms").push();
        myRef.setValue(new FaveRoomsModel(roomOwner, roomOwnerUID, roomIdInRoom, roomNameInRoom));


        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    String nickName = user.getUserName();

                    DatabaseReference myNoti = base.getReference(roomOwnerUID + " Notifications").push();
                    myNoti.setValue(new NotificationsModel(nickName + context.getString(followedYourRoom), "loved"));

                    DocumentReference doc = db.collection("UserInformation").document(roomOwnerUID);
                    doc.update("hasNotification", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            //  new YouHaveNewMessageBroadcast().gotMessages(getBaseContext());
                        }
                    });
                }

            }
        });


    }

    public static void unFaveThisRoom
            (final Button faveThisRoom, final Button unFaveThisRoom,
             final String theUID, final String roomOwner) {

        LoadUIForLiveRoom.faveThisRoomUI(faveThisRoom, View.VISIBLE, unFaveThisRoom, View.GONE);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        com.google.firebase.database.Query applesQuery = ref.child(theUID + " FaveRooms").orderByChild("faveRoomOwner").equalTo(roomOwner);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    public static void theLastOnlineVIPNewOne(final String roomOwnerUID,
                                              final LinearLayout introLay,
                                              final TextView introNickname,
                                              final ImageView introIamge,
                                              final Context context) {

        FirebaseDatabase base = FirebaseDatabase.getInstance();
        final DatabaseReference reference = base.getReference(roomOwnerUID + " OnlineVIP");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    OnlineModel model = dataSnapshot.getValue(OnlineModel.class);
                    assert model != null;
                    theVIPIntro(model.getOnlineDudeUID(), roomOwnerUID, introLay, introNickname, introIamge, context);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private static void theVIPIntro(String theUID, final String roomOwnerUID,
                                    final LinearLayout introLay, final TextView
                                            introNickName, final ImageView intoImage, final Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()) {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    boolean hasVip = user.isHasVip();
                    if (hasVip) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();

                        String vip = user.getVip();
                        String theUser = user.getUserName();

                        switch (vip) {
                            case "LegendaryVIP":
                                DatabaseReference refLeg = database.getReference(roomOwnerUID + " Chat").push();
                                refLeg.setValue(new ChatModel(context.getString(R.string.legendary_vip_join_the_room), "no", user.getTheUID()
                                        , theUser, "", vip, R.mipmap.vip_car));

                                showMeTheVipLogoIntro(introLay);
                                introNickName.setText(theUser);
                                Bitmap diamond = BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_diamond);
                                intoImage.setImageBitmap(diamond);

                                break;
                            case "RoyalVIP":
                                DatabaseReference refRoyal = database.getReference(roomOwnerUID + " Chat").push();
                                refRoyal.setValue(new ChatModel(context.getString(R.string.royal_vip_join_the_room), "no", user.getTheUID()
                                        , theUser, "", vip, R.mipmap.bugatti));
                                showMeTheVipLogoIntro(introLay);
                                introNickName.setText(theUser);
                                Bitmap royal = BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_platinum);
                                intoImage.setImageBitmap(royal);

                                break;
                            case "GoldenVIP":
                                DatabaseReference refGolden = database.getReference(roomOwnerUID + " Chat").push();
                                refGolden.setValue(new ChatModel(context.getString(R.string.golden_vip_join_the_room), "no", user.getTheUID()
                                        , theUser, "", vip, R.mipmap.vip_golden));
                                showMeTheVipLogoIntro(introLay);
                                introNickName.setText(theUser);
                                Bitmap golden = BitmapFactory.decodeResource(context.getResources(), R.mipmap.vip_golden);
                                intoImage.setImageBitmap(golden);
                                break;
                            case "SilverVIP":
                                DatabaseReference refSilver = database.getReference(roomOwnerUID + " Chat").push();
                                refSilver.setValue(new ChatModel(context.getString(R.string.silver_vip_join_the_room), "no", user.getTheUID()
                                        , theUser, "", vip, R.mipmap.vip_silver));
                                break;
                            case "RegularVIP":
                                DatabaseReference refReg = database.getReference(roomOwnerUID + " Chat").push();
                                refReg.setValue(new ChatModel(context.getString(R.string.regular_vip_join_the_room), "no", user.getTheUID()
                                        , theUser, "", vip, R.mipmap.vip_pronze));
                                break;
                            case "no":
                                break;
                        }
                    } else {

                    }
                }
            }
        });
    }

    private static void showMeTheVipLogoIntro(final LinearLayout introLay) {

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

    public static void userBlocked(final String roomOwnerUID,
                                   final String theUID,
                                   final EditText typeMessageHere,
                                   final Context context ,
                                   final LinearLayout micLay) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(roomOwnerUID + " BlockedForRooms").orderByChild("blockedForRoomsUID").equalTo(theUID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                        appleSnapshot.getRef();

                        // Get Post object and use the values to update the UI
                        BlockModelForRooms post = appleSnapshot.getValue(BlockModelForRooms.class);

                        assert post != null;
                        if (post.getBlockedForRoomsUID().equals(theUID)) {
                            new OurToast().myToast(context, context.getString(R.string.you_are_blocked_from_this_room));
                            typeMessageHere.setVisibility(View.GONE);
                            RemoveOnlineGuysClass.removeGuy(roomOwnerUID, theUID);
                            micLay.setVisibility(View.INVISIBLE);

                        } else {

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    private static void updateMicOneState
            (final Context context, final VoiceModel model, String theUID , final ImageView firstMicDisable ,
             ImageView firstMic , final TextView txtMicOne ,SimpleExoPlayer exo1 ) {


        assert model != null;
        if (model.getMics().equals("mic one")) {
            if (model.getStatus().equals("live")) {
                LoadingPics.loadPicsForHome(context, model.getUserUID(), firstMicDisable);
                if (exo1 != null) {
                    exo1.getPlayWhenReady();
                    exo1.release();
                    exo1 = null;
                }
                //   exo1 = ExoRadioPlayerFactory.createHLSExoPlayer(getBaseContext(), Uri.parse("rtmp://www.casavoice.live:1935/live/demo"));
                SimpleExoPlayer tmpExo = exo1 = ExoRadioPlayerFactory.createHLSExoPlayer(context, Uri.parse("rtmp://www.casavoice.live:1935/live/" + model.getTheUri()));
                if (tmpExo != null) {
                    exo1 = tmpExo;

                    updateMicButtonState(firstMic, View.GONE, firstMicDisable, View.VISIBLE, txtMicOne, model.getNickName());
                    new OurToast().myToast(context, "Mic One is Live ....");

                    if (model.getUserUID().equals(theUID)) {
                        exo1.stop();
                    }
                } else {
                    return;
                }

            } else if (model.getStatus().equals("pending")) {
                //  new OurToast().myToast(getBaseContext(), "Mic one try to Connecting to server ...");
            } else if (model.getStatus().equals("off")) {

                if (exo1 != null && exo1.getPlayWhenReady() == true) {
                    exo1.stop();
                }
                updateMicButtonState(firstMic, View.VISIBLE, firstMicDisable, View.GONE, txtMicOne, "");
            }
        }
    }


    private static void updateMicTwoState(final Context context, final VoiceModel model, String theUID ,
                                          final ImageView secondMicDisable ,
                                          ImageView secondMic , final TextView txtMicTwo ,SimpleExoPlayer exo2 )
    {
        if (model.getMics().equals("mic two")) {
            if (model.getStatus().equals("live")) {
                LoadingPics.loadPicsForHome(context, model.getUserUID(), secondMicDisable);
                if (exo2 != null) {
                    exo2.getPlayWhenReady();
                    exo2.release();
                    exo2 = null;
                }
                SimpleExoPlayer tmpExo = exo2 = ExoRadioPlayerFactory.createHLSExoPlayer(context, Uri.parse("rtmp://www.casavoice.live:1935/live/" + model.getTheUri()));
                if(tmpExo != null){
                    exo2 = tmpExo;

                    updateMicButtonState(secondMic, View.GONE, secondMicDisable, View.VISIBLE, txtMicTwo, model.getNickName());
                    new OurToast().myToast(context, "Mic Two is Live ....");

                    if (model.getUserUID().equals(theUID))
                    {
                        exo2.stop();
                    }
                }else {
                    return ;
                }

            } else if (model.getStatus().equals("pending")) {
                //   new OurToast().myToast(getBaseContext(), "Mic two trying to Connecting to server ...");
            } else if (model.getStatus().equals("off")) {

                if (exo2 != null && exo2.getPlayWhenReady() == true) {
                    exo2.stop();
                }
                updateMicButtonState(secondMic, View.VISIBLE, secondMicDisable, View.GONE, txtMicTwo, "");
            }
        }
    }


    private static void updateMicThreeState(final Context context, final VoiceModel model, String theUID ,
                                            final ImageView thirdMicDisable , ImageView thirdMic , final TextView txtMicThree ,SimpleExoPlayer exo3)
    {
        if (model.getMics().equals("mic three")) {
            if (model.getStatus().equals("live")) {
                LoadingPics.loadPicsForHome(context, model.getUserUID(), thirdMicDisable);
                if (exo3 != null) {
                    exo3.getPlayWhenReady();
                    exo3.release();
                    exo3 = null;
                }
                SimpleExoPlayer tmpExo = exo3 = ExoRadioPlayerFactory.createHLSExoPlayer(context, Uri.parse("rtmp://www.casavoice.live:1935/live/" + model.getTheUri()));
                if(tmpExo != null){
                    exo3 = tmpExo;

                    updateMicButtonState(thirdMic, View.GONE, thirdMicDisable, View.VISIBLE, txtMicThree, model.getNickName());
                    new OurToast().myToast(context, "Mic Three is Live ...");

                    if (model.getUserUID().equals(theUID))
                    {
                        exo3.stop();
                    }
                }else {
                    return ;
                }
            } else if (model.getStatus().equals("pending")) {
                //  new OurToast().myToast(getBaseContext(), "Mic three trying to Connecting to server ...");
            } else if (model.getStatus().equals("off")) {
                if (exo3 != null && exo3.getPlayWhenReady() == true) {
                    exo3.stop();
                    //    new OurToast().myToast(getBaseContext(), "exo three stop , its not null (Off)");
                }
                updateMicButtonState(thirdMic, View.VISIBLE, thirdMicDisable, View.GONE, txtMicThree, "");
            }
        }
    }

    private static void updateMicFourState
            (final Context context, final VoiceModel model, String theUID , final ImageView fourthMicDisable ,
             ImageView fourthMic , final TextView txtMicFour ,SimpleExoPlayer exo4 )
    {
        if (model.getMics().equals("mic four")) {
            if (model.getStatus().equals("live")) {

                LoadingPics.loadPicsForHome(context, model.getUserUID(), fourthMicDisable);
                if (exo4 != null) {
                    exo4.getPlayWhenReady();
                    exo4.release();
                    exo4 =  null ;
                }
                SimpleExoPlayer tmpExo = exo4 = ExoRadioPlayerFactory.createHLSExoPlayer(context, Uri.parse("rtmp://www.casavoice.live:1935/live/" + model.getTheUri()));
                if(tmpExo != null){
                    exo4 = tmpExo;

                    updateMicButtonState(fourthMic, View.GONE, fourthMicDisable, View.VISIBLE, txtMicFour, model.getNickName());
                    new OurToast().myToast(context, "Mic Four is Live ...");

                    if (model.getUserUID().equals(theUID))
                    {
                        exo4.stop();
                    }
                }else {
                    return ;
                }
            } else if (model.getStatus().equals("pending")) {
                //  new OurToast().myToast(getBaseContext(), "Mic four trying to Connecting to server ...");
            } else if (model.getStatus().equals("off")) {
                if (exo4 != null && exo4.getPlayWhenReady() == true) {
                    exo4.stop();
                }

                updateMicButtonState(fourthMic, View.VISIBLE, fourthMicDisable, View.GONE, txtMicFour, "");
            }
        }
    }

    private static void updateMicFiveState
            (final Context context, final VoiceModel model, String theUID , final ImageView fivthMicDisable ,
             ImageView fivthMic , final TextView txtMicFive ,SimpleExoPlayer exo5)
    {


        if (model.getMics().equals("mic five")) {
            if (model.getStatus().equals("live")) {

                LoadingPics.loadPicsForHome(context, model.getUserUID(), fivthMicDisable);
                if (exo5 != null) {
                    exo5.getPlayWhenReady();
                    exo5.release();
                    exo5 = null;
                }
                SimpleExoPlayer tmpExo = exo5 = ExoRadioPlayerFactory.createHLSExoPlayer(context, Uri.parse("rtmp://www.casavoice.live:1935/live/" + model.getTheUri()));
                if(tmpExo != null){
                    exo5 = tmpExo;

                    updateMicButtonState(fivthMic, View.GONE, fivthMicDisable, View.VISIBLE, txtMicFive, model.getNickName());
                    new OurToast().myToast(context, "Mic Five is Live ...");

                    if (model.getUserUID().equals(theUID))
                    {
                        exo5.stop();
                    }
                }else {
                    return ;
                }
            } else if (model.getStatus().equals("pending")) {
                //  new OurToast().myToast(getBaseContext(), "Mic five trying to Connecting to server ...");
            } else if (model.getStatus().equals("off")) {
                if (exo5 != null && exo5.getPlayWhenReady() == true) {
                    exo5.stop();
                }

                updateMicButtonState(fivthMic, View.VISIBLE, fivthMicDisable, View.GONE, txtMicFive, "");
            }
        }
    }


    private static void updateMicButtonState(View mic, int micVisibility, View micDisable, int micDisableVisibility, TextView micTextView, String micName) {
        mic.setVisibility(micVisibility);
        micDisable.setVisibility(micDisableVisibility);
        micTextView.setText(micName);
    }


    public static void getAllMics(final Context context , DatabaseReference reference ,
                                  final String theUID , final ImageView firstMicDisable ,
                                  final ImageView firstMic , final TextView txtMicOne , final SimpleExoPlayer exo1 ,
                                  final ImageView secondMicDisable ,final ImageView secondMic ,
                                  final TextView txtMicTwo , final SimpleExoPlayer exo2 ,
                                  final ImageView thirdMicDisable ,final ImageView thirdMic ,
                                  final TextView txtMicThree , final SimpleExoPlayer exo3 ,
                                  final ImageView fourthMicDisable ,final ImageView fourthMic ,
                                  final TextView txtMicFour , final SimpleExoPlayer exo4 ,
                                  final ImageView fivthMicDisable ,final ImageView fivthMic ,
                                  final TextView txtMicFive , final SimpleExoPlayer exo5) {

        childEventListener = reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    VoiceModel model = dataSnapshot.getValue(VoiceModel.class);
                    updateMicOneState(context, model, theUID, firstMicDisable, firstMic, txtMicOne, exo1);
                    updateMicTwoState(context, model, theUID, secondMicDisable, secondMic, txtMicTwo, exo2);
                    updateMicThreeState(context, model, theUID, thirdMicDisable, thirdMic, txtMicThree, exo3);
                    updateMicFourState(context, model, theUID, fourthMicDisable, fourthMic, txtMicFour, exo4);
                    updateMicFiveState(context, model, theUID, fivthMicDisable, fivthMic, txtMicFive, exo5);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    final VoiceModel model = dataSnapshot.getValue(VoiceModel.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateMicOneState(context, model, theUID, firstMicDisable, firstMic, txtMicOne, exo1);
                            updateMicTwoState(context, model, theUID, secondMicDisable, secondMic, txtMicTwo, exo2);
                            updateMicThreeState(context, model, theUID, thirdMicDisable, thirdMic, txtMicThree, exo3);
                            updateMicFourState(context, model, theUID, fourthMicDisable, fourthMic, txtMicFour, exo4);
                            updateMicFiveState(context, model, theUID, fivthMicDisable, fivthMic, txtMicFive, exo5);
                        }
                    });

                }

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
    }


    public static void happinessRoom(final int muchSendInTheRoom , final String roomOwnerUID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("RoomInformation").document(roomOwnerUID);
        doc.update("happinessRoom", muchSendInTheRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // nees to reset the happenss every day <3
            }
        });

    }

    public static void howMuchYouSend(final int muchSend , final String theUID) {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    final UserModel user = documentSnapshot.toObject(UserModel.class);
                    final int dailySendBalance = user.getDailySendBalance();
                    int weeklySendBalance = user.getWeeklySendBalance();
                    int monthlySendBalance = user.getMonthlySendBalance();
                    final int howMuchLevel = user.getLevel();

                    //doc = db.collection("UserInformation").document(theUID);
                    doc.update("dailySendBalance", dailySendBalance + muchSend).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (howMuchLevel <= 10) {
                                if (muchSend >= 10000 && dailySendBalance >= 20000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
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
                            } else if (howMuchLevel > 10 && howMuchLevel <= 25) {
                                if (muchSend >= 20000 && dailySendBalance >= 30000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
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
                            } else if (howMuchLevel > 25 && howMuchLevel <= 50) {
                                if (muchSend >= 30000 && dailySendBalance >= 50000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
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
                            } else if (howMuchLevel > 50) {
                                if (muchSend >= 50000 && dailySendBalance >= 200000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
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

                    doc.update("weeklySendBalance", weeklySendBalance + muchSend).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                    doc.update("monthlySendBalance", monthlySendBalance + muchSend).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                } else {

                }

            }
        });


    }

    public static void howMuchYouRec(final int muchRec, final String theUID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    final int dailyRecBalance = user.getDailyRecBalance();
                    int weeklyRecBalance = user.getWeeklyRecBalance();
                    int monthlyRecBalance = user.getMonthlyRecBalance();
                    final int howMuchLevel = user.getLevel();


                    doc.update("dailyRecBalance", dailyRecBalance + muchRec).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            if (howMuchLevel <= 10) {
                                if (muchRec >= 10000 && dailyRecBalance >= 20000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
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
                            } else if (howMuchLevel > 10 && howMuchLevel <= 25) {
                                if (muchRec >= 20000 && dailyRecBalance >= 30000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
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
                            } else if (howMuchLevel > 25 && howMuchLevel <= 50) {
                                if (muchRec >= 30000 && dailyRecBalance >= 50000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
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
                            } else if (howMuchLevel > 50) {
                                if (muchRec >= 50000 && dailyRecBalance >= 200000) {
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
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

                    doc.update("weeklyRecBalance", weeklyRecBalance + muchRec).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });

                    doc.update("monthlyRecBalance", monthlyRecBalance + muchRec).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
                } else {

                }

            }
        });

    }

    public static void recNewBalanceAcc(final int cost, final String theUID ) {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    int recBalance = user.getBalanceOfCoins();
                    final int earn = (cost * 40 / 100);
                    final int newCost = recBalance + earn;
                    DocumentReference doc = db.collection("UserInformation").document(theUID);
                    doc.update("balanceOfCoins", newCost).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            howMuchYouRec(earn, theUID);
                        }
                    });
                }
            }
        });

    }





}
