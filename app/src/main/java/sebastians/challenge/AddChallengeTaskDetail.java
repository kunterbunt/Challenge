package sebastians.challenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import sebastians.challenge.tools.PhotoManager;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_challenge_task_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.save:
                Intent sendBackIntent = new Intent();
                sendBackIntent.putExtra(INTENT_TITLE, getTaskTitle());
                sendBackIntent.putExtra(INTENT_DESCRIPTION, getTaskDescription());
                sendBackIntent.putExtra(INTENT_TIMEAFTERPREV, getTaskDuration());
                setResult(Activity.RESULT_OK, sendBackIntent);
                finish();
                break;
            // Up button.
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass on photo taking intent to fragment.
        if (requestCode == PhotoManager.REQUEST_TAKE_PHOTO) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
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

    public int getTaskDuration() {
        return task_timeAfterPrevious;
    }

    public void setTaskDuration(int task_timeAfterPrevious) {
        this.task_timeAfterPrevious = task_timeAfterPrevious;
    }
}
