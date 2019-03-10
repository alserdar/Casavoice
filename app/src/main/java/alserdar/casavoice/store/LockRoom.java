package alserdar.casavoice.store;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.models.RoomModel;
import alserdar.casavoice.models.UserModel;
import alserdar.casavoice.timed_things_stuff.CheckTime;

public class LockRoom extends AppCompatActivity implements View.OnClickListener {


    LinearLayout dayLay , monthLay , threeMonthsLay , yearLay ;
    Button okLock ;
    boolean okDay , okMonth , okThreeMonths , okYear ;

    String theUID;
    int balance;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference doc ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_room);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();

        final TextView infoLocked = findViewById(R.id.infoAboutRoomPeriod);

        theUID = DetectUserInfo.theUID(getBaseContext());

        doc = db.collection("RoomInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    RoomModel model = documentSnapshot.toObject(RoomModel.class);

                    if (model.getEndLockDate() == null)
                    {

                    }else
                    {

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy" , Locale.getDefault());

                        infoLocked.setText(String.format("%s %s %s %s", getString(R.string.locked_For), model.getLockPeriod(), getString(R.string.until), sdf.format(model.getEndLockDate())));
                    }

                }
            }
        });

        dayLay = findViewById(R.id.lockDay);
        monthLay = findViewById(R.id.lockMonth);
        threeMonthsLay = findViewById(R.id.lockThreeMonths);
        yearLay = findViewById(R.id.lockYear);

        okLock = findViewById(R.id.okLockTheRoom);

        dayLay.setOnClickListener(this);
        monthLay.setOnClickListener(this);
        threeMonthsLay.setOnClickListener(this);
        yearLay.setOnClickListener(this);


        readBalance();
        CheckTime.checkTimeReality(getBaseContext());

        okLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CheckTime.checkTimeReality(getBaseContext()))
                {
                    if (okDay)
                    {
                        readBalance();
                        if (balance >= 1000)
                        {

                            final EditText thePassword ;
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LockRoom.this);
                            alertDialogBuilder .setIcon(R.mipmap.lock);
                            alertDialogBuilder.setTitle(getString(R.string.type_password));
                            thePassword = new EditText(LockRoom.this);
                            thePassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                            alertDialogBuilder.setView(thePassword);
                            alertDialogBuilder.setPositiveButton(getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1)
                                        {
                                            if (thePassword.getText().toString().trim().equals(""))
                                            {

                                            }else
                                            {
                                                long pass = Long.parseLong(thePassword.getText().toString());
                                                doc = db.collection("RoomInformation").document(theUID);
                                                doc.update("pass" , pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        doc.update("lockPeriod" , "day").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                doc.update("startLockDate" , new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Date date = new Date();
                                                                        Calendar c = Calendar.getInstance();
                                                                        c.setTime(date);
                                                                        c.add(Calendar.DATE, 1);
                                                                        date = c.getTime();
                                                                        doc.update("endLockDate" , date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                doc.update("roomLocked" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                        DocumentReference doc = db.collection("UserInformation").document(theUID);
                                                                                        doc.update("balanceOfCoins" , balance - 1000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                Intent i = new Intent(getBaseContext() , Store.class);
                                                                                                startActivity(i);
                                                                                                finish();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }else
                        {
                            new OurToast().myToast(getBaseContext() , getString(R.string.not_enough_cash));
                        }
                    }else if (okMonth)
                    {
                        readBalance();
                        if (balance >= 7000)
                        {

                            final EditText thePassword ;
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LockRoom.this);
                            alertDialogBuilder .setIcon(R.mipmap.lock);
                            alertDialogBuilder.setTitle(getString(R.string.type_password));
                            thePassword = new EditText(LockRoom.this);
                            thePassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                            alertDialogBuilder.setView(thePassword);
                            alertDialogBuilder.setPositiveButton(getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1)
                                        {
                                            if (thePassword.getText().toString().trim().equals(""))
                                            {

                                            }else
                                            {
                                                long pass = Long.parseLong(thePassword.getText().toString());
                                                doc = db.collection("RoomInformation").document(theUID);
                                                doc.update("pass" , pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        doc.update("lockPeriod" , "month").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                doc.update("startLockDate" , new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Date date = new Date();
                                                                        Calendar c = Calendar.getInstance();
                                                                        c.setTime(date);
                                                                        c.add(Calendar.MONTH, 1);
                                                                        date = c.getTime();
                                                                        doc.update("endLockDate" , date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                doc.update("roomLocked" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                        DocumentReference doc = db.collection("UserInformation").document(theUID);
                                                                                        doc.update("balanceOfCoins" , balance - 7000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                Intent i = new Intent(getBaseContext() , Store.class);
                                                                                                startActivity(i);
                                                                                                finish();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }else
                        {
                            new OurToast().myToast(getBaseContext() , getString(R.string.not_enough_cash));
                        }

                    }else if (okThreeMonths)
                    {
                        readBalance();
                        if (balance >= 19000)
                        {

                            final EditText thePassword ;
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LockRoom.this);
                            alertDialogBuilder .setIcon(R.mipmap.lock);
                            alertDialogBuilder.setTitle(getString(R.string.type_password));
                            thePassword = new EditText(LockRoom.this);
                            thePassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                            alertDialogBuilder.setView(thePassword);
                            alertDialogBuilder.setPositiveButton(getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1)
                                        {
                                            if (thePassword.getText().toString().trim().equals(""))
                                            {

                                            }else
                                            {
                                                long pass = Long.parseLong(thePassword.getText().toString());
                                                doc = db.collection("RoomInformation").document(theUID);
                                                doc.update("pass" , pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        doc.update("lockPeriod" , "threeMonths").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                doc.update("startLockDate" , new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Date date = new Date();
                                                                        Calendar c = Calendar.getInstance();
                                                                        c.setTime(date);
                                                                        c.add(Calendar.MONTH, 3);
                                                                        date = c.getTime();
                                                                        doc.update("endLockDate" , date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                doc.update("roomLocked" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                        DocumentReference doc = db.collection("UserInformation").document(theUID);
                                                                                        doc.update("balanceOfCoins" , balance - 19000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                Intent i = new Intent(getBaseContext() , Store.class);
                                                                                                startActivity(i);
                                                                                                finish();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }else
                        {
                            new OurToast().myToast(getBaseContext() , getString(R.string.not_enough_cash));
                        }

                    }else if (okYear)
                    {
                        readBalance();
                        if (balance >= 70000)
                        {

                            final EditText thePassword ;
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LockRoom.this);
                            alertDialogBuilder .setIcon(R.mipmap.lock);
                            alertDialogBuilder.setTitle(getString(R.string.type_password));
                            thePassword = new EditText(LockRoom.this);
                            thePassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                            alertDialogBuilder.setView(thePassword);
                            alertDialogBuilder.setPositiveButton(getString(R.string.ok),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1)
                                        {
                                            if (thePassword.getText().toString().trim().equals(""))
                                            {

                                            }else
                                            {
                                                long pass = Long.parseLong(thePassword.getText().toString());
                                                doc = db.collection("RoomInformation").document(theUID);
                                                doc.update("pass" , pass).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        doc = db.collection("RoomInformation").document(theUID);
                                                        doc.update("startLockDate" , new Date()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Date date = new Date();
                                                                Calendar c = Calendar.getInstance();
                                                                c.setTime(date);
                                                                c.add(Calendar.YEAR, 1);
                                                                date = c.getTime();
                                                                doc.update("endLockDate" , date).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        doc.update("roomLocked" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {

                                                                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                                                DocumentReference doc = db.collection("UserInformation").document(theUID);
                                                                                doc.update("balanceOfCoins" , balance - 70000).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        Intent i = new Intent(getBaseContext() , Store.class);
                                                                                        startActivity(i);
                                                                                        finish();
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();

                        }else
                        {
                            new OurToast().myToast(getBaseContext() , getString(R.string.not_enough_cash));
                        }
                    }
                }else
                {
                    new OurToast().myToast(getBaseContext() , getString(R.string.check_your_time));
                }
            }
        });
    }
    @Override
    public void onClick(final View view) {

        switch (view.getId()) {
            case R.id.lockDay:
                dayLay.setBackgroundResource(R.drawable.gift_selector);
                monthLay.setBackgroundResource(R.drawable.selector);
                threeMonthsLay.setBackgroundResource(R.drawable.selector);
                yearLay.setBackgroundResource(R.drawable.selector);

                okDay = true;
                okMonth = false;
                okThreeMonths = false;
                okYear = false;

                readBalance();

                break;

            case R.id.lockMonth:
                dayLay.setBackgroundResource(R.drawable.selector);
                monthLay.setBackgroundResource(R.drawable.gift_selector);
                threeMonthsLay.setBackgroundResource(R.drawable.selector);
                yearLay.setBackgroundResource(R.drawable.selector);

                okDay = false;
                okMonth = true;
                okThreeMonths = false;
                okYear = false;

                readBalance();
                break;

            case R.id.lockThreeMonths:
                dayLay.setBackgroundResource(R.drawable.selector);
                monthLay.setBackgroundResource(R.drawable.selector);
                threeMonthsLay.setBackgroundResource(R.drawable.gift_selector);
                yearLay.setBackgroundResource(R.drawable.selector);

                okDay = false;
                okMonth = false;
                okThreeMonths = true;
                okYear = false;
                readBalance();
                break;

            case R.id.lockYear:
                dayLay.setBackgroundResource(R.drawable.selector);
                monthLay.setBackgroundResource(R.drawable.selector);
                threeMonthsLay.setBackgroundResource(R.drawable.selector);
                yearLay.setBackgroundResource(R.drawable.gift_selector);

                okDay = false;
                okMonth = false;
                okThreeMonths = false;
                okYear = true;
                readBalance();
                break;
        }

    }



    private void readBalance()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference doc = db.collection("UserInformation").document(theUID);
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                balance = user.getBalanceOfCoins();
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


    @Override
    public void onBackPressed() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Intent i = new Intent(getBaseContext() , Store.class);
                    startActivity(i);
                    finish();
                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }).start();

    }

}
