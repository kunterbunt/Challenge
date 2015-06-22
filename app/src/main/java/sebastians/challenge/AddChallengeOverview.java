package sebastians.challenge;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import sebastians.challenge.data.Challenge;
import sebastians.challenge.tools.DatabaseHelper;
import sebastians.challenge.dialogs.ButtonDialog;
import sebastians.challenge.dialogs.EditTextDialog;


public class AddChallengeOverview extends ActionBarActivity {

    public static final String LOG_TAG = "AddChallengeOverview";
    public static final int REQUEST_NEW_CHALLENGE = 12;

    private Challenge challenge;
    /** If the user presses the back button and chooses to discard, this keeps track of it
     * and allows going back then.
     */
    private boolean justPressedBackButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_challenge_overview);
        challenge = new Challenge();
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Slide(Gravity.BOTTOM));
            getWindow().setReturnTransition(new Slide(Gravity.BOTTOM));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_challenge_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                save();
                return true;
            case android.R.id.home:
                if (getChallenge().getTaskList().size() > 0) {
                    new ButtonDialog(this, null, getString(R.string.save), getString(R.string.discard), getString(R.string.cancel), null) {
                        @Override
                        public void onPositiveButtonClick() {
                            save();
                        }

                        @Override
                        public void onNegativeButtonClick() {
                            // Do nothing.
                        }

                        @Override
                        public void onNeutralButtonClick() {
                            justPressedBackButton = true;
                            onBackPressed();
                        }
                    };
                    return true;
                } else {
                    justPressedBackButton = true;
                    onBackPressed();
                }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Checks whether the created challenge is valid.
     * If so, the name is queried from the user and
     * the whole thing is saved to the database and the activity finishes.
     */
    private void save() {
        // Are there any tasks?
        if (getChallenge().getTaskList().size() == 0) {
            Toast.makeText(this, "A challenge without tasks is boring, man.", Toast.LENGTH_SHORT).show();
            return;
        }

        final Activity activity = this;
        // Ask for a name.
        new EditTextDialog(this, getString(R.string.saveChallenge), getString(R.string.save), null, getString(R.string.cancel), null) {
            @Override
            public void onPositiveButtonClick(EditText input) {
                String name = input.getText().toString();
                // Check if name is valid.
                if (name.equals("")) {
                    Toast.makeText(activity, "Hey! That's not a name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                getChallenge().setName(name);

                // Save to database.
                Challenge dbChallenge = DatabaseHelper.getInstance().create(getChallenge());
                Log.i(LOG_TAG, "Saved Challenge: " + dbChallenge.getName());
                setResult(Activity.RESULT_OK);
                finish();
                overridePendingTransition(R.anim.slide_in_top, R.anim.abc_fade_out);
            }

            @Override
            public void onNegativeButtonClick(EditText input) {
                // Do nothing.
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (!justPressedBackButton && getChallenge().getTaskList().size() > 0) {
            new ButtonDialog(this, null, getString(R.string.save), getString(R.string.discard), getString(R.string.cancel), null) {
                @Override
                public void onPositiveButtonClick() {
                    save();
                }

                @Override
                public void onNegativeButtonClick() {
                    // Do nothing.
                }

                @Override
                public void onNeutralButtonClick() {
                    justPressedBackButton = true;
                    onBackPressed();
                }
            };
        } else {
            justPressedBackButton = false;
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_top, R.anim.abc_fade_out);
        }
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }
}
