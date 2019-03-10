package alserdar.casavoice.timed_things_stuff;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import alserdar.casavoice.BuildConfig;

public class DailyResetBroadCast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.

        resetTheDaily(context);

    }

    public void resetTheDaily(Context context) {



        PendingIntent pIntent = createPendingIntent(context);

        Calendar now = Calendar.getInstance();

        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        Date date = parseDate(hour + ":" + minute);
        String compareStringOne = "23:59";
        Date dateCompareOne = parseDate(compareStringOne);
     // run in development
        if(BuildConfig.DEBUG){
            now.set(Calendar.MINUTE,minute+2);
        setDaily(context,now.getTime(),pIntent);
        return ;
        }
        // in production
        if ( dateCompareOne.compareTo(date) <= 0) {
            setDaily(context , date , pIntent);
        }



        /*
         try {

            Calendar calendar = Calendar.getInstance();
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
                    setDaily(context , calendar , pIntent);
                    break;
                }else
                {

                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
         */

    }


    SimpleDateFormat inputParser = new SimpleDateFormat("HH:mm");


    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    private static void setDaily(Context context , Date date , PendingIntent pIntent) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pIntent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pIntent);
        }
    }

    private static PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, AlarmBroadcast.class);
        return PendingIntent.getBroadcast(context, 1001, intent, PendingIntent.FLAG_UPDATE_CURRENT);


    }
}
