package alserdar.casavoice.check_internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zizo on 2/29/2016.
 */
public class NetworkUtil {

    public static int TYPE_NOT_CONNECTED = 0;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;


    private static int getConnectivityType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
            {
                if (activeNetwork.isConnected() || activeNetwork.isAvailable() || activeNetwork.isConnectedOrConnecting())
                    return TYPE_WIFI;
            }


            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                if (activeNetwork.isConnected()|| activeNetwork.isAvailable() || activeNetwork.isConnectedOrConnecting())
                    return TYPE_MOBILE;
            }

        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityType(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI || conn == NetworkUtil.TYPE_MOBILE) {
            status = "Internet enabled";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
            status = "Check Internet !!";
        }
        return status;
    }
}