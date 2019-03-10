package alserdar.casavoice.alert_stuff;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.OurNotificationMessages;
import alserdar.casavoice.R;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.UserModel;

public class ClearMessageService extends Service {
    public ClearMessageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null ;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

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
                    }
                }
            }
        });

        return START_STICKY_COMPATIBILITY ;
    }
}
