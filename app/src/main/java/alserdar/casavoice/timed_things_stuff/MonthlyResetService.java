package alserdar.casavoice.timed_things_stuff;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MonthlyResetService extends Service {
    public MonthlyResetService() {
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
        doc.update("monthlyRecBalance" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                doc.update("monthlySendBalance" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });

        return Service.START_STICKY;
    }
}
