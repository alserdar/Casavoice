package alserdar.casavoice.load_stuff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;

import alserdar.casavoice.LiveRoom;
import alserdar.casavoice.OurNotificationMessages;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.RoomsListAdapter;
import alserdar.casavoice.fab_setting_classes.ActivityAndNotifications;
import alserdar.casavoice.fab_setting_classes.ChatPrivatly;
import alserdar.casavoice.fab_setting_classes.Messages;
import alserdar.casavoice.fab_setting_classes.spin_wheels.SpinWheels;
import alserdar.casavoice.models.FollowersModel;
import alserdar.casavoice.models.FollowingModel;
import alserdar.casavoice.models.HisBlockModel;
import alserdar.casavoice.models.MyBlockModel;
import alserdar.casavoice.models.NotificationsModel;
import alserdar.casavoice.models.RoomModel;
import alserdar.casavoice.models.UserModel;

public class LoadBaseCode {


    public static void hasSpinOrNo(String theUID , final ImageView spinno)
    {
        final FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (userModel.isSpin())
                    {
                        spinno.setVisibility(View.GONE);
                    }else
                    {
                        spinno.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public static void hasRoom(final String theUID , final Button userProfileInfoRoomButton , final TextView userProfileRoomId)
    {
        final FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    final UserModel userModel = documentSnapshot.toObject(UserModel.class);
                    if (userModel.isHasRoom())
                    {
                        DocumentReference docRoom = db.collection("RoomInformation").document(theUID);
                        docRoom.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists())
                                {
                                    RoomModel roomModel = documentSnapshot.toObject(RoomModel.class);
                                    if (roomModel.isRoomLocked())
                                    {
                                        userProfileRoomId.setText(roomModel.getRoomId());
                                        userProfileInfoRoomButton.setText(roomModel.getRoomName());
                                        userProfileInfoRoomButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.lock, 0);
                                    }else
                                    {
                                        userProfileRoomId.setText(roomModel.getRoomId());
                                        userProfileInfoRoomButton.setText(roomModel.getRoomName());
                                    }

                                }
                            }
                        });
                    }else
                    {
                        userProfileInfoRoomButton.setText(R.string.have_not_room);
                    }
                }
            }
        });
    }


    public static void readBalance(final Context context , String theUID , final TextView balance)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    balance.setText(String.format("%s %s", context.getString(R.string.your_balance), String.valueOf(user.getBalanceOfCoins())));

                }else
                {

                }

            }
        });

    }

    public static void loadFaveRooms
            (String getFaveRoomOwnerUID , final TextView faveRoomName,
             final TextView faveRoomInfo, final TextView faveRoomOwner , final ImageView faveRoomPeople)
    {
        final FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
        final DocumentReference doc = db.collection("RoomInformation").document(getFaveRoomOwnerUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    final RoomModel roomModel = documentSnapshot.toObject(RoomModel.class);
                    DocumentReference doc = db.collection("UserInformation").document(roomModel.getRoomOwnerUID());
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                String nickName = user.getUserName();

                                faveRoomName.setText(roomModel.getRoomName());
                                faveRoomInfo.setText(roomModel.getInfoAboutRoom());
                                faveRoomOwner.setText(nickName);
                            }
                        }
                    });




                    if (roomModel.isRoomLocked())
                    {
                        faveRoomPeople.setBackgroundResource(R.mipmap.lock);
                    }else
                    {

                    }

                }
            }
        });
    }


    public static void clickMyRoomLayout
            (final Context context , final String roomOwnerUID ,
             final String facebookUserName , final String theUID , final String nickName ,
             final String roomID , final String roomName , final String roomOwner)
    {
        RoomsListAdapter.isHeAlreadyOnline(roomOwnerUID , theUID ,
                facebookUserName , nickName);

        Intent i = new Intent(context , LiveRoom.class);
        i.putExtra("roomIdInTheRoom" , roomID);
        i.putExtra("roomNameInTheRoom" , roomName);
        i.putExtra("ownerRoom" , roomOwner);
        i.putExtra("onlineGuys" , facebookUserName);
        i.putExtra("onlineGuysUID" , theUID);
        i.putExtra("roomOwnerUID" , theUID);
        context.startActivity(i);
        ((Activity)context).finish();

    }


    public static void LoadInfoHasRoomOrNot
            (final String theUID , final Context context ,
             final ImageView roomPictureView , final LinearLayout createRoomLayout , final LinearLayout myRoomLayout)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    if (user.isHasRoom())
                    {
                        LoadUICode.loadInfoForMyRoomUI(createRoomLayout , View.GONE ,
                                myRoomLayout, View.VISIBLE);

                        LoadingPics.loadPicForRooms(context.getApplicationContext() , theUID , roomPictureView);

                    }else
                    {
                        LoadUICode.loadInfoForMyRoomUI(createRoomLayout , View.VISIBLE ,
                                myRoomLayout,View.GONE );

                    }
                }
            }
        });
    }


    public static void LoadAllRooms(final Context context , final ListView listAllRooms)
    {
        try {
// this will custom by happiness
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Query query = db.collection("RoomInformation").orderBy("happiness", Query.Direction.DESCENDING);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {

                    if (documentSnapshots.isEmpty())
                    {

                    }else
                    {
                        List<RoomModel> listRooms = documentSnapshots.toObjects(RoomModel.class);
                        final RoomsListAdapter adapter = new RoomsListAdapter(context , listRooms);
                        listAllRooms.setAdapter(adapter);
                        int currentPosition = listAllRooms.getFirstVisiblePosition();
                        listAllRooms.setSelection(currentPosition);
                    }

                }
            });
        }catch (Exception e)
        {
            e.getMessage();
        }
    }


    public static void MyNotificationHome(final Context context , final String theUID)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference doc = db.collection("UserInformation").document(theUID);

                    doc.update("hasNotification" , false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent i = new Intent(context , ActivityAndNotifications.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);

                        }
                    });

                }catch (Exception e)
                {
                    e.getCause();
                }
            }
        }).start();
    }

    public static void myNewMessage(final Context context , final String theUID)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {


                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference doc = db.collection("UserInformation").document(theUID);

                    doc.update("hasMessages" , false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent i = new Intent(context , Messages.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(i);
                        }
                    });
                }catch (Exception e)
                {
                    e.getCause();
                }
            }
        }).start();
    }

    public static void myNewSpinno(final Context context , final String theUID)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(context , SpinWheels.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }).start();
    }

    public static void timeCalculated(final Context context , final String theUID) {

        final  FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRoom = db.collection("RoomInformation").document(theUID);
        docRoom.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    RoomModel model = documentSnapshot.toObject(RoomModel.class);
                    if (model.isRoomLocked())
                    {
                        if (new Date().after(model.getEndLockDate()))
                        {
                            docRoom.update("roomLocked" , false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {
                                        new OurNotificationMessages().OurNotification(context , context.getString(R.string.you_can_lock_your_room_again_));

                                    }
                                }
                            });
                        }

                    }
                }
            }
        });
        final DocumentReference docUser = db.collection("UserInformation").document(theUID);
        docUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    UserModel model = documentSnapshot.toObject(UserModel.class);
                    if (model.isHasVip())
                    {
                        assert model.getEndVip() != null ;
                        if (new Date().after(model.getEndVip()))
                        {
                            docUser.update("hasVip" , false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful())
                                    {

                                        docUser.update("vip" , "no").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful())
                                                {
                                                    new OurNotificationMessages().OurNotification(context , context.getString(R.string.you_has_no_more_vip));

                                                }
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });


    }


    public static void loadInfoForHome (final Context context , final String theUID ,
                                  ImageView profilePic , final TextView homeUsername)
    {
        try {
            LoadingPics.loadPicsForHome(context , theUID , profilePic);



            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference doc = db.collection("UserInformation").document(theUID);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists())
                    {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        if (user.isUserSus())
                        {
                            new OurToast().myToast(context , context.getString(R.string.you_are_suspended));
                            ((Activity)context).finish();

                        }else
                        {
                            if (DetectUserInfo.myUserName(context).equals("myUserName"))
                            {
                                SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(context);
                                final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                                editor.putString("myUserName" , user.getUserName());
                                editor.apply();
                            }
                            homeUsername.setText(user.getUserName());
                        }

                    }
                }
            });
        }catch (Exception e)
        {
            e.getMessage();
        }
    }


    public static void readSingleUserCustomObject
            (final Context context , final String theUID , final String hisOriginalUID, final TextView userNameProfileInfo ,
             final TextView idProfileInfo , final TextView levelProfileInfo , final TextView genderProfileInfo ,
             final TextView ageProfileInfo , final TextView homerProfileInfo , final TextView carProfileInfo ,
             final TextView petProfileInfo , final Button follow , final Button unfollow , final Button block ,
             final TextView txtFollow , final Button chat , final TextView txtBlock , final ImageView carPic , final ImageView petPic) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(hisOriginalUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);

                    LoadUICode.readUserInfo(context , user , userNameProfileInfo , idProfileInfo ,
                            levelProfileInfo , genderProfileInfo , ageProfileInfo , homerProfileInfo ,
                            carProfileInfo , petProfileInfo , carPic , petPic);

                }else
                {

                }
            }
        });

        DatabaseReference databaseFollowing = FirebaseDatabase.getInstance().getReference(theUID + " FollowingList");
        databaseFollowing.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        if (data.child("theFollowingDudesUID").getValue().equals(hisOriginalUID))
                        {
                            LoadUICode.readUserInfoForFollowingList(context , follow  , View.GONE
                                    , unfollow , View.VISIBLE , block , false , txtFollow , R.string.unfollow);
                        }else
                        {
                            LoadUICode.readUserInfoForFollowingList(context , follow  , View.VISIBLE
                                    , unfollow , View.GONE , block , true , txtFollow , R.string.follow);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference databaseBlockHim = FirebaseDatabase.getInstance()
                .getReference(theUID + " MyBlockList");
        databaseBlockHim.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    if (data.child("blockedHimUID").getValue().equals(hisOriginalUID))
                    {
                        LoadUICode.readUserForMyBlockList(context , chat , false ,
                                follow , false , txtBlock , R.string.unblock , Color.RED);
                    }else
                    {
                        LoadUICode.readUserForMyBlockList(context , chat , true ,
                                follow , true , txtBlock , R.string.block , Color.BLACK);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DatabaseReference databaseBlockMe = FirebaseDatabase.getInstance()
                .getReference(hisOriginalUID + " HisBlockList");
        databaseBlockMe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    for(DataSnapshot data : dataSnapshot.getChildren())
                    {
                        if (data.child("blockMeUID").getValue().equals(theUID))
                        {
                            LoadUICode.readUserForHisBlockList(chat , false , follow , false);
                        }else
                        {
                            LoadUICode.readUserForHisBlockList(chat , true , follow , true);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public static void letUsChat(final Context context , final String hisOriginalUID , final String theUID ,
                                 final String hisOriginalName )
    {
        if(hisOriginalUID.equals(theUID))
        {

        }else
        {
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            final DocumentReference doc = db.collection("UserInformation").document(hisOriginalUID);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot.exists())
                    {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        String id = user.getId();
                        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
                        String theRealIdOfTheOwnerOfTheAccount = getInfo.getString("user_id" , "user_id");

                        final Long sumTheId = Long.parseLong(id) + Long.parseLong(theRealIdOfTheOwnerOfTheAccount);

                        final DocumentReference docsy = db.collection("UserInformation").document(theUID);
                        docsy.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                if (documentSnapshot.exists())
                                {
                                    if (documentSnapshot.getBoolean(sumTheId + " pc") == null)
                                    {
                                        docsy.update(sumTheId + " pc", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Intent i = new Intent(context , ChatPrivatly.class);
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                i.putExtra("theSum" , sumTheId);
                                                i.putExtra("to" , hisOriginalName);
                                                i.putExtra("toUID" , hisOriginalUID);
                                                context.startActivity(i);

                                            }
                                        });
                                    }else
                                    {
                                        Intent i = new Intent(context , ChatPrivatly.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        i.putExtra("theSum" , sumTheId);
                                        i.putExtra("to" , hisOriginalName);
                                        i.putExtra("toUID" , hisOriginalUID);
                                        context.startActivity(i);
                                    }
                                }else
                                {

                                }


                            }
                        });

                    }else
                    {

                    }

                }
            });
        }
    }


    public static void followFun
            (final Context context , final String theUID , final String hisOriginalUID ,
             final String hisOriginalName , final String myOriginalName ,
             final String userNameProfileInfo , int followed ,
             Button follow , Button unfollow , TextView txtFollow , int unfollowed , final int followedYou)
    {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final  FirebaseDatabase database = FirebaseDatabase.getInstance();
        if (hisOriginalUID.equals(theUID))
        {

        }else
        {

            DatabaseReference myFollowingList = database.getReference(theUID + " FollowingList").push();
            myFollowingList.setValue(new FollowingModel(hisOriginalName , hisOriginalUID));
            new OurToast().myToast(context ,String.format(" %s %s " , userNameProfileInfo , context.getString(followed)));

            LoadUICode.followFunUI(context , follow , View.GONE , unfollow , View.VISIBLE , txtFollow , unfollowed);

            //add me to the fucking guy followers List
            DatabaseReference hisFollowersList = database.getReference(hisOriginalUID + " FollowersList").push();
            hisFollowersList.setValue(new FollowersModel(myOriginalName , theUID));

             DocumentReference doc  = db.collection("UserInformation").document(theUID);
             doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists())
                    {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        String nickName = user.getUserName();
                        FirebaseDatabase myNotifications = FirebaseDatabase.getInstance();
                        DatabaseReference myNoti = myNotifications.getReference(hisOriginalUID + " Notifications").push();
                        myNoti.setValue(new NotificationsModel( " " + nickName + " " + context.getString(followedYou), "loved"));

                        DocumentReference doc = db.collection("UserInformation").document(hisOriginalUID);
                        doc.update("hasNotification" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //  new YouHaveNewMessageBroadcast().gotMessages(getBaseContext());
                            }
                        });
                    }

                }
            });

        }
    }

    public static void hasMessages (final String theUID , final ImageView newMessages)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db = FirebaseFirestore.getInstance();
                    DocumentReference doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                boolean hasMessages = user.isHasMessages();
                                if (hasMessages)
                                {
                                    newMessages.setVisibility(View.VISIBLE);
                                }else
                                {
                                    newMessages.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                }catch (Exception e)
                {
                    e.getCause();
                }
            }
        }).start();
    }

    public static void hasNotification(final String theUID , final ImageView myNotification) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db = FirebaseFirestore.getInstance();

                    DocumentReference doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                boolean hasNoti = user.isHasNotification();
                                if (hasNoti)
                                {
                                    myNotification.setVisibility(View.VISIBLE);
                                }else
                                {
                                    myNotification.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                }catch (Exception e)
                {
                    e.getCause();
                }
            }
        }).start();
    }

    public static void blockFun(final Context context , final String hisOriginalUID ,
                                final String theUID , final String hisOriginalName , final String myOriginalName ,
                                final String userNameProfileInfo , final Button follow ,
                                final Button chat , final TextView txtBlock) {

        if (hisOriginalUID.equals(theUID))
        {

        }else
        {
            // add for list Block
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myBlockList = database.getReference(theUID + " MyBlockList").push();
            myBlockList.setValue(new MyBlockModel(hisOriginalName , hisOriginalUID));
            new OurToast().myToast(context, String.format(" %s %s " , userNameProfileInfo , context.getString(R.string.blocked)));
            follow.setEnabled(false);
            chat.setEnabled(false);
            txtBlock.setText(context.getString(R.string.unblock));
            txtBlock.setTextColor(Color.RED);

            //add me to the fucking guy blocksMe List
            DatabaseReference hisBlockList = database.getReference(hisOriginalUID + " HisBlockList").push();
            hisBlockList.setValue(new HisBlockModel(myOriginalName , theUID));

            RemoveFromFollowingAndFollowersListWhenBlock(theUID , hisOriginalUID);

        }
    }

    private static void RemoveFromFollowingAndFollowersListWhenBlock (final String theUID , final String hisOriginalUID)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        com.google.firebase.database.Query applesQuery = ref.child(theUID + " FollowingList").orderByChild("theFollowingDudesUID").equalTo(hisOriginalUID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        com.google.firebase.database.Query apples = ref.child(hisOriginalUID + " FollowersList")
                .orderByChild("theFollowersDudesUID").equalTo(theUID);

        apples.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        com.google.firebase.database.Query applesQuery2 = ref.child(hisOriginalUID + " FollowersList").orderByChild("theFollowingDudesUID").equalTo(theUID);
        applesQuery2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        com.google.firebase.database.Query apples2 = ref.child(theUID + " FollowingList")
                .orderByChild("theFollowersDudesUID").equalTo(hisOriginalUID);

        apples2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}
