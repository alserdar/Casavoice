package alserdar.casavoice;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import alserdar.casavoice.models.RoomModel;
import alserdar.casavoice.models.UserModel;

public class SearchActivity extends AppCompatActivity {

    EditText etSearch;
    ListView lvFirst;

    String searchFromUserProfileInfo = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final LinearLayout avlLay = findViewById(R.id.aviLay);

        etSearch= findViewById(R.id.searchEditText);
        lvFirst= findViewById(R.id.searchList);
        lvFirst.setTextFilterEnabled(true);

        searchFromUserProfileInfo = this.getIntent().getExtras().getString("roomIdFromUserProfileInformation");
        if(searchFromUserProfileInfo == null)
        {

        }else
        {
            etSearch.setText(searchFromUserProfileInfo);
        }

        final LinearLayout resLay = findViewById(R.id.resetLay);
        Button resetDaily = findViewById(R.id.resetDaily);
        Button resetWeekly = findViewById(R.id.resetWeekly);
        Button resetMonthly = findViewById(R.id.resetMonthly);

        /*


        here we Put the Fuckin Secret <3

         */



        Button goSearch = findViewById(R.id.search_btn);

        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                CharSequence foo = etSearch.getText();
                String bar = foo.toString();
                String desiredString = bar.substring(0,1);

                if (desiredString.equals("R"))
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("RoomInformation").whereEqualTo("roomId" , etSearch.getText().toString())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(final QuerySnapshot documentSnapshots) {

                                    if (documentSnapshots.isEmpty())
                                    {
                                        new OurToast().myToast(getBaseContext() , getString(R.string.room_not_found));
                                    }else
                                    {
                                        List<RoomModel> listUsers = documentSnapshots.toObjects(RoomModel.class);
                                        final SearchRoomListAdapter adapter = new SearchRoomListAdapter(getBaseContext() , listUsers);
                                        lvFirst.setAdapter(adapter);

                                    }
                                }
                            });
                }else
                {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("UserInformation").whereEqualTo("littleID" , etSearch.getText().toString())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(final QuerySnapshot documentSnapshots) {

                                    if (documentSnapshots.isEmpty())
                                    {
                                        new OurToast().myToast(getBaseContext() , getString(R.string.user_not_found));
                                    }else
                                    {
                                        List<UserModel> listUsers = documentSnapshots.toObjects(UserModel.class);
                                        final SearchUserListAdapter adapter = new SearchUserListAdapter(getBaseContext() , listUsers);
                                        lvFirst.setAdapter(adapter);

                                    }
                                }
                            });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
