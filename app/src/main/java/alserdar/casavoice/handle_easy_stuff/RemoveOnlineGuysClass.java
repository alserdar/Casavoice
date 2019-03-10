package alserdar.casavoice.handle_easy_stuff;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by ALAZIZY on 3/20/2018.
 */

public class RemoveOnlineGuysClass {

    public static void removeGuy(String roomOwnerUID , String theUID)
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child(roomOwnerUID + " Online").orderByChild("onlineDudeUID").equalTo(theUID);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                        appleSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static void removeGuyFromMic(final String roomOwnerUID , String userName , final String whichMic , final String theUID)
    {

        /*
         DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child( roomOwnerUID + " voice").orderByChild("userName").equalTo(userName);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> updateMicOne = new HashMap<>();
                updateMicOne.put("mics", whichMic);
                updateMicOne.put("nickName", "");
                updateMicOne.put("roomId", "");
                updateMicOne.put("status", "off");
                updateMicOne.put("userName", "");
                updateMicOne.put("theUri", "");
                updateMicOne.put("userUID", "");

                FirebaseDatabase databaseBroadCast = FirebaseDatabase.getInstance();
                DatabaseReference refBroadcast = databaseBroadCast.getReference(roomOwnerUID + " voice").child(whichMic);
                refBroadcast.updateChildren(updateMicOne);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
         */

    }
}
