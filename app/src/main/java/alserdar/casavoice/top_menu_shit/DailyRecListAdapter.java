package alserdar.casavoice.top_menu_shit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import alserdar.casavoice.R;
import alserdar.casavoice.UserProfileInformation;
import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.UserModel;

/**
 * Created by ALAZIZY on 11/28/2017.
 */


public class DailyRecListAdapter extends BaseAdapter implements Adapter {

    private Context c  ;
    private List<UserModel> list ;
    private int counter = 0;

    public DailyRecListAdapter(Context context , List<UserModel> model ) {

        c = context ;
        list = model ;
    }

    public void setUp (List<UserModel> model)
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
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.daily_rec_list, parent, false);
        }

        final UserModel model = (UserModel) getItem(position);

        final ImageView picRecDaily = view.findViewById(R.id.imageForRecDaily);
        final TextView theSender = view.findViewById(R.id.userNameForRecDaily);
        TextView howMuch = view.findViewById(R.id.muchSendForRecDaily);

        theSender.setText(model.getUserName());
        howMuch.setText(String.valueOf(model.getDailyRecBalance()));

        final String originalName = model.getOriginalName();
        final String originalUID = model.getTheUID();
        LoadingPics.loadPicsForTopMenu(c.getApplicationContext() , originalUID , picRecDaily);

        final String facebookUserName = DetectUserInfo.theUID(c.getApplicationContext());


        final View finalView1 = view;
        theSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if (originalUID.equals(facebookUserName) ||
                        theSender.getText().toString().equals("Casavoice")) {

                } else {

                    Intent iProf = new Intent(c, UserProfileInformation.class);
                    iProf.putExtra("hisOriginalName", originalName);
                    iProf.putExtra("hisUserName", facebookUserName);
                    iProf.putExtra("hisOriginalUID", originalUID);
                    c.startActivity(iProf);

                    /*
                     DatabaseReference amBlocked = FirebaseDatabase.getInstance()
                            .getReference(theSender.getText().toString() + " HisBlockList");
                    amBlocked.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                if (data.child("blockMe").getValue().equals(theSender.getText().toString())) {
                                    new OurToast().myToast(c, String.format(" %s %s " , c.getString(R.string.you_are_blocked_from) , theSender.getText().toString()));

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                     */

                }
            }
        });
        return view;
    }

}
