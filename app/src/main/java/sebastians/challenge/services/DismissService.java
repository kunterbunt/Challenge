package sebastians.challenge.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import sebastians.challenge.tools.DatabaseHelper;
import sebastians.challenge.data.Task;

/**
 * Dismiss Task from Notification!
 */
public class DismissService extends IntentService {
    public static final String INTENT_TASK_ID = "TASK_ID";

    public DismissService(String name) {
        super(name);
    }

    public DismissService(){
        super("DismissService");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        long taskId = intent.getLongExtra(DismissService.INTENT_TASK_ID, -1);
        Log.i(INTENT_TASK_ID, "TASK ID" + intent.getStringExtra(DismissService.INTENT_TASK_ID));
        DatabaseHelper.init(getApplicationContext());
        DatabaseHelper db = DatabaseHelper.getInstance();

        Task task = db.getTaskById(taskId);
        task.setDismissed(true);

        db.update(task);

        //finally cancel notification
        NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel((int)taskId);
    }
}
