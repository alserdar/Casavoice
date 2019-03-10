package alserdar.casavoice.timed_things_stuff;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

import alserdar.casavoice.OurNotificationMessages;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.models.AppInfoModel;
import alserdar.casavoice.models.UserModel;

public class ResetDailyAndHappinessService extends Service {
    public ResetDailyAndHappinessService() {
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
                    new OurToast().myToast(getBaseContext() , "here We go");
                    adminDocs.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                if (user.isRedMe())
                                {
                                  //  String theUID = DetectUserInfo.theUID(getBaseContext());
                                    final FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    FirebaseDatabase base = FirebaseDatabase.getInstance();
                                    DatabaseReference reference = base.getReference("appInfo");
                                    reference.addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                           for (DataSnapshot ds : dataSnapshot.getChildren())
                                           {
                                               if (ds.exists())
                                               {
                                                   AppInfoModel model = ds.getValue(AppInfoModel.class);
                                                   String theUid = model.getTheUID();
                                                   final DocumentReference doc = db.collection("UserInformation").document(theUid);
                                                   doc.update("dailyRecBalance" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                       @Override
                                                       public void onSuccess(Void aVoid) {
                                                           new OurToast().myToast(getBaseContext() , "rec");
                                                           doc.update("dailySendBalance" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                               @Override
                                                               public void onSuccess(Void aVoid) {
                                                                   new OurToast().myToast(getBaseContext() , "send");
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
                                               }
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
