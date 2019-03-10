package alserdar.casavoice.fab_setting_classes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Locale;

import alserdar.casavoice.R;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.MyMessageListModel;
import alserdar.casavoice.models.UserModel;

public class Messages extends AppCompatActivity {


    ListView listAllUsers ;
    String theUID;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference doc ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

        listAllUsers = findViewById(R.id.allUsersWasTalkToMeInPrivateChat);
        theUID = DetectUserInfo.theUID(getBaseContext());

        final FirebaseListAdapter<MyMessageListModel> adapter =
                new FirebaseListAdapter<MyMessageListModel>(getBaseContext(), MyMessageListModel.class, R.layout.list_of_private_chat_in_messages,
                        FirebaseDatabase.getInstance().getReference(theUID + " myMessageList")) {

                    @Override
                    protected void populateView(final View v, final MyMessageListModel model, final int position) {
                        final TextView from = v.findViewById(R.id.fromWho);




                        doc = db.collection("UserInformation").document(model.getToUID());
                        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    UserModel user = documentSnapshot.toObject(UserModel.class);
                                    String nickname = user.getUserName();
                                    from.setText(nickname);



                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss" , Locale.getDefault());


                                    boolean redMe = user.isRedMe();
                                    if (redMe)
                                    {
                                        from.setTextColor(Color.RED);
                                    }else
                                    {
                                        from.setTextColor(Color.BLACK);
                                    }

                                }
                            }
                        });
                        final ImageView ppWho = v.findViewById(R.id.profilePicWho);

                        LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext() , model.getToUID() , ppWho);

                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try{
                                            doc = db.collection("UserInformation").document(model.getToUID());
                                            doc.update("redMe" , false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    //  new YouHaveNewMessageBroadcast().gotMessages(getBaseContext());

                                                }
                                            });
                                            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    if (documentSnapshot.exists())
                                                    {

                                                        UserModel user = documentSnapshot.toObject(UserModel.class);

                                                        String id = user.getId();
                                                        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                                        String theRealIdOfTheOwnerOfTheAccount = getInfo.getString("user_id" , "user_id");

                                                        Long sumTheId = Long.parseLong(id) + Long.parseLong(theRealIdOfTheOwnerOfTheAccount);
                                                        Intent i = new Intent(getBaseContext() , ChatPrivatly.class);
                                                        i.putExtra("theSum" , sumTheId);
                                                        i.putExtra("to" , model.getTo());
                                                        i.putExtra("toUID" , model.getToUID());
                                                        startActivity(i);
                                                    }else
                                                    {

                                                    }
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

        listAllUsers.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
