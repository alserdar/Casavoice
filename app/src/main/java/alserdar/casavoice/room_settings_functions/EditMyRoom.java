package alserdar.casavoice.room_settings_functions;

import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.LiveRoom;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.UserModel;

public class EditMyRoom extends AppCompatActivity {


    LinearLayout changePicLay ;
    ImageView changeRoomPic ;
    EditText changeRoomName , changeRoomDetails ;
    Button unlockRoom , deleteChat , saveAll , upTheRoom;
    TextView theIdRoom ;

    String facebookUserName  , theUID;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 72;
    private int STORAGE_PERMISSION_CODE = 23;


    FirebaseStorage storage;
    StorageReference storageReference;

    String roomName ,roomID , roomOwnerUID, roomInfo;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference doc ;
    private int balance;
    private int happiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_room);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();




        changePicLay = findViewById(R.id.changeRoomPicLay);
        changeRoomPic = findViewById(R.id.changeRoomPicImageView);
        changeRoomName = findViewById(R.id.changeRoomName);
        changeRoomDetails = findViewById(R.id.changeRoomDetails);
        theIdRoom = findViewById(R.id.changeRoomID);
        unlockRoom = findViewById(R.id.unlockYourRoom);
        deleteChat = findViewById(R.id.deleteChat);
        upTheRoom = findViewById(R.id.upTheRoomInside);
        saveAll = findViewById(R.id.saveYourRoomChanges);

        facebookUserName = DetectUserInfo.faceBookUser(getBaseContext());
        theUID = DetectUserInfo.theUID(getBaseContext());

        theBalance();
        theHappiness();

        roomName = this.getIntent().getExtras().getString("roomName");
        roomID = this.getIntent().getExtras().getString("roomId");
        roomOwnerUID = this.getIntent().getExtras().getString("roomOwnerUID");
        roomInfo = this.getIntent().getExtras().getString("roomDetails");

        changeRoomName.setText(roomName);
        changeRoomDetails.setText(roomInfo);
        theIdRoom.setText(roomID);

        LoadingPics.loadPicForRooms(getBaseContext().getApplicationContext() , roomOwnerUID, changeRoomPic);


        upTheRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                theBalance();
                theHappiness();
                if ( balance >= 50000)
                {
                    db = FirebaseFirestore.getInstance();
                    doc = db.collection("RoomInformation").document(theUID);
                    int newHappiness = happiness + 1000 ;
                    doc.update("happiness", newHappiness).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final int minusBalance = (balance - 50000) ;
                            doc = db.collection("UserInformation").document(theUID);
                            doc.update("balanceOfCoins" , minusBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    new OurToast().myToast(getBaseContext(), getString(R.string.good_again));
                                }
                            });
                        }
                    });
                }else
                {
                    new OurToast().myToast(getBaseContext() , getString(R.string.you_need_to_charge));
                }


            }
        });



        changePicLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE_REQUEST);

                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();
            }
        });


        changeRoomPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            if(isReadStorageAllowed()) {
                                //If permission is already having then showing the toast
                                //Existing the method with return
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE_REQUEST);
                                return;
                            }

                            //If the app has not the permission then asking for the permission
                            requestStoragePermission();
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();


            }
        });

        deleteChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditMyRoom.this);

                dialog.setTitle("Delete Chat !")
                        .setMessage("Are you sure ?")
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                            }})
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(roomOwnerUID + " Chat");
                                ref.removeValue();
                            }
                        }).show();
            }
        });


        unlockRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                theBalance();
                if (balance >= 2000)
                {
                    db = FirebaseFirestore.getInstance();
                    doc = db.collection("RoomInformation").document(theUID);
                    doc.update("roomLocked", false).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final int minusBalance = (balance - 2000) ;
                            doc = db.collection("UserInformation").document(theUID);
                            doc.update("balanceOfCoins" , minusBalance).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    new OurToast().myToast(getBaseContext(), getString(R.string.room_unlcoked));
                                }
                            });
                        }
                    });
                }else
                {
                    new OurToast().myToast(getBaseContext() , getString(R.string.you_need_to_charge));
                }
            }
        });


        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            updateData();
                            uploadImage();
                            Intent i = new Intent(getBaseContext() , LiveRoom.class);
                            i.putExtra("roomIdInTheRoom" , roomID);
                            i.putExtra("roomNameInTheRoom" , changeRoomName.getText().toString());
                            i.putExtra("ownerRoom" , roomOwnerUID);
                            i.putExtra("onlineGuys" , facebookUserName);
                            startActivity(i);
                            finish();
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();
                //check internet
                // sleep for 5 seconds
                // load by AVD
                // get him back to the mutherfucker room

            }
        });

    }

    private void updateData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    if (roomName.equals(changeRoomName.getText().toString()))
                    {

                    }else
                    {

                        doc = db.collection("RoomInformation").document(theUID);
                        doc.update("roomName" , changeRoomName.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                doc.update("infoAboutRoom" , changeRoomDetails.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        new OurToast().myToast(getBaseContext() , getString(R.string.cool));
                                    }
                                });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                changeRoomPic.setImageBitmap(bitmap);
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


    private int theBalance ()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);

                    balance = user.getBalanceOfCoins();
                }
            }
        });

        return balance ;
    }

    private int theHappiness ()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference doc = db.collection("RoomInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                UserModel user = documentSnapshot.toObject(UserModel.class);

                happiness = user.getHappinessRoom();

            }
        });

        return happiness ;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

