package alserdar.casavoice;

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

import alserdar.casavoice.load_stuff.DetectUserInfo;
import alserdar.casavoice.load_stuff.LoadingPics;
import alserdar.casavoice.models.UserModel;

/**
 * Created by ALAZIZY on 11/28/2017.
 */


public class SearchUserListAdapter extends BaseAdapter implements Adapter {

    private Context c  ;
    private List<UserModel> list ;

    SearchUserListAdapter(Context context, List<UserModel> model) {

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

        final String theUID ;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.search_user_list_adapter, parent, false);
        }

        final UserModel model = (UserModel) getItem(position);

        theUID = DetectUserInfo.theUID(c.getApplicationContext());

        TextView userIs = view.findViewById(R.id.userSearched);
        ImageView userImageIs = view.findViewById(R.id.userImageSearched);
        userIs.setText(model.getUserName());
        final String theRealName = model.getOriginalName();
        final String theRealUID = model.getTheUID() ;
        LoadingPics.loadPicsForHome(view.getContext().getApplicationContext() , model.getTheUID() , userImageIs);

        userIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getTheUID().equals(theUID))
                {

                }else
                {
                    Intent i = new Intent(c , UserProfileInformation.class);
                    i.putExtra("hisOriginalName" , theRealName);
                    i.putExtra("hisOriginalUID" , theRealUID);
                    view.getContext().startActivity(i);
                }
            }
        });
        return view;
    }

}
