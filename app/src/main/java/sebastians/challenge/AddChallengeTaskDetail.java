package sebastians.challenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import sebastians.challenge.data.ImagePath;
import sebastians.challenge.tools.PhotoManager;


public class AddChallengeTaskDetail extends ActionBarActivity {

    public static final String LOG_TAG = "AddChallengeTaskDetail";
    public static final String INTENT_TITLE = "TITLE",
                                INTENT_DESCRIPTION = "DESCRIPTION",
                                INTENT_DURATION = "TIMEAFTERPREV",
                                INTENT_IMAGEPATHLIST = "IMAGEPATHLIST";
    public static final int REQUEST_SET_DETAIL = 42;

    private String task_title, task_description;
    private int task_timeAfterPrevious;
    private List<ImagePath> task_imagePaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge_task_detail);
        task_title = getIntent().getStringExtra(INTENT_TITLE);
        task_description = getIntent().getStringExtra(INTENT_DESCRIPTION);
        task_timeAfterPrevious = getIntent().getIntExtra(INTENT_DURATION, 1);
        task_imagePaths = ImagePath.convertToImagePathList(getIntent().getStringArrayListExtra(INTENT_IMAGEPATHLIST));
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
            // Up button.
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Save changed data.
        Intent sendBackIntent = new Intent();
        sendBackIntent.putExtra(INTENT_TITLE, getTaskTitle());
        sendBackIntent.putExtra(INTENT_DESCRIPTION, getTaskDescription());
        sendBackIntent.putExtra(INTENT_DURATION, getTaskDuration());
        sendBackIntent.putStringArrayListExtra(INTENT_IMAGEPATHLIST, (ArrayList) ImagePath.convertToStringList(task_imagePaths));
        setResult(Activity.RESULT_OK, sendBackIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass on intents to fragment.
        if (requestCode == PhotoManager.REQUEST_TAKE_PHOTO || requestCode == PhotoManager.REQUEST_PICK_PHOTO) {
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

    public List<ImagePath> getTaskImagePaths() {
        return task_imagePaths;
    }

    public void setTaskImagePaths(List<ImagePath> task_imagePaths) {
        this.task_imagePaths = task_imagePaths;
    }
}
