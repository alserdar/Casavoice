package alserdar.casavoice.timed_things_stuff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ResetWeeklyBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    gotChanged(context);
                }catch (RuntimeException e )
                {
                    e.getMessage();
                }
            }
        }).start();
    }

    public void gotChanged(Context context) {
        Intent i = new Intent(context , ResetWeeklyService.class);
        context.startService(i);
    }
}
