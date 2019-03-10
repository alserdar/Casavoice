package alserdar.casavoice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import alserdar.casavoice.animate.UpAndDown;
import alserdar.casavoice.fab_setting_classes.SettingsActivity;
import alserdar.casavoice.fab_setting_classes.YourProfile;
import alserdar.casavoice.fab_setting_classes.spin_wheels.SpinWheels;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadBaseCode;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.FaveRoomsModel;
import alserdar.casavoice.models.RoomModel;
import alserdar.casavoice.models.UserModel;
import alserdar.casavoice.store.Store;
import alserdar.casavoice.top_menu_shit.DailyRecListAdapter;
import alserdar.casavoice.top_menu_shit.DailySenderListAdapter;
import alserdar.casavoice.top_menu_shit.MonthlyRecListAdapter;
import alserdar.casavoice.top_menu_shit.MonthlySenderListAdapter;
import alserdar.casavoice.top_menu_shit.WeeklyRecListAdapter;
import alserdar.casavoice.top_menu_shit.WeeklySenderListAdapter;

public class Home extends AppCompatActivity {

    LinearLayout set ;
    TextView homeUsername ;
    ImageView profilePic , myNotification , newMessages , newSpinno;
    private String theUID;
    Button storeButton , messageButton , activityButton , profileButton , earnGoldButton , settingsButton;
    FirebaseFirestore db ;
    DocumentReference doc ;



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
        /*
         Intent iMessage = new Intent(getBaseContext() , YouHaveNewMessageBroadcast.class);
        sendBroadcast(iMessage);

        Intent iNotification = new Intent(getBaseContext() , YouHaveNewMessageBroadcast.class);
        sendBroadcast(iNotification);

         */

       // new YouHaveNewNotificationBroadcast().gotNotifications(Home.this);
      //  new ResetDailyAndHappinessBroadcast().gotChanged(Home.this);
      //  new ResetWeeklyBroadcast().gotChanged(Home.this);
      //  new ResetMonthlyBroadcast().gotChanged(Home.this);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        }
        theUID = DetectUserInfo.theUID(getBaseContext());
        homeUsername = findViewById(R.id.userNameForHome);
        profilePic = findViewById(R.id.profilePicForHome);


        LoadBaseCode.loadInfoForHome(getBaseContext() , theUID , profilePic , homeUsername);

        LoadBaseCode.timeCalculated(getBaseContext() , theUID);

        homeUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , YourProfile.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getCause();
                        }
                    }
                }).start();
            }
        });



        FloatingActionButton fab = findViewById(R.id.fabSetting);
        Button search = findViewById(R.id.searchUsers);
        myNotification = findViewById(R.id.newNotification);
        newMessages = findViewById(R.id.newMessages);
        newSpinno = findViewById(R.id.newSpinno);
        set = findViewById(R.id.settingLayout);
        storeButton = findViewById(R.id.setting_store);
        profileButton = findViewById(R.id.setting_profile);
        activityButton = findViewById(R.id.setting_activity);
        messageButton = findViewById(R.id.message);
        earnGoldButton = findViewById(R.id.earnGold);
        settingsButton = findViewById(R.id.settings);



        myNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadBaseCode.MyNotificationHome(getBaseContext() , theUID);
            }
        });

        newMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadBaseCode.myNewMessage(getBaseContext() , theUID);
            }
        });

        newSpinno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadBaseCode.myNewSpinno(getBaseContext() , theUID);
            }
        });

        earnGoldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , SpinWheels.class);
                            startActivity(i);

                        }catch (Exception e)
                        {
                            e.getCause();
                        }
                    }
                }).start();
            }
        });

        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , Store.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getCause();
                        }
                    }
                }).start();
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , YourProfile.class);
                            startActivity(i);

                        }catch (Exception e)
                        {
                            e.getCause();
                        }
                    }
                }).start();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadBaseCode.myNewMessage(getBaseContext() , theUID);
            }
        });


        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                LoadBaseCode.MyNotificationHome(getBaseContext() , theUID);
            }
        });


        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Intent i = new Intent(getBaseContext() , SettingsActivity.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getCause();
                        }
                    }
                }).start();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                         try {
                            Intent i = new Intent(getBaseContext() , SearchActivity.class);
                            startActivity(i);

                         }catch (Exception e)
                        {
                            e.getCause();
                        }


                    }
                }).start();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                if (set.getVisibility() == View.GONE)
                {
                   new UpAndDown().showTheLay(set);
                }else if (set.getVisibility() == View.VISIBLE)
                {
                    new UpAndDown().hideTheLay(set);
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
                                        set.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    };
                    timer.start();
                }
            }
        });

        LoadBaseCode.hasMessages(theUID , newMessages);
        LoadBaseCode.hasNotification(theUID , myNotification);
        LoadBaseCode.hasSpinOrNo(theUID , newSpinno);


    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            AllHome home = new AllHome();
            Room_Home roomHome = new Room_Home();
            Top_Home topHome = new Top_Home();

            switch (position)
            {
                case 0 :return home;
                case 1 :return roomHome;
                case 2 :return topHome;
            }

            return  null ;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.rooms);
                case 1:
                    return getString(R.string.favoutire);
                case 2:
                    return getString(R.string.top);
            }
            return null;
        }
    }

    // ============== all Home ====================

    public static class AllHome extends Fragment {

        LinearLayout lastRoomCreated ;
        TextView roomName , infoAboutRoom , emptyShit ;
        ImageView roomPictureView , peopleInTheRoom ;
        FirebaseFirestore db ;


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);



            lastRoomCreated = rootView.findViewById(R.id.lastRoomCreatedLayout);

            roomName = rootView.findViewById(R.id.roomNameTextView);
            infoAboutRoom = rootView.findViewById(R.id.infoAboutRoom);
            peopleInTheRoom = rootView.findViewById(R.id.peopleInTheRoom);
            emptyShit = rootView.findViewById(R.id.stillEmpty);
            roomPictureView = rootView.findViewById(R.id.roomProfilePictureListAdapter);
            final ListView listAllRooms = rootView.findViewById(R.id.listRooms);

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
                            final RoomsListAdapter adapter = new RoomsListAdapter(getContext() , listRooms);
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
            return rootView;
        }

    }


    //======================= room home =========================

    public static class Room_Home extends Fragment {


        Button createRoom ;
        LinearLayout myRoomLayout , createRoomLayout , faveRoomLayout;
        TextView myRoomName , infoAboutRoom  , emptyShit ;
        ImageView roomPictureView , peopleInTheRoom;
        String facebookUserName  , theUID , nickName;
        private DocumentReference doc;
        private FirebaseFirestore db;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {



            final View rootView = inflater.inflate(R.layout.my_room_home, container, false) ;

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
                soundIds[0] = sp.load(rootView.getContext(), R.raw.ring, 1);
            }else
            {
                sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
                soundIds = new int[10];
                soundIds[0] = sp.load(rootView.getContext(), R.raw.ring, 1);

            }

            try {

                facebookUserName = DetectUserInfo.faceBookUser(getContext());
                theUID = DetectUserInfo.theUID(getContext());
                nickName = DetectUserInfo.myUserName(getContext());
                faveRoomLayout = rootView.findViewById(R.id.favoriteRoomLayout);
                ListView myFaveRooms = rootView.findViewById(R.id.myFavoriteRoomsList);



                FirebaseListAdapter<FaveRoomsModel> adapter = new FirebaseListAdapter<FaveRoomsModel>(getContext(), FaveRoomsModel.class, R.layout.list_of_fave_rooms,
                        FirebaseDatabase.getInstance().getReference(theUID + " FaveRooms")) {


                    @Override
                    protected void populateView(final View v, final FaveRoomsModel model, final int position) {

                        final ImageView ppForFaveRooms = v.findViewById(R.id.faveRoomProfilePicture);
                        final TextView faveRoomName = v.findViewById(R.id.faveRoomNameTextView);
                        final TextView faveRoomInfo = v.findViewById(R.id.faveInfoAboutRoom);
                        final ImageView faveRoomPeople = v.findViewById(R.id.favePeopleInTheRoom);
                        final TextView faveRoomOwner = v.findViewById(R.id.faveStillEmpty);

                        LoadingPics.loadPicForRooms(v.getContext().getApplicationContext() , model.getFaveRoomOwnerUID() , ppForFaveRooms);

                        LoadBaseCode.loadFaveRooms(model.getFaveRoomOwnerUID() , faveRoomName
                                , faveRoomInfo , faveRoomOwner , faveRoomPeople);

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {

                                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                db = FirebaseFirestore.getInstance();
                                doc = db.collection("RoomInformation").document(model.getFaveRoomOwnerUID());
                                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists())
                                        {
                                            final RoomModel room = documentSnapshot.toObject(RoomModel.class);

                                            if (room.isRoomLocked())
                                            {

                                                final EditText thePassword ;
                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                                                alertDialogBuilder .setIcon(R.mipmap.lock);
                                                alertDialogBuilder.setTitle(R.string.type_password);
                                                thePassword = new EditText(v.getContext());
                                                thePassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                alertDialogBuilder.setView(thePassword);
                                                alertDialogBuilder.setPositiveButton(R.string.ok,
                                                            new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface arg0, int arg1)
                                                            {
                                                                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                                                if (thePassword.getText().toString().trim().equals(""))
                                                                {

                                                                }else
                                                                {
                                                                    if (room.getPass() == Long.parseLong(thePassword.getText().toString()))
                                                                    {

                                                                        RoomsListAdapter.isHeAlreadyOnline(model.getFaveRoomOwnerUID() , theUID ,
                                                                                facebookUserName , nickName);
                                                                        Intent enterRoom = new Intent(v.getContext() , LiveRoom.class);
                                                                        enterRoom.putExtra("roomIdInTheRoom" , model.getFaveRoomId());
                                                                        enterRoom.putExtra("roomNameInTheRoom" , model.getFaveRoomName());
                                                                        enterRoom.putExtra("ownerRoom" , model.getFaveRoomOwner());
                                                                        enterRoom.putExtra("roomOwnerUID" , model.getFaveRoomOwnerUID());
                                                                        enterRoom.putExtra("onlineGuys" , facebookUserName);
                                                                        enterRoom.putExtra("onlineGuysUID" , theUID);
                                                                        startActivity(enterRoom);
                                                                        getActivity().finish();
                                                                    }else
                                                                    {
                                                                        new OurToast().myToast(v.getContext(), mContext.getString(R.string.wrong_password));
                                                                    }
                                                                }
                                                            }
                                                        });
                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                alertDialog.show();
                                            }else
                                            {
                                                RoomsListAdapter.isHeAlreadyOnline(model.getFaveRoomOwnerUID() , theUID ,
                                                        facebookUserName , nickName);

                                                Intent enterRoom = new Intent(getContext() , LiveRoom.class);
                                                enterRoom.putExtra("roomIdInTheRoom" , model.getFaveRoomId());
                                                enterRoom.putExtra("roomNameInTheRoom" , model.getFaveRoomName());
                                                enterRoom.putExtra("ownerRoom" , model.getFaveRoomOwner());
                                                enterRoom.putExtra("onlineGuys" , facebookUserName);
                                                enterRoom.putExtra("onlineGuysUID" , theUID);
                                                enterRoom.putExtra("roomOwnerUID" , model.getFaveRoomOwnerUID());
                                                startActivity(enterRoom);
                                                getActivity().finish();
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }
                };

                myFaveRooms.setAdapter(adapter);

                createRoom = rootView.findViewById(R.id.createYourRoom);
                myRoomLayout = rootView.findViewById(R.id.myRoomLayout);
                createRoomLayout = rootView.findViewById(R.id.createRoomLayout);
                myRoomName = rootView.findViewById(R.id.myRoomNameTextView);
                infoAboutRoom = rootView.findViewById(R.id.myInfoAboutRoom);
                peopleInTheRoom = rootView.findViewById(R.id.myPeopleInTheRoom);
                emptyShit = rootView.findViewById(R.id.myStillEmpty);
                roomPictureView = rootView.findViewById(R.id.myRoomProfilePicture);

                LoadBaseCode.LoadInfoHasRoomOrNot(theUID , rootView.getContext()  , roomPictureView , createRoomLayout , myRoomLayout);

                db  = FirebaseFirestore.getInstance() ;
                doc = db.collection("RoomInformation").document(theUID);
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists())
                        {
                            final RoomModel roomModel = documentSnapshot.toObject(RoomModel.class);

                            db  = FirebaseFirestore.getInstance() ;
                            doc = db.collection("UserInformation").document(roomModel.getRoomOwnerUID());
                            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    UserModel user = documentSnapshot.toObject(UserModel.class);
                                    String nickName = user.getUserName();
                                    myRoomName.setText(roomModel.getRoomName());
                                    infoAboutRoom.setText(roomModel.getInfoAboutRoom());
                                    emptyShit.setText(nickName);

                                }
                            });


                            myRoomLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                    LoadBaseCode.clickMyRoomLayout(rootView.getContext() , roomModel.getRoomOwnerUID()
                                    ,facebookUserName , theUID , nickName ,roomModel.getRoomId() ,
                                            roomModel.getRoomName() , roomModel.getRoomOwner());

                                }
                            });

                        }
                        createRoom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sp.play(soundIds[0], 1, 1, 1, 0, 1);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent(getContext() , CreatingRoom.class);
                                        startActivity(i);
                                    }
                                }).start();
                            }
                        });
                    }
                });
            }catch (NullPointerException e)
            {
                e.getMessage();
            }

            return rootView;
        }


    }



    // ================== top home ====================


    public static class Top_Home extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Top_Home() {
        }

        public static Top_Home newInstance(int sectionNumber) {
            Top_Home fragment = new Top_Home();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.top_home_container, container, false);

            TopPagerAdapter mSectionsPagerAdapter = new TopPagerAdapter(getActivity().getSupportFragmentManager() , getContext());

            ViewPager mViewPager = rootView.findViewById(R.id.top_container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = rootView.findViewById(R.id.top_tabs);
            if (tabLayout != null) {
                tabLayout.setupWithViewPager(mViewPager);
                tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            }

            return rootView;
        }
    }

    private static class TopPagerAdapter extends FragmentPagerAdapter {

        Context context ;

        TopPagerAdapter(FragmentManager fm  , Context mContext) {
            super(fm);
            context = mContext ;
        }

        @Override
        public Fragment getItem(int position) {

            Top_Sender sender = new Top_Sender();
            Top_Receiver rec = new Top_Receiver();

            switch (position) {
                case 0:
                    return sender;
                case 1:
                    return rec;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

           @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return context.getString(R.string.top_sender);
                case 1:
                    return context.getString(R.string.top_receiver);
            }
            return null;
        }


    }


    // class for to sender

    public static class Top_Sender extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

        public Top_Sender() {
        }

        public static Top_Sender newInstance(int sectionNumber) {
            Top_Sender fragment = new Top_Sender();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.top_sender, container, false);


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
                soundIds[0] = sp.load(rootView.getContext(), R.raw.ring, 1);
            }else
            {
                sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
                soundIds = new int[10];
                soundIds[0] = sp.load(rootView.getContext(), R.raw.ring, 1);

            }

            final ListView senderDailyList = rootView.findViewById(R.id.senderDaily);
            final ListView senderWeeklyList = rootView.findViewById(R.id.senderWeekly);
            final ListView senderMonthlyList = rootView.findViewById(R.id.senderMonthly);

            final TextView senderDailyTX = rootView.findViewById(R.id.dailySenderTextView);
            final TextView senderWeeklyTX = rootView.findViewById(R.id.weeklySenderTextView);
            final TextView senderMonthlyTX = rootView.findViewById(R.id.monthlySenderTextView);

            final ImageView topOneDS = rootView.findViewById(R.id.topOneDS);
            final ImageView topTwoDS = rootView.findViewById(R.id.topTwoDS);
            final ImageView topThreeDS = rootView.findViewById(R.id.topThreeDS);

            senderDailyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
            senderWeeklyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
            senderMonthlyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);

            senderDailyTX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.play(soundIds[0], 1, 1, 1, 0, 1);

                    if (senderDailyList.getVisibility() == View.GONE)
                    {
                        senderDailyList.setVisibility(View.VISIBLE);
                        senderDailyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
                    }else
                    {
                        senderDailyList.setVisibility(View.GONE);
                        senderDailyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_row, 0);
                    }
                }
            });

            senderWeeklyTX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.play(soundIds[0], 1, 1, 1, 0, 1);

                    if (senderWeeklyList.getVisibility() == View.GONE)
                    {
                        senderWeeklyList.setVisibility(View.VISIBLE);
                        senderWeeklyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
                    }else
                    {
                        senderWeeklyList.setVisibility(View.GONE);
                        senderWeeklyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_row, 0);
                    }
                }
            });

            senderMonthlyTX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.play(soundIds[0], 1, 1, 1, 0, 1);

                    if (senderMonthlyList.getVisibility() == View.GONE)
                    {
                        senderMonthlyList.setVisibility(View.VISIBLE);
                        senderMonthlyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
                    }else
                    {
                        senderMonthlyList.setVisibility(View.GONE);
                        senderMonthlyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_row, 0);
                    }
                }
            });


            Query query = db.collection("UserInformation")
                    .orderBy("dailySendBalance", Query.Direction.DESCENDING).limit(10);

            query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {

                    if (documentSnapshots.isEmpty())
                    {

                    }else
                    {
                        List<UserModel> listSenderEveryDay = documentSnapshots.toObjects(UserModel.class);
                        final DailySenderListAdapter adapter = new DailySenderListAdapter(getContext() , listSenderEveryDay);
                        senderDailyList.setAdapter(adapter);
                    }
                }
            });

            Query queryWeekly = db.collection("UserInformation").orderBy("weeklySendBalance", Query.Direction.DESCENDING).limit(10);

            queryWeekly.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {

                    if (documentSnapshots.isEmpty())
                    {

                    }else
                    {
                        List<UserModel> listSenderEveryWeek = documentSnapshots.toObjects(UserModel.class);
                        final WeeklySenderListAdapter adapterWeekly = new WeeklySenderListAdapter(getContext() , listSenderEveryWeek);
                        senderWeeklyList.setAdapter(adapterWeekly);
                    }

                }
            });

            Query queryMonthly = db.collection("UserInformation").orderBy("monthlySendBalance", Query.Direction.DESCENDING).limit(10);

            queryMonthly.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {

                    if (documentSnapshots.isEmpty())
                    {

                    }else
                    {
                        List<UserModel> listSenderEveryMonth = documentSnapshots.toObjects(UserModel.class);
                        final MonthlySenderListAdapter adapterMonthly = new MonthlySenderListAdapter(getContext() , listSenderEveryMonth);
                        senderMonthlyList.setAdapter(adapterMonthly);
                    }

                }
            });


            return rootView;
        }
    }

    //class for to Receiver

    public static class Top_Receiver extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private FirebaseFirestore db = FirebaseFirestore.getInstance();

        public Top_Receiver() {
        }

        public static Top_Receiver newInstance(int sectionNumber) {
            Top_Receiver fragment = new Top_Receiver();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.top_receiver, container, false);

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
                soundIds[0] = sp.load(rootView.getContext(), R.raw.ring, 1);
            }else
            {
                sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
                soundIds = new int[10];
                soundIds[0] = sp.load(rootView.getContext(), R.raw.ring, 1);

            }

            final TextView recDailyTX = rootView.findViewById(R.id.recDailyTextView);
            final TextView recWeeklyTX = rootView.findViewById(R.id.recWeeklyTextView);
            final TextView recMonthlyTX = rootView.findViewById(R.id.recMonthlyTextView);

            final ListView recDailyList = rootView.findViewById(R.id.recDaily);
            final ListView recWeeklyList = rootView.findViewById(R.id.recWeekly);
            final ListView recMonthlyList = rootView.findViewById(R.id.recMonthly);


            recDailyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
            recWeeklyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
            recMonthlyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);

            recDailyTX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.play(soundIds[0], 1, 1, 1, 0, 1);

                    if (recDailyList.getVisibility() == View.GONE)
                    {
                        recDailyList.setVisibility(View.VISIBLE);
                        recDailyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
                    }else
                    {
                        recDailyList.setVisibility(View.GONE);
                        recDailyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_row, 0);
                    }
                }
            });

            recWeeklyTX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.play(soundIds[0], 1, 1, 1, 0, 1);

                    if (recWeeklyList.getVisibility() == View.GONE)
                    {
                        recWeeklyList.setVisibility(View.VISIBLE);
                        recWeeklyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
                    }else
                    {
                        recWeeklyList.setVisibility(View.GONE);
                        recWeeklyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_row, 0);
                    }
                }
            });

            recMonthlyTX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sp.play(soundIds[0], 1, 1, 1, 0, 1);

                    if (recMonthlyList.getVisibility() == View.GONE)
                    {
                        recMonthlyList.setVisibility(View.VISIBLE);
                        recMonthlyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.down_row, 0);
                    }else
                    {
                        recMonthlyList.setVisibility(View.GONE);
                        recMonthlyTX.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.right_row, 0);
                    }
                }
            });

            Query queryDailyRec = db.collection("UserInformation").orderBy("dailyRecBalance", Query.Direction.DESCENDING).limit(10);

            queryDailyRec.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (documentSnapshots.isEmpty())
                    {

                    }else
                    {
                        List<UserModel> listRecEveryDay = documentSnapshots.toObjects(UserModel.class);
                        final DailyRecListAdapter adapter = new DailyRecListAdapter(getContext() , listRecEveryDay);
                        recDailyList.setAdapter(adapter);
                    }

                }
            });

            Query queryWeekly = db.collection("UserInformation").orderBy("weeklyRecBalance", Query.Direction.DESCENDING).limit(10);

            queryWeekly.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (documentSnapshots.isEmpty())
                    {

                    }else
                    {
                        List<UserModel> listRecEveryWeek = documentSnapshots.toObjects(UserModel.class);
                        final WeeklyRecListAdapter adapterWeekly = new WeeklyRecListAdapter(getContext() , listRecEveryWeek);
                        recWeeklyList.setAdapter(adapterWeekly);
                    }

                }
            });

            Query queryMonthly = db.collection("UserInformation").orderBy("monthlyRecBalance", Query.Direction.DESCENDING).limit(10);

            queryMonthly.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    if (documentSnapshots.isEmpty())
                    {

                    }else
                    {
                        List<UserModel> listRecEveryMonth = documentSnapshots.toObjects(UserModel.class);
                        final MonthlyRecListAdapter adapterMonthly = new MonthlyRecListAdapter(getContext() , listRecEveryMonth);
                        recMonthlyList.setAdapter(adapterMonthly);
                    }

                }
            });


            return rootView;
        }

    }


    /*
     private void loadInfoForHome (final String theUID)
    {
        try {
            LoadingPics.loadPicsForHome(getApplicationContext() , theUID , profilePic);
            db = FirebaseFirestore.getInstance();
            doc = db.collection("UserInformation").document(theUID);
            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists())
                    {
                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        if (user.isUserSus())
                        {
                            new OurToast().myToast(getBaseContext() , getString(R.string.you_are_suspended));
                            finish();
                        }else
                        {
                            if (DetectUserInfo.myUserName(getBaseContext()).equals("myUserName"))
                            {
                                SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(Home.this);
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
     */




    boolean doubleBackToExitPressedOnce = false ;
    @Override
    public void onBackPressed() {



         if (set.getVisibility() == View.VISIBLE)
        {
            new UpAndDown().hideTheLay(set);
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
                                set.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            };
            timer.start();
        }else if (set.getVisibility() == View.GONE)
        {
            if (doubleBackToExitPressedOnce) {

                super.onBackPressed();
                return;
            }
            new OurToast().myToast(getBaseContext() , getString(R.string.pressBackAgainToExit));
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 30000);
        }
    }

}
