package alserdar.casavoice.fab_setting_classes.spin_wheels;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.fab_setting_classes.spin_wheels.library.LuckyWheelView;
import alserdar.casavoice.fab_setting_classes.spin_wheels.library.model.LuckyItem;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.models.UserModel;

public class SpinWheels extends AppCompatActivity {
    List<LuckyItem> data = new ArrayList<>();
    private String theUID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  DocumentReference doc = null;
    Button spinButton ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheels);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        theUID = DetectUserInfo.theUID(getBaseContext());

        spinButton = findViewById(R.id.spin);

        final LuckyWheelView luckyWheelView = findViewById(R.id.luckyWheel);

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.text = "10";
        luckyItem1.icon = R.mipmap.bike;
        luckyItem1.color = 0xffFFF3E0;
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.text = "50";
        luckyItem2.icon = R.mipmap.choclate;
        luckyItem2.color = 0xFF6A1B9A;
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.text = "100";
        luckyItem3.icon = R.mipmap.coffee;
        luckyItem3.color = 0xFF0091EA;
        data.add(luckyItem3);

        //////////////////
        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.text = "150";
        luckyItem4.icon = R.mipmap.cupcake;
        luckyItem4.color = 0xFF18FFFF;
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.text = "200";
        luckyItem5.icon = R.mipmap.star_tail;
        luckyItem5.color = 0xFFFFD740;
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.text = "250";
        luckyItem6.icon = R.mipmap.horse;
        luckyItem6.color = 0xFFE91E63;
        data.add(luckyItem6);
        //////////////////

        //////////////////////
        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.text = "300";
        luckyItem7.icon = R.mipmap.fantasy;
        luckyItem7.color = 0xffFFF3E0;
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.text = "350";
        luckyItem8.icon = R.mipmap.unicorn;
        luckyItem8.color = 0xFF6A1B9A;
        data.add(luckyItem8);


        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.text = "400";
        luckyItem9.icon = R.mipmap.icecream;
        luckyItem9.color = 0xFF0091EA;
        data.add(luckyItem9);
        ////////////////////////

        LuckyItem luckyItem10 = new LuckyItem();
        luckyItem10.text = "450";
        luckyItem10.icon = R.mipmap.watch;
        luckyItem10.color = 0xFF18FFFF;
        data.add(luckyItem10);

        LuckyItem luckyItem11 = new LuckyItem();
        luckyItem11.text = "500";
        luckyItem11.icon = R.mipmap.three_ballons;
        luckyItem11.color = 0xFFFFD740;
        data.add(luckyItem11);

        LuckyItem luckyItem12 = new LuckyItem();
        luckyItem12.text = "550";
        luckyItem12.icon = R.mipmap.teddy_rose;
        luckyItem12.color = 0xFFFF1744;
        data.add(luckyItem12);

        /////////////////////

        luckyWheelView.setData(data);
        luckyWheelView.setRound(getRandomRound());

        /*
         luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);

         */


        spinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doc = db.collection("UserInformation").document(theUID);
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModel user = documentSnapshot.toObject(UserModel.class);
                        boolean spin = user.isSpin();

                        if (spin)
                        {
                            new OurToast().myToast(getBaseContext() , getString(R.string.spin_start));

                        }else
                        {
                            int index = getRandomIndex();
                            luckyWheelView.startLuckyWheelWithTargetIndex(index);
                            doc.update("spin" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                        }
                    }
                });
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {

                configureToastAndBalance(index);
                //Toast.makeText(getApplicationContext(), String.valueOf(index), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private void getToast(int index)
    {
        new OurToast().myToast(getBaseContext() , String.format(" %s %s %s " , getString(R.string.congrats_you_got) ,
                String.valueOf(index) , getString(R.string.coins_in_your_balance)));

    }


    private void addBalance(final int muchCoins)
    {
        doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    int balance = user.getBalanceOfCoins();

                    int getCoins = balance + muchCoins ;
                    doc.update("balanceOfCoins" , getCoins).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            doc.update("spin" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                }
                            });

                        }
                    });
                   // balance.setText(String.format("%s%s", getString(R.string.your_balance), String.valueOf(user.getBalanceOfCoins())));

                }
            }
        });
    }

    private void configureToastAndBalance(int index)
    {
        switch (index)
        {
            case 0 :
                getToast(10);
                addBalance(10);
                break;

            case 1:
                getToast(10);
                addBalance(10);
                break;

            case 2 :
                getToast(50);
                addBalance(50);
                break;

            case 3 :
                getToast(100);
                addBalance(100);
                break;

            case 4 :
                getToast(150);
                addBalance(150);
                break;

            case 5 :
                getToast(200);
                addBalance(200);
                break;

            case 6 :
                getToast(250);
                addBalance(250);
                break;

            case 7 :
                getToast(300);
                addBalance(300);
                break;

            case 8 :
                getToast(350);
                addBalance(350);
                break;

            case 9 :
                getToast(400);
                addBalance(400);
                break;

            case 10 :
                getToast(450);
                addBalance(450);
                break;

            case 11 :
                getToast(500);
                addBalance(500);
                break;

            case 12 :
                getToast(550);
                addBalance(500);
                break;

        }
    }

    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(data.size() - 1) + 0;
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }
}
