package sebastians.challenge;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import sebastians.challenge.data.Challenge;
import sebastians.challenge.tools.DatabaseHelper;


public class ChallengeDetail extends ActionBarActivity {

    /** Put an intent extra into the calling intent to provide the database id of the challenge you want to show. */
    public static final String INTENT_CHALLENGE_ID = "CHALLENGE_ID";
    public static final String LOG_TAG = "ChallengeDetail";
    private Challenge mChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_detail);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Slide(Gravity.RIGHT));
            getWindow().setReturnTransition(new Explode());
        }

        long challengeId = getIntent().getLongExtra(INTENT_CHALLENGE_ID, -1);
        if (challengeId == -1)
            throw new IllegalArgumentException("No challenge database ID provided to ChallengeDetail activity.");
        mChallenge = DatabaseHelper.getInstance().getChallengeById(challengeId);
        setTitle(mChallenge.getName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_challenge_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }

    public Challenge getChallenge(){
        return this.mChallenge;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
