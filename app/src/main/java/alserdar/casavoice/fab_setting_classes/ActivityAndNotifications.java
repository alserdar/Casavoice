package alserdar.casavoice.fab_setting_classes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import alserdar.casavoice.R;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.NotificationsModel;

public class ActivityAndNotifications extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_and_notifications);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0 :return NotificationsTheTwo.newInstance(position + 1);

             //   case 1 :return ActivityTheOne.newInstance(position + 1);

            }

            return null ;

        }

        @Override
        public int getCount() {
            return 1;
        }
    }
    public static class ActivityTheOne extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public ActivityTheOne() {
        }


        public static ActivityTheOne newInstance(int sectionNumber) {
            ActivityTheOne fragment = new ActivityTheOne();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_activity, container, false);
            return rootView;
        }
    }


    public static class NotificationsTheTwo extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public NotificationsTheTwo() {
        }


        public static NotificationsTheTwo newInstance(int sectionNumber) {
            NotificationsTheTwo fragment = new NotificationsTheTwo();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);

            ListView listNotifications = rootView.findViewById(R.id.listNotifications);
            String theUID = DetectUserInfo.theUID(getContext());

            FirebaseListAdapter<NotificationsModel> adapter =
                    new FirebaseListAdapter<NotificationsModel>(getContext(), NotificationsModel.class, R.layout.single_text_of_list_notifications,
                            FirebaseDatabase.getInstance().getReference(theUID + " Notifications")) {
                        @Override
                        public NotificationsModel getItem(int position) {
                            return super.getItem(super.getCount() - position - 1);

                        }

                        @Override
                        protected void populateView(View v, final NotificationsModel model, int position) {

                            TextView notificationsText = v.findViewById(R.id.single_text_of_notifications);
                            ImageView justPic = v.findViewById(R.id.justPicInNotification);
                            notificationsText.setText(String.format(" %s " , model.getSingleNotifications()));


                            switch (model.getJustPic())
                            {
                                case "Ferrari":
                                    justPic.setBackgroundResource(R.mipmap.ferrari);
                                    break;
                                case "re7a" :
                                    justPic.setBackgroundResource(R.mipmap.re7a);
                                    break;
                                case "Negma" :
                                    justPic.setBackgroundResource(R.mipmap.ngma);
                                    break;
                                case "Bugatti" :
                                    justPic.setBackgroundResource(R.mipmap.bugatti);
                                    break;
                                case "Farawla" :
                                    justPic.setBackgroundResource(R.mipmap.farawla);
                                    break;
                                case "Coctail" :
                                    justPic.setBackgroundResource(R.mipmap.coctail);
                                    break;
                                case "Camaro" :
                                    justPic.setBackgroundResource(R.mipmap.camaro);
                                    break;
                                case "Sabeka" :
                                    justPic.setBackgroundResource(R.mipmap.sabeka);
                                    break;
                                case "Gawhara" :
                                    justPic.setBackgroundResource(R.mipmap.gawhara);
                                    break;
                                case "Jeep" :
                                    justPic.setBackgroundResource(R.mipmap.jeep);
                                    break;
                                case "Bosa" :
                                    justPic.setBackgroundResource(R.mipmap.bosa);
                                    break;
                                case "Warda" :
                                    justPic.setBackgroundResource(R.mipmap.warda);
                                    break;
                                case "yacht" :
                                    justPic.setBackgroundResource(R.mipmap.yacht);
                                    break;
                                case "Tag" :
                                    justPic.setBackgroundResource(R.mipmap.tagg);
                                    break;
                                case "Iphone" :
                                    justPic.setBackgroundResource(R.mipmap.iphones);
                                    break;
                                case "Torta" :
                                    justPic.setBackgroundResource(R.mipmap.torta);
                                    break;
                                case "Chocolata" :
                                    justPic.setBackgroundResource(R.mipmap.chocalata);
                                    break;
                                case "loved" :
                                    justPic.setBackgroundResource(R.mipmap.heart);
                                    break;

                            }

                        }
                    };

            listNotifications.setAdapter(adapter);
            return rootView;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
