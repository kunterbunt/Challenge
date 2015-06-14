package sebastians.challenge;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import sebastians.challenge.data.Task;


public class AddChallengeTaskDetail extends ActionBarActivity {

    public static final String LOG_TAG = "AddChallengeTaskDetail";
    public static final String INTENT_TITLE = "TITLE",
                                INTENT_DESCRIPTION = "DESCRIPTION",
                                INTENT_TIMEAFTERPREV = "TIMEAFTERPREV";
    public static final int REQUEST_SET_DETAIL = 42;

    private String task_title, task_description;
    private int task_timeAfterPrevious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge_task_detail);
        task_title = getIntent().getStringExtra(INTENT_TITLE);
        task_description = getIntent().getStringExtra(INTENT_DESCRIPTION);
        task_timeAfterPrevious = getIntent().getIntExtra(INTENT_TIMEAFTERPREV, 1);
    }

    public String getTaskTitle() {
        return task_title;
    }

    public void setTaskTitle(String task_title) {
        this.task_title = task_title;
    }

    public String getTaskDescription() {
        return task_description;
    }

    public void setTaskDescription(String task_description) {
        this.task_description = task_description;
    }

    public int getTaskTimeAfterPrevious() {
        return task_timeAfterPrevious;
    }

    public void setTaskTimeAfterPrevious(int task_timeAfterPrevious) {
        this.task_timeAfterPrevious = task_timeAfterPrevious;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_challenge_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            Intent sendBackIntent = new Intent();
            sendBackIntent.putExtra(INTENT_TITLE, getTaskTitle());
            sendBackIntent.putExtra(INTENT_DESCRIPTION, getTaskDescription());
            sendBackIntent.putExtra(INTENT_TIMEAFTERPREV, getTaskTimeAfterPrevious());
            setResult(Activity.RESULT_OK, sendBackIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
