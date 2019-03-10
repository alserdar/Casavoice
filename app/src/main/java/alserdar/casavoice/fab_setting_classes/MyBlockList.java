package alserdar.casavoice.fab_setting_classes;

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
import alserdar.casavoice.models.MyBlockModel;
import alserdar.casavoice.models.UserModel;

public class MyBlockList extends AppCompatActivity {

    String theUID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference doc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_block_list);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final ListView myBlockList = findViewById(R.id.myBlockListView);

        theUID = DetectUserInfo.theUID(getBaseContext());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    FirebaseListAdapter<MyBlockModel> adapter =
                            new FirebaseListAdapter<MyBlockModel>(getBaseContext(), MyBlockModel.class, R.layout.list_block_for_users,
                                    FirebaseDatabase.getInstance().getReference(theUID + " MyBlockList")) {
                                @Override
                                protected void populateView(View v, final MyBlockModel model, final int position) {


                                    final TextView blockedGuys = v.findViewById(R.id.blockedDudesForUsers);

                                    doc = db.collection("UserInformation").document(model.getBlockedHimUID());
                                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists())
                                            {
                                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                                blockedGuys.setText(user.getUserName());

                                            }else
                                            {

                                            }
                                        }
                                    });


                                    final Button unblockHim = v.findViewById(R.id.unblockFromUsers);

                                    unblockHim.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    try{
                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                        Query applesQuery = ref.child(theUID + " MyBlockList")
                                                                .orderByChild("blockedHimUID").equalTo(model.getBlockedHimUID());

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

                                                        Query apples = ref.child(model.getBlockedHimUID() + " HisBlockList")
                                                                .orderByChild("blockMeUID").equalTo(theUID);

                                                        apples.addListenerForSingleValueEvent(new ValueEventListener() {
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

                    myBlockList.setAdapter(adapter);

                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }).start();

    }
}
