package alserdar.casavoice.alert_stuff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.OurNotificationMessages;
import alserdar.casavoice.R;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.UserModel;

public class ClearMessageBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        gotMessages(context);
    }

    public void gotMessages(final Context context) {
        String theUID = DetectUserInfo.theUID(context);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    boolean newMessage = user.isHasMessages();
                    if (newMessage)
                    {

                        new OurNotificationMessages().OurNotification(context , context.getString(R.string.you_have_new_message));
                    }
                }
            }
        });

    }
}
