package alserdar.casavoice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Random;

import alserdar.casavoice.alert_stuff.YouHaveNewMessageService;
import alserdar.casavoice.alert_stuff.YouHaveNewNotificationsService;
import alserdar.casavoice.check_internet.NetworkUtil;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.login_package.GetCountryBySimCard;
import alserdar.casavoice.login_package.RegisterForCasavoice;
import alserdar.casavoice.models.UserModel;
import alserdar.casavoice.timed_things_stuff.CheckTime;


public class Launcher extends AppCompatActivity {

    private String TAG = "serdar";
    Intent mServiceIntent;
    private YouHaveNewNotificationsService mSensorServiceNotifications;
    private YouHaveNewMessageService mSensorServiceMessage;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }

    TextView randoomly ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final LinearLayout avlLay = findViewById(R.id.aviLay);

        String thoughts [] = {getString(R.string.richestThoust) ,
                getString(R.string.moreHappinessThouts) ,
        getString(R.string.vip_features) , getString(R.string.get_dream_car) , getString(R.string.creat_your_room) ,
        getString(R.string.make_new_freinds_with_sending_gifts) , getString(R.string.richestThoust) ,
                getString(R.string.enjoy_talking_with_people) ,
        getString(R.string.enjoy_casavoice) , getString(R.string.always_be_on_top_list) ,
        getString(R.string.always_be_famous)};
        randoomly = findViewById(R.id.randomThoughts);
        String randomStr = thoughts[new Random().nextInt(thoughts.length)];

        randoomly.setText(randomStr);

      //  new DailyResetBroadCast().resetTheDaily(Launcher.this);
      //  new WeeklyResetBroadCast().resetTheWeek(Launcher.this);
     //   new MonthlyResetBroadCast().resetTheMonth(Launcher.this);
     //   new HappyRoomResetBroadCast().resetTheHappiness(Launcher.this);

        ctx = this;
        mSensorServiceNotifications = new YouHaveNewNotificationsService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorServiceNotifications.getClass());
        if (!isMyServiceRunning(mSensorServiceNotifications.getClass())) {
            startService(mServiceIntent);
        }

        mSensorServiceMessage = new YouHaveNewMessageService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorServiceMessage.getClass());
        if (!isMyServiceRunning(mSensorServiceMessage.getClass())) {
            startService(mServiceIntent);
        }


        Intent intent = new Intent();
        intent.setAction("alserdar.casavoice.alert_stuff.ClearMessageBroadcast");
        intent.putExtra("gotMessages" , "gotMessages");
        sendBroadcast(intent);


        CheckTime.checkTimeReality(getBaseContext());

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.getMessage();
                } finally {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            if (NetworkUtil.getConnectivityStatusString(getBaseContext()).equals("Internet enabled")) {
                                boolean loggedIn = AccessToken.getCurrentAccessToken() != null;
                                if (loggedIn) {
                                    AVLoadingIndicatorView view = new AVLoadingIndicatorView(getBaseContext());
                                    avlLay.setVisibility(View.VISIBLE);
                                    view.show();

                                    String theUID = DetectUserInfo.theUID(getBaseContext());

                                    if (theUID == null) {
                                        Intent i = new Intent(getBaseContext(), GetCountryBySimCard.class);
                                        startActivity(i);
                                    } else {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        DocumentReference doc = db.collection("UserInformation").document(theUID);
                                        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    UserModel user = documentSnapshot.toObject(UserModel.class);
                                                    String nickName = user.getUserName();
                                                    if (nickName.isEmpty()) {
                                                        Intent i = new Intent(getBaseContext(), RegisterForCasavoice.class);
                                                        startActivity(i);
                                                    } else {

                                                        if (user.isHasVip() || user.isHasRoom())
                                                        {
                                                            if(CheckTime.checkTimeReality(getBaseContext()))
                                                            {
                                                                Intent i = new Intent(getBaseContext(), Home.class);
                                                                startActivity(i);
                                                            }else
                                                            {

                                                                AlertDialog alertDialog = new AlertDialog.Builder(Launcher.this).create();
                                                                alertDialog.setTitle(getString(R.string.alert));
                                                                alertDialog.setIcon(R.mipmap.my_icon);
                                                                alertDialog.setMessage(getString(R.string.turn_on_autoes));
                                                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), 0);

                                                                                dialog.dismiss();
                                                                            }
                                                                        });
                                                                alertDialog.show();
                                                            }
                                                        }else {
                                                            Intent i = new Intent(getBaseContext(), Home.class);
                                                            startActivity(i);
                                                        }
                                                    }
                                                }
                                            }
                                        });

                                    }


                                } else {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Intent i = new Intent(getBaseContext(), GetCountryBySimCard.class);
                                            startActivity(i);
                                        }
                                    }).start();

                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                            Looper.prepare();
                                        new OurToast().myToast(getBaseContext(), getString(R.string.check_internet ));
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        };
        timer.start();

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }
}
