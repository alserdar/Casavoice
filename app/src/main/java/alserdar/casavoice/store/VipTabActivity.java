package alserdar.casavoice.store;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.models.UserModel;
import alserdar.casavoice.timed_things_stuff.CheckTime;

public class VipTabActivity extends AppCompatActivity {

    private static int balance ;
    private static String theVipStatus;
    private static int theLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip_tab);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        theBalance(getBaseContext());
        vipStatus(getBaseContext());
        Levels(getBaseContext());
        CheckTime.checkTimeReality(getBaseContext());

        String theUID = DetectUserInfo.theUID(getBaseContext());

        final TextView infoVIP = findViewById(R.id.infoAboutYourVip);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    UserModel model = documentSnapshot.toObject(UserModel.class);

                    if (model.getEndVip() == null)
                    {

                    }else
                    {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy" , Locale.getDefault());

                        infoVIP.setText(String.format("%s %s %s %s", getString(R.string.you_are), model.getVip(), getString(R.string.until), sdf.format(model.getEndVip())));
                    }
                }
            }
        });



        TabLayout tabLayout = findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.getTabAt(0).setIcon(R.mipmap.vip_diamond);
            tabLayout.getTabAt(1).setIcon(R.mipmap.vip_platinum);
            tabLayout.getTabAt(2).setIcon(R.mipmap.vip_golden);
            tabLayout.getTabAt(3).setIcon(R.mipmap.vip_silver);
            tabLayout.getTabAt(4).setIcon(R.mipmap.vip_pronze);
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position)
            {
                case 0 : return LegendaryVIP.newInstance(position + 1);

                case 1 : return RoyalVIP.newInstance(position + 1);

                case 2 : return GoldenVIP.newInstance(position + 1);

                case 3 : return SilverVIP.newInstance(position + 1);

                case 4 : return BronzeVIP.newInstance(position + 1);

            }

            return null ;

        }

        @Override
        public int getCount() {
            return 5;
        }

    }


    public static class LegendaryVIP extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private FirebaseFirestore db ;
        private DocumentReference doc;
        private String theUID;

        public LegendaryVIP() {
        }

        public static LegendaryVIP newInstance(int sectionNumber) {
            LegendaryVIP fragment = new LegendaryVIP();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.legendary_lay, container, false);
            theUID = DetectUserInfo.theUID(rootView.getContext());
            Button ok = rootView.findViewById(R.id.okLegendary);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    theBalance(rootView.getContext());
                    vipStatus(rootView.getContext());
                    Levels(rootView.getContext());

                    db = FirebaseFirestore.getInstance();
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                if (CheckTime.checkTimeReality(rootView.getContext()))
                                {
                                    UserModel user = documentSnapshot.toObject(UserModel.class);
                                    boolean hasVip = user.isHasVip();
                                    if (hasVip)
                                    {
                                        new OurToast().myToast(getContext() , getString(R.string.wait_until_your_vip_finished));
                                    }else
                                    {
                                        if (balance >= 49999)
                                        {
                                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                            alertDialog.setTitle(getString(R.string.legendary_vip));
                                            // alertDialog.setMessage("Move From " + theVipStatus + " to Legendary ?");
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            db = FirebaseFirestore.getInstance();
                                                            doc = db.collection("UserInformation").document(theUID);
                                                            doc.update("vip", "LegendaryVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    final int senderNewBalance = (balance - 49999) ;
                                                                    doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            new OurToast().myToast(getContext(), getString(R.string.you_are_legendary_now));
                                                                            doc.update("startVip", new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Date date = new Date();
                                                                                    Calendar c = Calendar.getInstance();
                                                                                    c.setTime(date);
                                                                                    c.add(Calendar.MONTH, 1);
                                                                                    date = c.getTime();
                                                                                    doc.update("endVip", date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            doc.update("hasVip", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

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

                                                            doc.update("level", theLevel + 10).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    new OurToast().myToast(getContext(), getString(R.string.level_up_to_10));

                                                                }
                                                            });
                                                        }
                                                    });
                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            alertDialog.show();
                                            alertDialog.getWindow().getDecorView().getBackground().
                                                    setColorFilter(new LightingColorFilter(0xFF000000, ContextCompat.getColor(getContext(),
                                                            R.color.colorPrimary)));
                                        }else
                                        {
                                            new OurToast().myToast(getContext() , getString(R.string.rechrge_your_balance));
                                        }
                                    }
                                }else
                                {
                                    new OurToast().myToast(rootView.getContext() , getString(R.string.check_your_time));
                                }

                            }else
                            {

                            }
                        }
                    });
                }
            });
            return rootView;
        }
    }

    public static class RoyalVIP extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        private FirebaseFirestore db ;
        private DocumentReference doc;
        private String theUID;

        public RoyalVIP() {
        }

        public static RoyalVIP newInstance(int sectionNumber) {
            RoyalVIP fragment = new RoyalVIP();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.royal_lay, container, false);
            theUID = DetectUserInfo.theUID(rootView.getContext());
            Button ok = rootView.findViewById(R.id.okRoyal);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    theBalance(rootView.getContext());
                    vipStatus(rootView.getContext());
                    Levels(rootView.getContext());


                    db = FirebaseFirestore.getInstance();
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                if (CheckTime.checkTimeReality(rootView.getContext()))
                                {
                                    UserModel user = documentSnapshot.toObject(UserModel.class);

                                    boolean hasVip = user.isHasVip();
                                    if (hasVip)
                                    {
                                        new OurToast().myToast(getContext() , getString(R.string.wait_until_your_vip_finished));
                                    }else
                                    {
                                        if (balance >= 39999)
                                        {
                                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                            alertDialog.setTitle(getString(R.string.royal_vip));
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            db = FirebaseFirestore.getInstance();
                                                            doc = db.collection("UserInformation").document(theUID);
                                                            doc.update("vip", "RoyalVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    final int senderNewBalance = (balance - 39999) ;
                                                                    doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            new OurToast().myToast(getContext(), getString(R.string.you_are_royal_now));
                                                                            doc.update("startVip", new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Date date = new Date();
                                                                                    Calendar c = Calendar.getInstance();
                                                                                    c.setTime(date);
                                                                                    c.add(Calendar.MONTH, 1);
                                                                                    date = c.getTime();
                                                                                    doc.update("endVip", date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            doc.update("hasVip", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

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

                                                            doc.update("level", theLevel + 5).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    new OurToast().myToast(getContext(), getString(R.string.level_up_to_5));
                                                                }
                                                            });
                                                        }
                                                    });
                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            alertDialog.show();
                                            alertDialog.getWindow().getDecorView().getBackground().
                                                    setColorFilter(new LightingColorFilter(0xFF000000, ContextCompat.getColor(getContext(),
                                                            R.color.ourSliver)));
                                        }else
                                        {
                                            new OurToast().myToast(getContext() , getString(R.string.rechrge_your_balance));
                                        }
                                    }
                                }else
                                {
                                    new OurToast().myToast(rootView.getContext() , getString(R.string.check_your_time));
                                }

                            }else
                            {

                            }


                        }
                    });


                }
            });

            return rootView;
        }
    }

    public static class GoldenVIP extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private String theUID;
        FirebaseFirestore db ;
        DocumentReference doc ;

        public GoldenVIP() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static GoldenVIP newInstance(int sectionNumber) {
            GoldenVIP fragment = new GoldenVIP();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.golden_lay, container, false);
            theUID = DetectUserInfo.theUID(rootView.getContext());
            Button ok = rootView.findViewById(R.id.okGolden);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    theBalance(rootView.getContext());
                    vipStatus(rootView.getContext());
                    db = FirebaseFirestore.getInstance();
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                if (CheckTime.checkTimeReality(rootView.getContext()))
                                {
                                    UserModel user = documentSnapshot.toObject(UserModel.class);

                                    boolean hasVip = user.isHasVip();
                                    if (hasVip)
                                    {
                                        new OurToast().myToast(getContext() , getString(R.string.wait_until_your_vip_finished));
                                    }else
                                    {
                                        if (balance >= 29999)
                                        {
                                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                            alertDialog.setTitle(getString(R.string.golden_vip));
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            db = FirebaseFirestore.getInstance();
                                                            doc = db.collection("UserInformation").document(theUID);
                                                            doc.update("vip", "GoldenVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    final int senderNewBalance = (balance - 29999) ;
                                                                    doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            new OurToast().myToast(getContext(), getString(R.string.you_are_golden_now));
                                                                            doc.update("startVip", new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Date date = new Date();
                                                                                    Calendar c = Calendar.getInstance();
                                                                                    c.setTime(date);
                                                                                    c.add(Calendar.MONTH, 1);
                                                                                    date = c.getTime();
                                                                                    doc.update("endVip", date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            doc.update("hasVip", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

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
                                                    });
                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            alertDialog.show();
                                            alertDialog.getWindow().getDecorView().getBackground().
                                                    setColorFilter(new LightingColorFilter(0xFF000000, ContextCompat.getColor(getContext(),
                                                            R.color.ourYellow)));
                                        }else
                                        {
                                            new OurToast().myToast(getContext() , getString(R.string.rechrge_your_balance));
                                        }
                                    }
                                }else
                                {
                                    new OurToast().myToast(rootView.getContext() , getString(R.string.check_your_time));
                                }

                            }else
                            {

                            }


                        }
                    });

                }
            });
            return rootView;
        }
    }

    public static class SilverVIP extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        String theUID;
        FirebaseFirestore db ;
        DocumentReference doc ;


        public SilverVIP() {
        }

        public static SilverVIP newInstance(int sectionNumber) {
            SilverVIP fragment = new SilverVIP();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.silver_lay, container, false);
            Button ok = rootView.findViewById(R.id.okSilver);
            theUID = DetectUserInfo.theUID(rootView.getContext());
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    theBalance(rootView.getContext());
                    vipStatus(rootView.getContext());

                    db = FirebaseFirestore.getInstance();
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                if (CheckTime.checkTimeReality(rootView.getContext()))

                                {
                                    UserModel user = documentSnapshot.toObject(UserModel.class);

                                    boolean hasVip = user.isHasVip();
                                    if (hasVip)
                                    {
                                        new OurToast().myToast(getContext() , getString(R.string.wait_until_your_vip_finished));
                                    }else
                                    {

                                        if (balance >= 19999)
                                        {
                                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                            alertDialog.setTitle(getString(R.string.silver_vip));
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            db = FirebaseFirestore.getInstance();
                                                            doc = db.collection("UserInformation").document(theUID);
                                                            doc.update("vip", "SilverVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    final int senderNewBalance = (balance - 19999) ;
                                                                    doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            new OurToast().myToast(getContext(), getString(R.string.you_are_sliver_now));
                                                                            doc.update("startVip", new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Date date = new Date();
                                                                                    Calendar c = Calendar.getInstance();
                                                                                    c.setTime(date);
                                                                                    c.add(Calendar.MONTH, 1);
                                                                                    date = c.getTime();
                                                                                    doc.update("endVip", date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            doc.update("hasVip", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

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
                                                    });
                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            alertDialog.show();
                                            alertDialog.getWindow().getDecorView().getBackground().
                                                    setColorFilter(new LightingColorFilter(0xFF000000, ContextCompat.getColor(getContext(),
                                                            R.color.ourSliver)));
                                        }else
                                        {
                                            new OurToast().myToast(getContext() , getString(R.string.rechrge_your_balance));
                                        }
                                    }
                                }else
                                {
                                    new OurToast().myToast(rootView.getContext() , getString(R.string.check_your_time));
                                }

                            }else
                            {

                            }


                        }
                    });

                }
            });
            return rootView;
        }
    }

    public static class BronzeVIP extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        FirebaseFirestore db ;
        private DocumentReference doc;
        private String theUID;

        public BronzeVIP() {
        }

        public static BronzeVIP newInstance(int sectionNumber) {
            BronzeVIP fragment = new BronzeVIP();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.pronze_lay, container, false);
            theUID = DetectUserInfo.theUID(rootView.getContext());
            Button ok = rootView.findViewById(R.id.okBronze);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    theBalance(rootView.getContext());
                    vipStatus(rootView.getContext());

                    db = FirebaseFirestore.getInstance();
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                if (CheckTime.checkTimeReality(rootView.getContext()))

                                {
                                    UserModel user = documentSnapshot.toObject(UserModel.class);

                                    boolean hasVip = user.isHasVip();
                                    if (hasVip)
                                    {
                                        new OurToast().myToast(getContext() , getString(R.string.wait_until_your_vip_finished));
                                    }else
                                    {
                                        if (balance >= 4999)
                                        {
                                            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                            alertDialog.setTitle(getString(R.string.bronze_vip));
                                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok),
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            db = FirebaseFirestore.getInstance();
                                                            doc = db.collection("UserInformation").document(theUID);
                                                            doc.update("vip", "RegularVIP").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    final int senderNewBalance = (balance - 4999) ;
                                                                    doc.update("balanceOfCoins" , senderNewBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            new OurToast().myToast(getContext(), getString(R.string.you_are_pronze_now));
                                                                            doc.update("startVip", new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {
                                                                                    Date date = new Date();
                                                                                    Calendar c = Calendar.getInstance();
                                                                                    c.setTime(date);
                                                                                    c.add(Calendar.MONTH, 1);
                                                                                    date = c.getTime();
                                                                                    doc.update("endVip", date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            doc.update("hasVip", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

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
                                                    });
                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                            alertDialog.show();
                                            alertDialog.getWindow().getDecorView().getBackground().
                                                    setColorFilter(new LightingColorFilter(0xFF000000, ContextCompat.getColor(getContext(),
                                                            R.color.ourPronze)));
                                        }else
                                        {
                                            new OurToast().myToast(getContext() , getString(R.string.rechrge_your_balance));
                                        }
                                    }
                                }else
                                {
                                    new OurToast().myToast(rootView.getContext() , getString(R.string.check_your_time));
                                }

                            }else
                            {

                            }


                        }
                    });

                }
            });
            return rootView;
        }
    }

    private static int theBalance(Context context)
    {
        String theUID = DetectUserInfo.theUID(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
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

    private static String vipStatus(Context context)
    {
        String theUID = DetectUserInfo.theUID(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);

                    theVipStatus = user.getVip();
                }else
                {

                }


            }
        });

        return theVipStatus ;
    }

    private static int Levels(Context context)
    {
        String theUID = DetectUserInfo.theUID(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);

                    theLevel = user.getLevel();

                }else
                {

                }
            }
        });

        return theLevel ;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext() , Store.class);
        startActivity(i);
        finish();
    }
}
