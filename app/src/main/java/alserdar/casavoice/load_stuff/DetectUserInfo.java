package alserdar.casavoice.load_stuff;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ALAZIZY on 2/28/2018.
 */

public class DetectUserInfo {

    public static String faceBookUser (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("FacebookUser" , "FacebookUser");
    }

    public static String myUserName (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("myUserName" , "myUserName");
    }

    public static String faceBookId (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("user_id" , "user_id");
    }

    public static String theUID (Context context)
    {
        SharedPreferences getInfo = PreferenceManager.getDefaultSharedPreferences(context);
        return getInfo.getString("uid" , "uid");
    }
}
