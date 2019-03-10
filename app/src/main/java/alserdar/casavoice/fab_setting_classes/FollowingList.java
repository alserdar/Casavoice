package alserdar.casavoice.fab_setting_classes;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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

import java.util.StringTokenizer;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.UserProfileInformation;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.FollowingModel;
import alserdar.casavoice.models.UserModel;

public class FollowingList extends AppCompatActivity {

    String theUID;
    TextView following ;
    Button removeFromFollowingList ;
    ImageView ppForListFollowing ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference doc ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

        final SoundPool sp;
        final int[] soundIds;
        AudioAttributes attrs = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            attrs = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            sp = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(attrs)
                    .build();
            soundIds = new int[10];
            soundIds[0] = sp.load(getBaseContext(), R.raw.ring, 1);
        }else
        {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundIds = new int[10];
            soundIds[0] = sp.load(getBaseContext(), R.raw.ring, 1);

        }

        final ListView followingList = findViewById(R.id.followingList);
        theUID = DetectUserInfo.theUID(getBaseContext());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    FirebaseListAdapter<FollowingModel> adapter =
                            new FirebaseListAdapter<FollowingModel>(getBaseContext(), FollowingModel.class, R.layout.list_following,
                                    FirebaseDatabase.getInstance().getReference(theUID + " FollowingList")) {
                                @Override
                                protected void populateView(final View v, final FollowingModel model, final int position) {

                                    doc = db.collection("UserInformation").document(model.getTheFollowingDudesUID());
                                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists())
                                            {
                                                following = v.findViewById(R.id.dudesOnListFollowing);
                                                ppForListFollowing = v.findViewById(R.id.profilePictureForListFollowing);
                                                removeFromFollowingList = v.findViewById(R.id.removeButtonForListFollowing);

                                                final UserModel user = documentSnapshot.toObject(UserModel.class);
                                                following.setText(user.getUserName());
                                                LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext() , user.getTheUID() , ppForListFollowing);

                                                removeFromFollowingList.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        sp.play(soundIds[0], 1, 1, 1, 0, 1);

                                                        AlertDialog alertDialog = new AlertDialog.Builder(FollowingList.this).create();
                                                        alertDialog.setTitle(getString(R.string.remove));
                                                        alertDialog.setMessage(getString(R.string.are_you_sure));
                                                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.ok),
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {


                                                                        new Thread(new Runnable() {
                                                                            @Override
                                                                            public void run() {

                                                                                try{
                                                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                                                                    Query applesQuery = ref.child(theUID + " FollowingList")
                                                                                            .orderByChild("theFollowingDudesUID").equalTo(model.getTheFollowingDudesUID());

                                                                                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                                                                appleSnapshot.getRef().removeValue();
                                                                                                new OurToast().myToast(getBaseContext() , getString(R.string.removed));
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
                                                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.cancel),
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                        alertDialog.show();


                                                    }
                                                });

                                                following.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        sp.play(soundIds[0], 1, 1, 1, 0, 1);

                                                        new Thread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                try{
                                                                    StringTokenizer st = new StringTokenizer(model.getTheFollowingDudes(), " ");
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
                                                                }catch (Exception e)
                                                                {
                                                                    e.getMessage();
                                                                }
                                                            }
                                                        }).start();

                                                    }
                                                });

                                            }
                                        }
                                    });
                                }
                            };
                    followingList.setAdapter(adapter);
                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }).start();

    }
}
