package alserdar.casavoice.timed_things_stuff;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.format.Time;

import java.text.DateFormat;
import java.util.Date;

public class CheckTime {

public static boolean checkTimeReality (Context context )
    {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                return Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME) == 1 &&
                        Settings.Global.getInt(context.getContentResolver(), Settings.Global.AUTO_TIME_ZONE) == 1;
            }else
            {
                return Settings.System.getInt(context.getContentResolver(), Settings.System.AUTO_TIME) == 1 &&
                        Settings.System.getInt(context.getContentResolver(), Settings.System.AUTO_TIME_ZONE) == 1;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return true ;
    }


    private void getTheDate()
    {
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());

    }

    private void getTheTime()
    {
        Time t = new Time(Time.getCurrentTimezone());
        t.setToNow();
    }
}
