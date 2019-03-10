package alserdar.casavoice.room_settings_functions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;

public class ReportRoom extends AppCompatActivity {

    TextView roomName , roomId ;
    EditText theReport ;
    Button pushTheReport ;


    String getRoomName , getRoomId , getRoomOwner , getSender , getSenderId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_room);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        getRoomName = this.getIntent().getExtras().getString("roomName");
        getRoomId = this.getIntent().getExtras().getString("roomID");
        getRoomOwner = this.getIntent().getExtras().getString("roomOwnerUID");
        getSender = this.getIntent().getExtras().getString("reportSenderName");
        getSenderId = this.getIntent().getExtras().getString("reportSenderId");


        roomName = findViewById(R.id.roomNameReport);
        roomId = findViewById(R.id.roomIdReport);
        theReport = findViewById(R.id.theReport);
        pushTheReport = findViewById(R.id.sendReport);


        roomName.setText(getRoomName);
        roomId.setText(getRoomId);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (theReport.getText().toString().equals(""))
                {
                    pushTheReport.setEnabled(false);
                }else
                {
                    pushTheReport.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        theReport.addTextChangedListener(watcher);

        pushTheReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> freshRoom = new HashMap<>();
                freshRoom.put("roomName", getRoomName);
                freshRoom.put("roomId", getRoomId);
                freshRoom.put("roomOwnerUID", getRoomOwner);
                freshRoom.put("reportSender", getSender);
                freshRoom.put("reportSenderID", getSenderId);
                freshRoom.put("theReport", theReport.getText().toString());


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("ReportRooms").document()
                        .set(freshRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new OurToast().myToast(getBaseContext(), getString(R.string.sending));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext(), getString(R.string.something_went_bad_try_again_later));
                    }
                });
            }
        });

    }
}
