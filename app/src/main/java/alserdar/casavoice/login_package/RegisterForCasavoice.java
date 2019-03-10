package alserdar.casavoice.login_package;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import alserdar.casavoice.Home;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.models.AppInfoModel;

public class RegisterForCasavoice extends AppCompatActivity {

    String facebookUserName , facebookId , theUID;
    Button saveAll ;
    ImageView reg_pic ;
    EditText regName ;
    TextView reg_birthDate , def_reg_birthDate , whichGender ;
    RadioButton regMale , regFemale ;
    private int STORAGE_PERMISSION_CODE = 25;
    private int PICK_IMAGE_REQUEST = 75;
    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    int littleID ;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_for_casavoice);


        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(RegisterForCasavoice.this);
        facebookUserName = getInfo.getString("FacebookUser" , "FacebookUser");
        country = getInfo.getString("currentCountry" , "currentCountry");
        facebookId = getInfo.getString("user_id" , "user_id");
        theUID = getInfo.getString("uid" , "uid");

        float convertTheId = Float.parseFloat(facebookId);
        float closeTheId = convertTheId/999991989 ;
        littleID = Math.round(closeTheId);
        SharedPreferences little_id = PreferenceManager.getDefaultSharedPreferences(RegisterForCasavoice.this);
        final SharedPreferences.Editor editor = little_id.edit();
        editor.putString("little_id" , String.valueOf(littleID));
        editor.apply();

        saveAll = findViewById(R.id.reg_save);
        reg_pic = findViewById(R.id.regPic);
        regName = findViewById(R.id.reg_username);
        reg_birthDate = findViewById(R.id.reg_birthday);
        def_reg_birthDate = findViewById(R.id.def_reg_birthday);
        regMale = findViewById(R.id.reg_male);
        regFemale = findViewById(R.id.reg_female);
        whichGender = findViewById(R.id.whichGenderIs);

        reg_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            }
        });

        regMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (regMale.isChecked())
                {
                    whichGender.setText("Male");
                }
            }
        });

        regFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (regFemale.isChecked())
                {
                    whichGender.setText("Female");
                }
            }
        });


        reg_birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int realYear = Calendar.getInstance().get(Calendar.YEAR);
                int realMonth = Calendar.getInstance().get(Calendar.MONTH);
                int realDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                final DatePickerDialog datePickerDialog = new DatePickerDialog
                        (RegisterForCasavoice.this, new DatePickerDialog.OnDateSetListener() {


                            public void onDateSet(DatePicker view, int pickedYear, int pickedMonthOfYear, int pickedDayOfMonth) {


                                reg_birthDate.setText(String.format(Locale.getDefault(), "%d/%d/%d", pickedDayOfMonth, pickedMonthOfYear + 1, pickedYear));
                                def_reg_birthDate.setText(String.format(Locale.getDefault(), "%d/%d/%d", pickedDayOfMonth, pickedMonthOfYear + 1, pickedYear));

                            }
                        }, realYear, realMonth, realDayOfMonth);

                datePickerDialog.show();
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (regName.getText().toString().equals("casavoice") || regName.getText().toString().equals("Casavoice")
                        || regName.getText().toString().isEmpty())
                {
                    saveAll.setEnabled(false);

                }else
                {
                    saveAll.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        regName.addTextChangedListener(watcher);

        saveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String , Object> freshUser = new HashMap<>();
                freshUser.put("userName" , regName.getText().toString());
                freshUser.put("originalName" , facebookUserName);
                freshUser.put("id" , facebookId);
                freshUser.put("theUID" , theUID);
                freshUser.put("littleID" , String.valueOf(littleID));
                freshUser.put("email", "empty");
                freshUser.put("gender" , whichGender.getText().toString());
                freshUser.put("phone" , "empty");
                freshUser.put("house", "Basement"); // this will remove too
                freshUser.put("level", 1); // this will remove
                freshUser.put("BirthDate" , def_reg_birthDate.getText().toString());
                freshUser.put("country" , country);
                freshUser.put("pet" , "Cat"); // this will remove too
                freshUser.put("car" , "Fiat"); // this will remove too
                freshUser.put("balanceOfCoins" , 500); // this will remove too
                freshUser.put("hasRoom" , false); // this will remove too
                freshUser.put("vip" , "no"); // this will remove too
                freshUser.put("userSus" , false); // this will remove too
                freshUser.put("hasVip" , false); // this will remove too
                freshUser.put("happinessPerson" , 60); // this will remove too
                freshUser.put("happinessCar" , 20); // this will remove too
                freshUser.put("happinessPet" , 30); // this will remove too
                freshUser.put("happinessHouse" , 10); // this will remove too

                SharedPreferences privateOrNotDetails = PreferenceManager.getDefaultSharedPreferences(RegisterForCasavoice.this);
                final SharedPreferences.Editor editor = privateOrNotDetails.edit();
                editor.putString("myUserName" , regName.getText().toString());
                editor.apply();

                uploadImage();


                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("UserInformation").document(theUID)
                        .set(freshUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        final FirebaseDatabase base = FirebaseDatabase.getInstance();
                        DatabaseReference reference = base.getReference("appInfo").push();
                        reference.setValue(new AppInfoModel(facebookUserName , regName.getText().toString() , theUID , country ,
                                "" , new Date() , new Date()));

                        new OurToast().myToast(getBaseContext() ,String.format(" %s %s " , getString(R.string.welcome) , regName.getText().toString()));
                        Intent i = new Intent(getBaseContext() , Home.class);
                        startActivity(i);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new OurToast().myToast(getBaseContext() , getString(R.string.Something_went_bad_try_again_later));
                    }
                });
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                reg_pic.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
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

        new OurToast().myToast(getBaseContext() , getString(R.string.save_your_information_first));
    }
}
