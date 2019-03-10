package alserdar.casavoice.timed_things_stuff;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

import alserdar.casavoice.OurNotificationMessages;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.UserModel;

public class ResetMonthlyService extends Service {
    public ResetMonthlyService() {
    }

    public int counter=0;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    public IBinder onBind(Intent intent) {

        return null ;
    }


    long oldTime=0;
    public void startTimer() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    //set a new Timer
                    timer = new Timer();

                    //initialize the TimerTask's job
                    initializeTimerTask();

                    //schedule the timer, to wake up every 1 second
                    timer.schedule(timerTask, 60000*60*6, 60000); //
                }catch (RuntimeException e )
                {
                    e.getMessage();
                }
            }
        }).start();

    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //  Log.i("in timer", "in timer ++++  "+ (counter++));

                try
                {

                    FirebaseFirestore adminDB = FirebaseFirestore.getInstance();
                    final DocumentReference adminDocs = adminDB.collection("UserInformation").document("9V77DOKQXfNpgDOZSYnp5hAKU663");
                    adminDocs.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                if (user.isRedMe())
                                {
                                    String theUID = DetectUserInfo.theUID(getBaseContext());
                                    final FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
                                    doc.update("monthlyRecBalance" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            doc.update("monthlySendBalance" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    adminDocs.update("redMe" , false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            new OurNotificationMessages().OurNotification(getBaseContext() , "OK Daily");
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }else
                                {
                                    new OurNotificationMessages().OurNotification(getBaseContext() , "Nothing");

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    startTimer();
                }catch (RuntimeException e )
                {
                    e.getMessage();
                }
            }
        }).start();


        return START_NOT_STICKY ;
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }catch (RuntimeException e )
                {
                    e.getMessage();
                }
            }
        }).start();

    }
}
