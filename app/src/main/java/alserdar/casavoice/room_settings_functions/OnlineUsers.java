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
import alserdar.casavoice.models.BlockModelForRooms;
import alserdar.casavoice.models.OnlineModel;
import alserdar.casavoice.models.UserModel;

public class OnlineUsers extends AppCompatActivity {

    TextView onlineGuys ;
    Button blockTheUser , makeTheUserAdmin ;
    private String theUID;
    FirebaseFirestore db  = FirebaseFirestore.getInstance() ;
    DocumentReference doc ;
    String roomOwnerUID;
    ImageView ppOnline ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_users);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final ListView listTheOnline = findViewById(R.id.list_online_users);


        theUID = DetectUserInfo.theUID(getBaseContext());
        roomOwnerUID = this.getIntent().getExtras().getString("roomOwnerUID");



        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    FirebaseListAdapter<OnlineModel> adapter =
                            new FirebaseListAdapter<OnlineModel>(getBaseContext(), OnlineModel.class, R.layout.list_online,
                                    FirebaseDatabase.getInstance().getReference(roomOwnerUID + " Online")) {
                                @Override
                                protected void populateView(final View v, final OnlineModel model, final int position) {

                                    assert roomOwnerUID != null;
                                    doc = db.collection("UserInformation").document(model.getOnlineDudeUID());
                                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists())
                                            {


                                                onlineGuys = v.findViewById(R.id.onlineDudes);
                                                ppOnline = v.findViewById(R.id.ppForOnline);
                                                blockTheUser = v.findViewById(R.id.blockUsers);
                                                makeTheUserAdmin = v.findViewById(R.id.makeAdmins);

                                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                                String nickName = user.getUserName();
                                                onlineGuys.setText(nickName);

                                                final String realName = user.getOriginalName();
                                                final String realUID = user.getTheUID() ;

                                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext()
                                                        , user.getTheUID() , ppOnline);

                                                if (theUID.equals(roomOwnerUID)) {

                                                    if (realName.equals(roomOwnerUID))
                                                    {
                                                        makeTheUserAdmin.setVisibility(View.GONE);
                                                        blockTheUser.setVisibility(View.GONE);
                                                    }else
                                                    {
                                                        makeTheUserAdmin.setVisibility(View.VISIBLE);
                                                        blockTheUser.setVisibility(View.VISIBLE);
                                                    }
                                                }else
                                                {
                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                    Query applesQuery = ref.child(roomOwnerUID + " Admins")
                                                            .orderByChild("adminsUID").equalTo(model.getOnlineDudeUID());

                                                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists())
                                                            {
                                                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                                    AdminsModel admins = appleSnapshot.getValue(AdminsModel.class);

                                                                    if (admins.getAdminsUID().equals(model.getOnlineDudeUID()))
                                                                    {
                                                                        blockTheUser.setVisibility(View.VISIBLE);
                                                                        makeTheUserAdmin.setVisibility(View.GONE);
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });

                                                    makeTheUserAdmin.setVisibility(View.GONE);
                                                    blockTheUser.setVisibility(View.GONE);

                                                }


                                                blockTheUser.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                        DatabaseReference myBlockList = database.getReference(roomOwnerUID + " BlockedForRooms").push();
                                                        myBlockList.setValue(new BlockModelForRooms(realName , realUID));
                                                      //  new OurToast().myToast(getBaseContext() , onlineGuys.getText().toString() + " Blocked");
                                                        makeTheUserAdmin.setVisibility(View.GONE);
                                                        blockTheUser.setVisibility(View.GONE);

                                                    }
                                                });

                                                // add for admins list
                                                makeTheUserAdmin.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                        DatabaseReference myBlockList = database.getReference(roomOwnerUID + " Admins").push();
                                                        myBlockList.setValue(new AdminsModel(realName , realUID));
                                                      //  new OurToast().myToast(getBaseContext() , onlineGuys.getText().toString() + " is Admin now");
                                                        makeTheUserAdmin.setVisibility(View.GONE);
                                                        blockTheUser.setVisibility(View.GONE);
                                                    }
                                                });
                                            }
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