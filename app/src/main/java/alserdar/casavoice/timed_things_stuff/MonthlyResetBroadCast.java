package alserdar.casavoice.timed_things_stuff;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class MonthlyResetBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        resetTheMonth(context);

    }

    public static void resetTheMonth(Context context) {
        try {

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH , 1);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 00);
            calendar.set(Calendar.MILLISECOND, 0);

            PendingIntent pIntent = createPendingIntent(context);
            for (long rightNow = calendar.getTimeInMillis();
                 rightNow > 0 && calendar.compareTo(Calendar.getInstance()) > 0 ; rightNow++)
            {
                if (rightNow > 0)
                {
                    setMonthly(context , calendar , pIntent);
                    break;
                }else
                {

                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }


    private static void setMonthly(Context context, Calendar calendar, PendingIntent pIntent) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() , pIntent);
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, MonthlyResetService.class);
        return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


    }
}
