package sebastians.challenge.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sebastian on 14/06/15.
 */
public class PeriodicWakeupReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 134;
    public static final String LOG_TAG ="timer";

    //set update interval to 1 minute
    public static final long UPDATE_INTERVAL = 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "WAKEUP", Toast.LENGTH_SHORT).show();
         Log.i(LOG_TAG, "Woke Up");

    }

    public static void setAlarm(Context context){
        PendingIntent pendingIntent;
        AlarmManager manager;
Log.i(LOG_TAG, "scheduled Alarm");
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, PeriodicWakeupReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), UPDATE_INTERVAL, pendingIntent);
        Toast.makeText(context, "Alarm Set", Toast.LENGTH_SHORT).show();
    }
}

