package alserdar.casavoice.fab_setting_classes;

import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.StringTokenizer;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.UserProfileInformation;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.FollowersModel;
import alserdar.casavoice.models.FollowingModel;
import alserdar.casavoice.models.UserModel;

public class FollowersList extends AppCompatActivity {

    String theUID;
    String nickname ;
    TextView followers ;
    ImageView ppForListFollowers ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference doc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

        final ListView followerList = findViewById(R.id.followersList);
        theUID = DetectUserInfo.theUID(getBaseContext());
        nickname = DetectUserInfo.myUserName(getBaseContext());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    FirebaseListAdapter<FollowersModel> adapter =
                            new FirebaseListAdapter<FollowersModel>(getBaseContext(), FollowersModel.class, R.layout.list_followers,
                                    FirebaseDatabase.getInstance().getReference(theUID + " FollowersList")) {
                                @Override
                                protected void populateView(final View v, final FollowersModel model, final int position) {


                                    doc = db.collection("UserInformation").document(model.getTheFollowersDudesUID());
                                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists())
                                            {

                                                followers = v.findViewById(R.id.dudesOnListFollowers);
                                                ppForListFollowers = v.findViewById(R.id.profilePictureForListFollowers);
                                                final Button followThisFollower = v.findViewById(R.id.followButtonForListFollowers);

                                                final UserModel user = documentSnapshot.toObject(UserModel.class);
                                                followers.setText(user.getUserName());
                                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext() ,user.getTheUID() , ppForListFollowers);

                                                followThisFollower.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                        DatabaseReference myFollowingList = database.getReference(theUID + " FollowingList").push();
                                                        myFollowingList.setValue(new FollowingModel(followers.getText().toString() , user.getTheUID()));
                                                        new OurToast().myToast(getBaseContext() , followers.getText().toString() + getString(R.string.followed));


                                                        //add me to the fucking guy followers List
                                                        DatabaseReference hisFollowersList = database.getReference(followers.getText().toString() + " FollowersList").push();
                                                        hisFollowersList.setValue(new FollowersModel(nickname , theUID));

                                                    }
                                                });

                                                followers.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {

                                                        StringTokenizer st = new StringTokenizer(model.getTheFollowersDudes(), " ");
                                                        final String userName = st.nextToken();
                                                        //  new OurToast().myToast(getBaseContext() , userName);


                                                        if (userName.equals(theUID) ||
                                                                userName.equals("Casavoice")) {

                                                        } else {

                                                            Intent iProf = new Intent(getBaseContext(), UserProfileInformation.class);
                                                            iProf.putExtra("hisOriginalName", userName);
                                                            iProf.putExtra("hisOriginalUID", user.getTheUID());
                                                            startActivity(iProf);

                                                            DatabaseReference amBlocked = FirebaseDatabase.getInstance()
                                                                    .getReference(theUID + " HisBlockList");
                                                            amBlocked.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                                    if (dataSnapshot.exists())
                                                                    {
                                                                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                                                                            if (data.child("blockMe").getValue().equals(userName)) {
                                                                                new OurToast().myToast(getBaseContext(), getString(R.string.you_are_blocked_from) + userName);

                                                                            }
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                });

                                            }
                                        }
                                    });
                                }
                            };


                    followerList.setAdapter(adapter);
                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }).start();

    }
}
