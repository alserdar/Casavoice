package alserdar.casavoice.fab_setting_classes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import alserdar.casavoice.OurToast;
import alserdar.casavoice.R;
import alserdar.casavoice.UserProfileInformation;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.MyMessageListModel;
import alserdar.casavoice.models.PrivateChatModel;
import alserdar.casavoice.models.UserModel;

public class ChatPrivatly extends AppCompatActivity {


    ListView listChatOnPrivate ;
    TextView whosChat ;
    EditText typeMessagePrivateChat ;
    Button sendMessagePrivateChat ;
    String to , toUID ,facebookUserName , theUID;
    Long theSum ;
    private Boolean check = false;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference doc;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_privatly);
        ActionBar bar = getSupportActionBar();
        assert bar != null;
        bar.hide();


        listChatOnPrivate = findViewById(R.id.listForPrivateChat);
        whosChat = findViewById(R.id.whosChatPrivateChat);
        typeMessagePrivateChat = findViewById(R.id.typeMessagePrivateChat);
        sendMessagePrivateChat = findViewById(R.id.sentPrivteChat);


        sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss" , Locale.getDefault());

        to = this.getIntent().getExtras().getString("to");
        toUID = this.getIntent().getExtras().getString("toUID");
        theSum = this.getIntent().getExtras().getLong("theSum");
        facebookUserName = DetectUserInfo.faceBookUser(getBaseContext());
        theUID = DetectUserInfo.theUID(getBaseContext());

        doc = db.collection("UserInformation").document(toUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    UserModel user = documentSnapshot.toObject(UserModel.class);
                    String toUser = user.getUserName();
                    whosChat.setText(toUser);
                }else
                {

                }

            }
        });


        isItFalseOrNot();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (typeMessagePrivateChat.getText().toString().equals(""))
                {
                    sendMessagePrivateChat.setEnabled(false);
                }else
                {
                    sendMessagePrivateChat.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        typeMessagePrivateChat.addTextChangedListener(watcher);

        sendMessagePrivateChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setUpMyMessageList();
                setUpHisMessageList();

                doc = db.collection("UserInformation").document(theUID);
                doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists())
                        {
                            UserModel user = documentSnapshot.toObject(UserModel.class);
                            String myNickName = user.getUserName();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference(theSum + " PrivateChat").push();
                            myRef.setValue(new PrivateChatModel(typeMessagePrivateChat.getText().toString() ,
                                    facebookUserName , myNickName , sdf.format(new Date()) ,theUID ));

                            typeMessagePrivateChat.setText("");
                        }else
                        {

                        }

                    }
                });
                doc = db.collection("UserInformation").document(toUID);
                doc.update("hasMessages" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        doc = db.collection("UserInformation").document(theUID);
                        doc.update("redMe" , true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                //  new YouHaveNewMessageBroadcast().gotMessages(getBaseContext());

                            }
                        });
                    }
                });

            }
        });

        displayPrivateChat();
    }

    private void displayPrivateChat() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    FirebaseListAdapter<PrivateChatModel> adapter =
                            new FirebaseListAdapter<PrivateChatModel>(getBaseContext(), PrivateChatModel.class, R.layout.list_private_chat,
                                    FirebaseDatabase.getInstance().getReference(theSum+" PrivateChat")) {
                                @Override
                                protected void populateView(View v, final PrivateChatModel model, int position) {


                                    TextView messageText = v.findViewById(R.id.textMessageForPrivateChat);
                                    TextView messageUser = v.findViewById(R.id.userNameMessageForPrivateChat);
                                    final ImageView ppForChat = v.findViewById(R.id.avatarForPrivateChat);

                                    messageText.setText(model.getPrivateMessageText());
                                    messageUser.setText(model.getMessageFromNickName());

                                    LoadingPics.loadPicsForHome(getBaseContext().getApplicationContext() , model.getMessageFromUID()
                                            , ppForChat);

                                    messageUser.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (model.getMessageFromUID().equals(theUID))
                                            {

                                            }else
                                            {
                                                StringTokenizer st = new StringTokenizer(model.getMessageFrom(), " ");
                                                String  userName = st.nextToken();
                                                new OurToast().myToast(getBaseContext() , userName);
                                                Intent iProf = new Intent(getBaseContext() , UserProfileInformation.class);
                                                iProf.putExtra("hisOriginalName" , userName);
                                                iProf.putExtra("hisOriginalUID" , model.getMessageFromUID());
                                                startActivity(iProf);
                                            }
                                        }
                                    });

                                }
                            };

                    listChatOnPrivate.setAdapter(adapter);

                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }).start();

    }

    private void setUpMyMessageList ()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    if (isItFalseOrNot())
                    {

                    }else
                    {


                        doc = db.collection("UserInformation").document(theUID);
                        doc.update(theSum + " pc", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase myMessage = FirebaseDatabase.getInstance();
                                final DatabaseReference mine = myMessage.getReference(theUID + " myMessageList").push();
                                mine.setValue(new MyMessageListModel(to ,  sdf.format(new Date()) , toUID));

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

    private void setUpHisMessageList () {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    if (isItFalseOrNot())
                    {

                    }else
                    {
                        doc = db.collection("UserInformation").document(toUID);
                        doc.update(theSum + " pc", true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase hisMessage = FirebaseDatabase.getInstance();
                                final DatabaseReference him = hisMessage.getReference(toUID + " myMessageList").push();
                                him.setValue(new MyMessageListModel(facebookUserName ,  sdf.format(new Date()) , theUID));
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

    private boolean isItFalseOrNot ()
    {

        doc = db.collection("UserInformation").document(theUID);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists())
                {
                    check = documentSnapshot.getBoolean(theSum + " pc");
                }else
                {

                }
            }
        });

        return check ;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
