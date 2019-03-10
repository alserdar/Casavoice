package alserdar.casavoice.timed_things_stuff;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

import alserdar.casavoice.OurNotificationMessages;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.models.UserModel;

public class ResetDailyAndHappinessBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    gotChanged(context);
                }catch (RuntimeException e )
                {
                    e.getMessage();
                }
            }
        }).start();
    }

    public void gotChanged(final Context context) {

        PendingIntent pendingIntent = createPendingIntent(context);


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
                        new OurToast().myToast(context , "resetDailyGotChanged");


                    }else
                    {
                        new OurNotificationMessages().OurNotification(context , "Nothing");

                    }
                }
            }
        });

    }

    private static void setDaily(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
        }else
        {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);

        }
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, ResetDailyAndHappinessService.class);
        return PendingIntent.getBroadcast(context, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);


    }
}
