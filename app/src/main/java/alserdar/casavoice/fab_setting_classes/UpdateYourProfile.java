package alserdar.casavoice.fab_setting_classes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.animate.UpAndDown;
import alserdar.casavoice.store.profile_functions.GetNiceCar;
import alserdar.casavoice.store.profile_functions.GetNiceHouse;
import alserdar.casavoice.store.profile_functions.GetNicePet;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.UserModel;

public class UpdateYourProfile extends AppCompatActivity {


    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    FirebaseStorage storage;
    StorageReference storageReference;

    ImageView _updateUserProfile ;
    EditText _updateUserName ;
    RadioButton male , female ;
    TextView gender , home , relationsShip , id , level , age , car, pet;
    LinearLayout _updateGenderLayout , _updateHomeLayout
            , _updateRelationShipLayout  , _updateAgeLayout
            , _updateCarLayout , _updatePetLayout  , _hiddenGenderLayout;

    Button saveUpdateProfile ;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private int STORAGE_PERMISSION_CODE = 23;

    private String theUID;
    private String originalName , originalGender , originalBirthDate , originalCar ,originalHouse , originalPet ;
    private DocumentReference doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_your_profile);
        ActionBar actionBar =getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

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

        originalName = this.getIntent().getExtras().getString("userName");
        originalGender = this.getIntent().getExtras().getString("gender");
        originalBirthDate = this.getIntent().getExtras().getString("birthDate");
        originalCar = this.getIntent().getExtras().getString("car");
        originalHouse = this.getIntent().getExtras().getString("house");
        originalPet = this.getIntent().getExtras().getString("pet");

        theUID = DetectUserInfo.theUID(getBaseContext());

        //layouts ref

        _hiddenGenderLayout = findViewById(R.id.hiddenGenderLayout);
        _updateGenderLayout = findViewById(R.id.updateGenderLayout);
        _updateHomeLayout = findViewById(R.id.update_homeLayout);
      //  _updateRelationShipLayout = (LinearLayout)findViewById(R.id.update_relationshipLayout);
        _updateAgeLayout = findViewById(R.id.update_birthdayLayout);
        _updateCarLayout = findViewById(R.id.update_carLayout);
        _updatePetLayout = findViewById(R.id.update_petLayout);

        //views ref
        _updateUserProfile = findViewById(R.id.updateUserProfilePicture);

        _updateUserName = findViewById(R.id.updateUserName);
        gender = findViewById(R.id.update_gender);
        id = findViewById(R.id.update_the_id);
        level = findViewById(R.id.update_the_level);
        home = findViewById(R.id.update_home);
        age = findViewById(R.id.update_birthday);
        pet = findViewById(R.id.update_pet);
        car = findViewById(R.id.update_car);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        readSingleUserCustomObject();

        LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext() , theUID, _updateUserProfile);

        _updateUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

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
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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

        saveUpdateProfile = findViewById(R.id.saveYourProfile);
        saveUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            updateData();
                            uploadImage();
                            Intent i = new Intent(getBaseContext() , YourProfile.class);
                            startActivity(i);
                            finish();
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();


            }
        });

        if (theUID.isEmpty())
        {
            _updateUserName.setText("Noobie");
        }else
        {
            _updateUserName.setText(theUID);
        }

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            if (male.isChecked())
                            {
                                _hiddenGenderLayout.setVisibility(View.GONE);
                                gender.setText(R.string.male);
                                gender.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.mipmap.make, 0);

                            }
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            if (female.isChecked())
                            {
                                _hiddenGenderLayout.setVisibility(View.GONE);
                                gender.setText(R.string.female);
                                gender.setCompoundDrawablesWithIntrinsicBounds( 0, 0, R.mipmap.female, 0);
                            }
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

        _updateGenderLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                if (_hiddenGenderLayout.getVisibility() == View.GONE)
                {
                    _hiddenGenderLayout.setVisibility(View.VISIBLE);
                    new UpAndDown().showTheLay(_hiddenGenderLayout);
                }else if (_hiddenGenderLayout.getVisibility() == View.VISIBLE)
                {
                    new UpAndDown().hideTheLay(_hiddenGenderLayout);

                    Thread timer = new Thread() {
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.getMessage();
                            } finally {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                       _hiddenGenderLayout.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    };
                    timer.start();
                }
            }
        });


        // age updating codes
        _updateAgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                int realYear = Calendar.getInstance().get(Calendar.YEAR);
                int realMonth = Calendar.getInstance().get(Calendar.MONTH);
                int realDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog datePickerDialog = new DatePickerDialog
                        (UpdateYourProfile.this, new DatePickerDialog.OnDateSetListener() {


                            public void onDateSet(DatePicker view, int pickedYear, int pickedMonthOfYear, int pickedDayOfMonth) {


                                age.setText(String.format(Locale.getDefault(), "%d/%d/%d", pickedDayOfMonth, pickedMonthOfYear + 1, pickedYear));

                            }
                        }, realYear, realMonth, realDayOfMonth);

                datePickerDialog.show();

            }

        });


        //home updating codes
        _updateHomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            Intent i = new Intent(getBaseContext() , GetNiceHouse.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

        // car updating codes
        _updateCarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{

                            Intent i = new Intent(getBaseContext() , GetNiceCar.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();
            }
        });

        //pet updating codes
        _updatePetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.play(soundIds[0], 1, 1, 1, 0, 1);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            Intent i = new Intent(getBaseContext() , GetNicePet.class);
                            startActivity(i);
                        }catch (Exception e)
                        {
                            e.getMessage();
                        }
                    }
                }).start();

            }
        });

    }

    private void updateData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    final DocumentReference doc = db.collection("UserInformation").document(theUID);
                    String sameName = _updateUserName.getText().toString();
                    String sameGender = gender.getText().toString();
                    String sameBirthDate = age.getText().toString();
                    String sameHouse = home.getText().toString();
                    String sameCar = car.getText().toString();
                    String samePet = pet.getText().toString();

                    if (originalName.equals(sameName))
                    {

                    }else
                    {

                        doc.update("userName" , _updateUserName.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                new OurToast().myToast(getBaseContext() ,  getString(R.string.name_changed));
                                doc.update("originalName" , theUID).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(UpdateYourProfile.this);
                                        final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                                        editor.putString("myUserName" , _updateUserName.getText().toString());
                                        editor.apply();
                                        new OurToast().myToast(getBaseContext() , getString(R.string.name_changed));
                                    }
                                });
                            }
                        });
                    }

                    if (originalGender.equals(sameGender))
                    {

                    }else
                    {
                        doc.update("gender" , gender.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                new OurToast().myToast(getBaseContext() , getString(R.string.gender_changed));
                            }
                        });
                    }

                    if (originalBirthDate.equals(sameBirthDate))
                    {

                    }else
                    {
                        doc.update("BirthDate" , age.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                new OurToast().myToast(getBaseContext() , getString(R.string.birthdate_changed));
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
                _updateUserProfile.setImageBitmap(bitmap);
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

        if(filePath != null)
        {

            StorageReference ref = storageReference.child("images/"+ theUID);
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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

    private void readSingleUserCustomObject() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                _updateUserName.setText(user.getUserName());
                                id.setText("ID :" + user.getLittleID());
                                level.setText("lv :" + String.valueOf(user.getLevel()));
                                gender.setText(user.getGender());
                                age.setText(user.getBirthDate());
                                home.setText(user.getHouse());
                                car.setText(user.getCar());
                                pet.setText(user.getPet());
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
