package alserdar.casavoice.alert_stuff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class YouHaveNewMessageBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {


        //  Log.i(YouHaveNewNotificationBroadcast.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, YouHaveNewMessageService.class));;
    }
}
