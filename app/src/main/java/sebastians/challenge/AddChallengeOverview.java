package sebastians.challenge;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import sebastians.challenge.data.Challenge;
import sebastians.challenge.data.DatabaseHelper;
import sebastians.challenge.data.ImagePath;
import sebastians.challenge.dialogs.EditTextDialog;


public class AddChallengeOverview extends ActionBarActivity {

    public static final String LOG_TAG = "AddChallengeOverview";
    public static final int REQUEST_NEW_CHALLENGE = 12;

    private Challenge challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge_overview);
        challenge = new Challenge("no name");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_challenge_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (!((AddChallengeOverviewFragment) currentFragment).hasTasks()) {
                Toast.makeText(this, "A challenge without tasks is boring, man.", Toast.LENGTH_SHORT).show();
                return true;
            }

            final Activity activity = this;
            new EditTextDialog(this, getString(R.string.saveChallenge), getString(R.string.save), null, getString(R.string.cancel), null) {
                @Override
                public void onPositiveButtonClick(EditText input) {
                    String name = input.getText().toString();
                    if (name.equals("")) {
                        Toast.makeText(activity, "Hey! That's not a name!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    getChallenge().setName(name);

                    Challenge dbChallenge = DatabaseHelper.getInstance().create(getChallenge());
                    Log.i(LOG_TAG, "Saved Challenge: " + dbChallenge.getName() + "\n" + dbChallenge.getDescription() + "\n"
                        + dbChallenge.getTaskList().size());
                    setResult(Activity.RESULT_OK);
                    finish();
                }

                @Override
                public void onNegativeButtonClick(EditText input) {
                    // Do nothing.
                }
            };
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
