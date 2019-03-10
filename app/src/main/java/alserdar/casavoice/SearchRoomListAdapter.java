package alserdar.casavoice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.OnlineModel;
import alserdar.casavoice.models.RoomModel;
import alserdar.casavoice.models.UserModel;

/**
 * Created by ALAZIZY on 11/28/2017.
 */


public class SearchRoomListAdapter extends BaseAdapter implements Adapter {

    private Context c  ;
    private List<RoomModel> list ;
    private int counter = 0;

    SearchRoomListAdapter(Context context, List<RoomModel> model) {

        c = context ;
        list = model ;
    }

    public void setUp (List<RoomModel> model)
    {
        list = model ;
    }

    @Override
    public int getCount() {
        if (list != null){
            return list.size();
        }
        return 0 ;
    }


    @Override
    public Object getItem(int i) {
        if (list != null){
            return list.get(i);
        }
        return null ;
    }

    @Override
    public long getItemId(int i)
    {
        if (list != null) {
            return list.get(i).hashCode();
        }
        return 0;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        final String facebookUserName , theUID , nickName;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.search_room_list_adapter, parent, false);
        }

        final RoomModel model = (RoomModel) getItem(position);
        facebookUserName = DetectUserInfo.faceBookUser(view.getContext());
        theUID = DetectUserInfo.theUID(view.getContext());
        nickName = DetectUserInfo.myUserName(view.getContext());
        final DocumentReference doc = db.collection("UserInformation").document(model.getRoomOwnerUID());
        final View finalView = view;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists())
                            {
                                UserModel user = documentSnapshot.toObject(UserModel.class);
                                String userName = user.getUserName();
                                final ImageView roomProfilePicture = finalView.findViewById(R.id.searchRoomProfilePicture);
                                final ImageView peopleInTheRoomLocked = finalView.findViewById(R.id.searchPeopleInTheRoom);
                                TextView roomName = finalView.findViewById(R.id.searchRoomNameTextView);
                                TextView infoAboutRoom = finalView.findViewById(R.id.searchInfoAboutRoom);
                                TextView still = finalView.findViewById(R.id.searchStillEmpty);
                                roomName.setText(model.getRoomName());
                                still.setText(userName);
                                infoAboutRoom.setText(model.getInfoAboutRoom());

                                LoadingPics.loadPicForRooms(c.getApplicationContext() , model.getRoomOwnerUID() , roomProfilePicture);

                                if (model.isRoomLocked())
                                {
                                    peopleInTheRoomLocked.setBackgroundResource(R.mipmap.lock);
                                }else
                                {

                                }
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


        final View finalView1 = view;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    //final View finalView1 = view;
                    finalView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {


                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            DocumentReference doc = db.collection("RoomInformation").document(model.getRoomOwnerUID());
                            doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.exists())
                                    {
                                        final RoomModel room = documentSnapshot.toObject(RoomModel.class);

                                        if (room.isRoomLocked())
                                        {

                                            final EditText thePassword ;
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                                            alertDialogBuilder .setIcon(R.mipmap.lock);
                                            alertDialogBuilder.setTitle(c.getString(R.string.type_password));
                                            thePassword = new EditText(c);
                                            thePassword.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            alertDialogBuilder.setView(thePassword);
                                            alertDialogBuilder.setPositiveButton(c.getString(R.string.ok),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface arg0, int arg1)
                                                        {
                                                            if (thePassword.getText().toString().trim().equals(""))
                                                            {

                                                            }else
                                                            {
                                                                if (room.getPass() == Long.parseLong(thePassword.getText().toString()))
                                                                {

                                                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                    DatabaseReference myOnlineList = database.getReference(model.getRoomOwnerUID() + " Online").push();
                                                                    myOnlineList.setValue(new OnlineModel(facebookUserName , theUID , nickName));
                                                                    Intent enterRoom = new Intent(finalView1.getContext() , LiveRoom.class);
                                                                    enterRoom.putExtra("roomIdInTheRoom" , model.getRoomId());
                                                                    enterRoom.putExtra("roomNameInTheRoom" , model.getRoomName());
                                                                    enterRoom.putExtra("ownerRoom" , model.getRoomOwner());
                                                                    enterRoom.putExtra("onlineGuys" , facebookUserName);
                                                                    enterRoom.putExtra("roomOwnerUID" , model.getRoomOwnerUID());
                                                                    enterRoom.putExtra("onlineGuysUID" , theUID);
                                                                    finalView1.getContext().startActivity(enterRoom);
                                                                }else
                                                                {
                                                                    new OurToast().myToast(c, c.getString(R.string.wrong_password));
                                                                }
                                                            }
                                                        }
                                                    });
                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                        }else
                                        {
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference myOnlineList = database.getReference(model.getRoomOwnerUID() + " Online").push();
                                            myOnlineList.setValue(new OnlineModel(facebookUserName , theUID , nickName));

                                            Intent enterRoom = new Intent(finalView1.getContext() , LiveRoom.class);
                                            enterRoom.putExtra("roomIdInTheRoom" , model.getRoomId());
                                            enterRoom.putExtra("roomNameInTheRoom" , model.getRoomName());
                                            enterRoom.putExtra("ownerRoom" , model.getRoomOwner());
                                            enterRoom.putExtra("roomOwnerUID" , model.getRoomOwnerUID());
                                            enterRoom.putExtra("onlineGuys" , facebookUserName);
                                            enterRoom.putExtra("onlineGuysUID" , theUID);
                                            finalView1.getContext().startActivity(enterRoom);
                                        }

                                    }else
                                    {

                                    }

                                }
                            });


                        }
                    });
                }catch (Exception e)
                {
                    e.getMessage();
                }
            }
        }).start();

        return view;
    }

}
