package alserdar.casavoice;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.OnlineModel;
import alserdar.casavoice.models.OwnerModel;
import alserdar.casavoice.models.VoiceModel;

public class CreatingRoom extends AppCompatActivity {

    EditText myRoomName, myRoomDetails;
    Button createRoom;
    String facebookUserName , theUID, roomProfilePicture , myNikname;
    FirebaseFirestore db;
    ImageView roomPic ;
    String id ;

    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 72 ;

    private int STORAGE_PERMISSION_CODE = 23;
    private int makeItInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_room);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

        facebookUserName = DetectUserInfo.faceBookUser(getBaseContext());
        theUID = DetectUserInfo.theUID(getBaseContext());
        myNikname = DetectUserInfo.myUserName(getBaseContext());
        id = DetectUserInfo.faceBookId(getBaseContext());
        db = FirebaseFirestore.getInstance();

        roomPic = findViewById(R.id.setPicForRoom);
        myRoomName = findViewById(R.id.myRoomName);
        myRoomDetails = findViewById(R.id.myRoomDetails);
        createRoom = findViewById(R.id.createMyRoomButton);


        roomPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //First checking if the app is already having the permission
                if(isReadStorageAllowed()){
                    //If permission is already having then showing the toast
                    Toast.makeText(CreatingRoom.this, getString(R.string.permission_granted),Toast.LENGTH_LONG).show();
                    //Existing the method with return


                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE_REQUEST);

                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();
            }
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (myRoomName.getText().toString().trim().isEmpty() || myRoomDetails.getText().toString().trim().isEmpty())
                {
                    createRoom.setEnabled(false);
                    createRoom.setClickable(false);
                    createRoom.setHint(R.string.create);
                }else
                {
                    createRoom.setEnabled(true);
                    createRoom.setClickable(true);
                    createRoom.setText(R.string.create);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        myRoomName.addTextChangedListener(watcher);
        myRoomDetails.addTextChangedListener(watcher);

        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                uploadImage();

                float convertTheId = Float.parseFloat(id);

                float closeTheId = convertTheId/999991989 ;

                makeItInt = Math.round(closeTheId);

                Map<String, Object> freshRoom = new HashMap<>();
                freshRoom.put("roomName", myRoomName.getText().toString());
                freshRoom.put("roomProfilePicture", roomProfilePicture);
                freshRoom.put("roomId", "R"+makeItInt);
                freshRoom.put("roomOwner", facebookUserName);
                freshRoom.put("roomOwnerUID", theUID);
                freshRoom.put("infoAboutRoom", myRoomDetails.getText().toString());
                freshRoom.put("peopleInTheRoom", "empty");
                freshRoom.put("emptyShitAboutRoom", "empty");
                freshRoom.put("maxUsersInTheRoom", 20);
                freshRoom.put("roomCreated", false);
                freshRoom.put("roomLocked", false);
                freshRoom.put("pass", 0);
                freshRoom.put("happinessRoom", 0);


                db.collection("RoomInformation").document(theUID)
                        .set(freshRoom).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new OurToast().myToast(getBaseContext(), getString(R.string.building));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext(), getString(R.string.something_went_bad_try_again_later));
                    }
                });

                DocumentReference hasRoom = db.collection("UserInformation").document(theUID);
                hasRoom.update("hasRoom" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        FirebaseDatabase roomDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference refMic1 = roomDatabase.
                                getReference(theUID + " voice").child("mic one");
                        refMic1.setValue(new VoiceModel("R"+makeItInt , "mic one" , "" , "" , "Offline" , "" , ""));

                        DatabaseReference refMic2 = roomDatabase.
                                getReference(theUID + " voice").child("mic two");
                        refMic2.setValue(new VoiceModel("R"+makeItInt , "mic two" , "" , "" , "Offline" , "", ""));

                        DatabaseReference refMic3 = roomDatabase.
                                getReference(theUID + " voice").child("mic three");
                        refMic3.setValue(new VoiceModel("R"+makeItInt , "mic three" , "" , "" , "Offline" , "", ""));

                        DatabaseReference refMic4 = roomDatabase.
                                getReference(theUID + " voice").child("mic four");
                        refMic4.setValue(new VoiceModel("R"+makeItInt , "mic four" , "" , "" , "Offline" , "", ""));


                        DatabaseReference refMic5 = roomDatabase.
                                getReference(theUID + " voice").child("mic five");
                        refMic5.setValue(new VoiceModel("R"+makeItInt , "mic five" , "" , "" , "Offline" , "", ""));

                    }
                });

                // room created for the owner
                DocumentReference doc = db.collection("RoomInformation").document(theUID);
                doc.update("roomCreated", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myOnlineList = database.getReference(theUID + " Online").push();
                        myOnlineList.setValue(new OnlineModel(facebookUserName , theUID , myNikname));

                        new OurToast().myToast(getBaseContext(), getString(R.string.room_created));
                        Intent i = new Intent(getBaseContext(), LiveRoom.class);
                        i.putExtra("roomIdInTheRoom" , "R"+makeItInt);
                        i.putExtra("roomNameInTheRoom" , myRoomName.getText().toString());
                        i.putExtra("ownerRoom" , facebookUserName);
                        i.putExtra("roomOwnerUID" , theUID);
                        startActivity(i);
                        finish();
                    }
                });


                // list For Owner
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myAdminsList = database.getReference(theUID + " Owner");
                myAdminsList.setValue(new OwnerModel(theUID));

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                roomPic.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (filePath != null) {

            StorageReference ref = storageReference.child("roomImages/" + theUID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            new OurToast().myToast(getBaseContext() , getString(R.string.picture_uploaded));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new OurToast().myToast(getBaseContext() , getString(R.string.failed));
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    //Requesting permission
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == STORAGE_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                //Displaying a toast
                new OurToast().myToast(getBaseContext() , getString(R.string.permission_granted));
            }else{
                //Displaying another toast if permission is not granted
                new OurToast().myToast(getBaseContext() , getString(R.string.permission_denied));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}