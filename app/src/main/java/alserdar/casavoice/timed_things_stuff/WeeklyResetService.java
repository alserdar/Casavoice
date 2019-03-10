package alserdar.casavoice.timed_things_stuff;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class WeeklyResetService extends Service {
    public WeeklyResetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null ;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String facebookUserName = getInfo.getString("FacebookUser", "FacebookUser");


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("UserInformation").document(facebookUserName);
        doc.update("weeklyRecBalance" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                doc.update("weeklySendBalance" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });

        return Service.START_STICKY;
    }
}
