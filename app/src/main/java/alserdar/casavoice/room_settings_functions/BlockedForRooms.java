package alserdar.casavoice.room_settings_functions;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.R;
import alserdar.casavoice.models.BlockModelForRooms;
import alserdar.casavoice.models.UserModel;

public class BlockedForRooms extends AppCompatActivity {

    Button unBlockDudeFromRooms ;
    String theUID, roomOwnerUID;
    FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_for_rooms);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final ListView listTheOnline = findViewById(R.id.list_block_users_for_rooms);
        theUID = DetectUserInfo.theUID(getBaseContext());
        roomOwnerUID = this.getIntent().getExtras().getString("roomOwnerUID");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    FirebaseListAdapter<BlockModelForRooms> adapter =
                            new FirebaseListAdapter<BlockModelForRooms>(getBaseContext(), BlockModelForRooms.class, R.layout.list_blocked_for_rooms,
                                    FirebaseDatabase.getInstance().getReference(roomOwnerUID + " BlockedForRooms")) {
                                @Override
                                protected void populateView(View v, final BlockModelForRooms model, int position) {
                                    final TextView blockedGuysForRooms = v.findViewById(R.id.blockedDudesForRooms);

                                    DocumentReference doc = db.collection("UserInformation").document(model.getBlockedForRoomsUID());
                                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists())
                                            {
                                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                                String nickName = user.getUserName();

                                                blockedGuysForRooms.setText(nickName);
                                            }else
                                            {

                                            }

                                        }
                                    });
                                    unBlockDudeFromRooms = v.findViewById(R.id.unblockFromRooms);

                                    if (theUID.equals(roomOwnerUID))
                                    {
                                        unBlockDudeFromRooms.setVisibility(View.VISIBLE);
                                    }else
                                    {
                                        unBlockDudeFromRooms.setVisibility(View.GONE);
                                    }

                                    unBlockDudeFromRooms.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    try{
                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                        Query applesQuery = ref.child(roomOwnerUID + " BlockedForRooms")
                                                                .orderByChild("blockedForRoomsUID").equalTo(model.getBlockedForRoomsUID());

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
                                                    }catch (Exception e)
                                                    {
                                                        e.getMessage();
                                                    }
                                                }
                                            }).start();


                                        }
                                    });

                                }
                            };

                    listTheOnline.setAdapter(adapter);
                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }).start();


    }

}
