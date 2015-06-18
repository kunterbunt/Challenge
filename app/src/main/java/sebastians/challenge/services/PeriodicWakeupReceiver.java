package sebastians.challenge.services;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.ArrayList;

import sebastians.challenge.ChallengeDetail;
import sebastians.challenge.R;
import sebastians.challenge.data.Challenge;
import sebastians.challenge.tools.DatabaseHelper;
import sebastians.challenge.data.Task;

/**
 * Created by sebastian on 14/06/15.
 */
public class PeriodicWakeupReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 134;
    public static final String LOG_TAG ="timer";

    //set update interval to 1/2 minute
    public static final long UPDATE_INTERVAL = 10 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        //get active challenges from database
        DatabaseHelper.init(context);
        DatabaseHelper db = DatabaseHelper.getInstance();
        ArrayList<Challenge> activeChallenges = (ArrayList<Challenge>) db.getActiveChallenges();
//        Log.i(LOG_TAG, "Active Challenges: " + activeChallenges.size());

        for(int i = 0; i < activeChallenges.size(); i++){
            Challenge challenge = activeChallenges.get(i);


            Task task = challenge.getDueTask();
            int dueTaskId = challenge.getDueTaskId();
            if(task == null){
                //set challenge not active! if all tasks are done
                challenge.setActive(false);
                db.update(challenge);
                //TODO SEND MESSAGE TO USER -> NOTIFICATION bla bla bla
                //INFORM CONNECTED FRIENDS
            }else{
                Log.i(LOG_TAG, "Active Task: " + task.getTitle());

                if(!task.isDone() && !task.isDismissed()){
                    //notify user about pending task
                    //With NOTIFICATION!
                    Intent resultIntent = new Intent(context, ChallengeDetail.class);
                    resultIntent.putExtra(ChallengeDetail.INTENT_CHALLENGE_ID, challenge.getDatabaseId());
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addParentStack(ChallengeDetail.class);
                    stackBuilder.addNextIntent(resultIntent);
                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );


                    Intent dismissIntent = new Intent(context, DismissService.class);
                    dismissIntent.putExtra(DismissService.INTENT_TASK_ID, task.getDatabaseId());
                    PendingIntent dismissIntentPendingIntent = PendingIntent.getService(context, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(context)
                                    .setSmallIcon(R.drawable.button_fab)
                                    .setContentTitle(task.getTitle())
                                    .setContentText(task.getDescription())
                                    .addAction(R.drawable.button_fab, "Dismiss", dismissIntentPendingIntent);




                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    // mId allows you to update the notification later on.
                    mNotificationManager.notify((int)task.getDatabaseId(), mBuilder.build());

                    for(int tskId = 0; tskId < dueTaskId; tskId++){
                        //delete potential old task notifications from notificationbar
                        Task myTask = challenge.getTaskList().get(tskId);
                        mNotificationManager.cancel((int)myTask.getDatabaseId());
                    }


                }



            }



        }



    }



    public static void setAlarm(Context context){
        PendingIntent pendingIntent;
        AlarmManager manager;
        Log.i(LOG_TAG, "scheduled Alarm");
        manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, PeriodicWakeupReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), UPDATE_INTERVAL, pendingIntent);

    }
}

