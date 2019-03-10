package alserdar.casavoice.alert_stuff;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

import alserdar.casavoice.OurNotificationMessages;
import alserdar.casavoice.R;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.UserModel;

public class YouHaveNewMessageService extends Service {


    public int counter = 0;

    public YouHaveNewMessageService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public YouHaveNewMessageService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("alserdar.casavoice.alert_stuff.YouHaveNewMessageBroadcast");
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 60000, 60000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //  Log.i("in timer", "in timer ++++  "+ (counter++));
                String theUID = DetectUserInfo.theUID(getBaseContext());
                final FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference doc = db.collection("UserInformation").document(theUID);
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists())
                        {
                            boolean newMessage ;
                            UserModel user = documentSnapshot.toObject(UserModel.class);
                            newMessage = user.isHasMessages();
                            if (newMessage)
                            {
                                new OurNotificationMessages().OurNotification(getBaseContext() , getString(R.string.you_have_new_message));
                                stoptimertask();
                            }else
                            {

                            }
                        }
                    }
                });
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


    /*
      public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //  Log.i("in timer", "in timer ++++  "+ (counter++));

                try
                {
                    String theUID = DetectUserInfo.theUID(getBaseContext());
                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists())
                            {
                                boolean newMessage ;
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                newMessage = user.isHasMessages();
                                if (newMessage)
                                {
                                    new OurNotificationMessages().OurNotification(getBaseContext() , getString(R.string.you_have_new_message));
                                    stoptimertask();
                                }
                            }
                        }
                    });
                }catch (RuntimeException e)
                {
                    e.getMessage();
                }


            }
        };
    }
     */