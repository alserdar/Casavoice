package alserdar.casavoice.room_settings_functions;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.AdminsModel;
import alserdar.casavoice.models.UserModel;

public class AdminsUsers extends AppCompatActivity {

    String theUID;
    String roomOwnerUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_users);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final ListView listTheOnline = findViewById(R.id.list_admin_users);

        theUID = DetectUserInfo.theUID(getBaseContext());
        roomOwnerUID = this.getIntent().getExtras().getString("roomOwnerUID");

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    FirebaseListAdapter<AdminsModel> adapter =
                            new FirebaseListAdapter<AdminsModel>(getBaseContext(), AdminsModel.class, R.layout.list_admins,
                                    FirebaseDatabase.getInstance().getReference(roomOwnerUID + " Admins")) {
                                @Override
                                protected void populateView(View v, final AdminsModel model, int position) {



                                    final TextView adminsGuys = v.findViewById(R.id.adminsDudes);
                                    final ImageView adminsPic = v.findViewById(R.id.adminUserPic);
                                    Button removeAdmin = v.findViewById(R.id.removeAdmin);

                                    FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
                                    DocumentReference doc = db.collection("UserInformation").document(model.getAdminsUID());
                                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists())
                                            {
                                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                                String nickName = user.getUserName();
                                                adminsGuys.setText(nickName);
                                                LoadingPics.loadPicsForHome(getBaseContext() , model.getAdminsUID() , adminsPic);
                                            }else
                                            {

                                            }
                                        }
                                    });


                                    if (theUID.equals(roomOwnerUID))
                                    {
                                        removeAdmin.setVisibility(View.VISIBLE);
                                    }else
                                    {
                                        removeAdmin.setVisibility(View.GONE);
                                    }

                                    removeAdmin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {

                                                    try{
                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                        Query applesQuery = ref.child(roomOwnerUID + " Admins")
                                                                .orderByChild("adminsUID").equalTo(model.getAdminsUID());

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
