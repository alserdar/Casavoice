package alserdar.casavoice.timed_things_stuff;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.load_stuff.DetectUserInfo;

public class HappyRoomResetService extends Service {
    public HappyRoomResetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null ;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String theUID = DetectUserInfo.theUID(getBaseContext());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference doc = db.collection("RoomInformation").document(theUID);
        doc.update("happiness" , 0).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });


        return Service.START_STICKY;
    }
}
